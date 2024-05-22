package com.example.heybooks.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.model.BookItems
import com.example.books.model.Review
import com.example.books.model.ReviewWithUserInfo
import com.example.heybooks.model.Event
import com.example.heybooks.model.USER_NODE
import com.example.heybooks.model.UseData
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.utils.DetailViewState
import com.example.heybooks.utils.DetailViewStateTab
import com.example.heybooks.utils.LoginViewState
import com.example.heybooks.utils.ReviewState
import com.example.heybooks.utils.SavedBooksState
import com.example.heybooks.utils.SignUpViewState
import com.example.heybooks.utils.UsersViewState
import com.example.heybooks.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

@HiltViewModel
class MainViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db: FirebaseFirestore,
    val storage: FirebaseStorage
) : ViewModel() {

    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UseData?>(null)
    private val firestore = FirebaseFirestore.getInstance()

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    private val _detailViewState = MutableStateFlow<DetailViewState>(DetailViewState.Loading)
    private val _loginViewState = MutableStateFlow<LoginViewState>(LoginViewState.Empty)
    private val _signUpViewState = MutableStateFlow<SignUpViewState>(SignUpViewState.Loading)
    private val _savedBooksState = MutableStateFlow<SavedBooksState>(SavedBooksState.Loading)
    private val _detailViewStateTab =
        MutableStateFlow<DetailViewStateTab>(DetailViewStateTab.Loading)
    private val _reviewsViewState = MutableStateFlow<ReviewState>(ReviewState.Loading)


    private val _inProcess = MutableLiveData<Boolean>()
    val iinProcess: LiveData<Boolean> get() = _inProcess

    //bookList
    private val _viewLiveData = MutableLiveData<ViewState>()
    val viewLiveData: LiveData<ViewState> get() = _viewLiveData

    //review
    private val _reviewsLiveData = MutableLiveData<ReviewState>()
    val reviewsLiveData: LiveData<ReviewState> get() = _reviewsLiveData

    //user
    private val _usersViewState = MutableLiveData<UsersViewState>()
    val usersViewState: LiveData<UsersViewState> get() = _usersViewState

    val savedBooksState = _savedBooksState.asStateFlow()
    val books = _viewState.asStateFlow()
    val bookDetails = _detailViewState.asStateFlow()
    val login = _loginViewState.asStateFlow()
    val signup = _signUpViewState.asStateFlow()
    val bookDetailsTab = _detailViewStateTab.asStateFlow()

    val reviews = _reviewsViewState.asStateFlow()

    //    val users =  _usersViewState.asStateFlow()
    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }

    }


    //    fun getBookByIsbn(isbn: String?): BookItems? {
//        return _viewState.value.find { it.isbn == isbn }
//    }

    private val _bookListLiveData = MutableLiveData<List<BookItems>>()
    val bookListLiveData: LiveData<List<BookItems>> = _bookListLiveData

    //Thêm Sửa Xóa Sách
    fun deleteBook(isbn: String, context: Context) = viewModelScope.launch {
        try {
            // Lấy sách từ Firestore
            val bookRef = firestore.collection("Books").document(isbn)
            val bookDocument = bookRef.get().await()
            val book = bookDocument.toObject(BookItems::class.java)
            val imageRef = book?.let { storage.getReferenceFromUrl(it.imageUrl) }
            try {
                imageRef?.delete()?.await()
                bookRef.delete().await()
            } catch (e: Exception) {
                bookRef.delete().await()
            }
            getAllBooks()
            Toast.makeText(
                context, "Succeeded", Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context, "Faill", Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun deleteReviews(reviewId: String, isbn: String, context: Context) = viewModelScope.launch {
        try {
            val reviewRef = firestore.collection("Reviews").document(reviewId)
            reviewRef.delete().await()
            loadReviews(isbn)
            Toast.makeText(
                context, "Succeeded", Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context, "Fail", Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun deleteImg(imageUrl: String) = viewModelScope.launch {
        try {
            val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl)
            storageRef.delete().await()
            getAllBooks()
        } catch (e: Exception) {
            Log.e("DeleteImage", "Failed to delete image: ${e.message}")
        }
    }


    fun updateBook(
        isbn: String,
        title: String,
        authors: String,
        categories: String,
        bookIntroduction: String,
        bookContent: String,
        imageUrl: String,
    ) = viewModelScope.launch {
        val bookData = hashMapOf(
            "title" to title,
            "authors" to authors,
            "categories" to categories,
            "bookintroduction" to bookIntroduction,
            "bookcontent" to bookContent,
            "imageUrl" to imageUrl
        )
        try {
            firestore.collection("Books")
                .document(isbn)
                .update(bookData as Map<String, Any>)
                .await()
            getAllBooks()
            Log.d("MainViewModel", "Book updated successfully")
        } catch (e: Exception) {
            Log.e("MainViewModel", "Error updating book", e)
        }

    }


    fun addReview(isbn: String, comment: String, context: Context) = viewModelScope.launch {
        val userId = auth.currentUser?.uid ?: return@launch
        val currentTime = System.currentTimeMillis()
        val review = Review(
            reviewId = currentTime.toString(),
            isbn = isbn,
            userId = userId,
            comment = comment
        )
        firestore.collection("Reviews")
            .document(review.reviewId)
            .set(review)
            .addOnSuccessListener {
                Toast.makeText(
                    context, "Succeeded", Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context, "Succeeded", Toast.LENGTH_SHORT
                ).show()
            }
        loadReviews(isbn)
    }

    fun loadReviews(isbn: String) = viewModelScope.launch {
        try {
            val snapshot = firestore.collection("Reviews")
                .whereEqualTo("isbn", isbn)
                .get()
                .await()

            val reviewsList = mutableListOf<ReviewWithUserInfo>()

            for (document in snapshot.documents) {
                val review = document.toObject(Review::class.java)
                review?.let {
                    // Lấy thông tin của người dùng từ bảng Users
                    val userId = it.userId
                    val userSnapshot = firestore.collection("User").document(userId).get().await()
                    val userName = userSnapshot.getString("name")
                    val imageUrl = userSnapshot.getString("imageUrl")

                    // Tạo ra một đối tượng ReviewWithUserInfo
                    val reviewWithUserInfo = ReviewWithUserInfo(
                        reviewId = it.reviewId,
                        isbn = it.isbn,
                        userId = it.userId,
                        comment = it.comment,
                        userName = userName ?: "Unknown",
                        imageUrl = imageUrl ?: "Unknown"
                    )

                    reviewsList.add(reviewWithUserInfo)
                }
            }

            reviewsList.sortByDescending { it.reviewId }
//            sortBy

//            _reviewsViewState.value = ReviewState.Success(reviewsList)
            _reviewsLiveData.postValue(ReviewState.Success(reviewsList))
        } catch (e: Exception) {
            _reviewsViewState.value = ReviewState.Error(e)
        }
    }

    fun getSavedBooksForUser() = viewModelScope.launch {
        val userId = auth.currentUser?.uid ?: return@launch
        try {
            val booksSnapshot = firestore.collection("SavedBooks")
                .document(userId)
                .collection("Books")
                .get()
                .await()
            val bookList = booksSnapshot.toObjects(BookItems::class.java)
            _savedBooksState.value = SavedBooksState.Success(bookList)
        } catch (e: Exception) {
            _savedBooksState.value = SavedBooksState.Error(e)
        }
    }


    fun saveBookForUser(book: BookItems) = viewModelScope.launch {
        val userId = auth.currentUser?.uid ?: return@launch
        try {
            val bookWithUserId =
                book.copy(userId = userId) // Tạo một bản sao của sách với userId mới
            firestore.collection("SavedBooks")
                .document(userId)
                .collection("Books")
                .document(book.isbn)
                .set(bookWithUserId)
                .await()
        } catch (e: Exception) {

        }
    }

    fun removeBookForUser(book: BookItems) = viewModelScope.launch {
        val userId = auth.currentUser?.uid ?: return@launch
        try {
            firestore.collection("SavedBooks")
                .document(userId)
                .collection("Books")
                .document(book.isbn)
                .delete()
                .await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    fun getAllBooks() = viewModelScope.launch {
        try {
            val booksSnapshot = firestore.collection("Books").get().await()
            val bookList = booksSnapshot.toObjects(BookItems::class.java)
            bookList.sortByDescending { it.isbn }
            _viewLiveData.postValue(ViewState.Success(bookList))
            _viewState.value = ViewState.Success(bookList)
        } catch (e: Exception) {
            _viewState.value = ViewState.Error(e)
        }
    }

    fun addBook(
        title: String,
        authors: String,
        categories: String,
        bookintroduction: String,
        bookcontent: String,
        imageUrl: String,
        context: Context
    ) {
        val currentTime = System.currentTimeMillis().toString()
        val dbBooks = firestore.collection("Books")

        // Tạo một document mới với key currentTime.
        val newBookDoc = dbBooks.document(currentTime)
        val bookItem = BookItems(
            isbn = currentTime,
            title = title,
            authors = authors,
            categories = categories,
            bookintroduction = bookintroduction,
            bookcontent = bookcontent,
            imageUrl = imageUrl
        )
        newBookDoc.set(bookItem)
            .addOnSuccessListener {
                Toast.makeText(
                    context, "Succeeded", Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context, "Faill\n$e", Toast.LENGTH_SHORT
                ).show()
            }
    }


    fun uploadToStorage(uri: Uri, context: Context, type: String, onSuccess: (String) -> Unit) {
        val storageRef = storage.reference
        val unique_image_name = UUID.randomUUID().toString()
        val spaceRef = storageRef.child("images/$unique_image_name.jpg")

        val byteArray: ByteArray? = context.contentResolver
            .openInputStream(uri)
            ?.use { it.readBytes() }

        byteArray?.let {
            val uploadTask = spaceRef.putBytes(byteArray)
            uploadTask.addOnFailureListener {
                Toast.makeText(
                    context,
                    "Upload failed",
                    Toast.LENGTH_SHORT
                ).show()
            }.addOnSuccessListener { _ ->
                Toast.makeText(
                    context,
                    "Upload succeeded",
                    Toast.LENGTH_SHORT
                ).show()

                spaceRef.downloadUrl.addOnSuccessListener { imageUrl ->
                    // Gọi hàm gọi lại onSuccess với đường dẫn của ảnh
                    onSuccess(imageUrl.toString())
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Failed to get download URL",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    fun uploadImg(uri: Uri, context: Context, imageName: String, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val storageRef = Firebase.storage.reference
                val imageRef = storageRef.child("images/$imageName.jpg")

                // Upload the file
                val uploadTask = imageRef.putFile(uri).await()

                // Get the download URL
                val downloadUrl = imageRef.downloadUrl.await().toString()
                onComplete(downloadUrl)
//                Toast.makeText(
//                    context, "Success",
//                    Toast.LENGTH_SHORT
//                ).show()
            } catch (e: Exception) {
                // Handle the error
//                Toast.makeText(
//                    context, "Fail",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }

    // get book by ID
    suspend fun getBookByID(isbnNO: String) {
        try {
            val firestore = FirebaseFirestore.getInstance()
            val docRef = firestore.collection("Books").document(isbnNO)
            val document = docRef.get().await()

            if (document.exists()) {
                val bookData = document.toObject(BookItems::class.java)
                _detailViewState.value = bookData?.let { DetailViewState.Success(it) }!!
            } else {
                _detailViewState.value =
                    DetailViewState.Error(Exception("Book not found for ISBN: $isbnNO"))
            }
        } catch (e: Exception) {
            _detailViewState.value = DetailViewState.Error(e)
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("App ", "Book execption:", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNotEmpty()) errorMsg else customMessage
        eventMutableState.value = Event(message)
        inProcess.value = false
    }


    @SuppressLint("SuspiciousIndentation")
    fun createOrUpdateProfile(
        name: String? = null,
        number: String? = null,
        imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            val useData = UseData(
                userId = uid,
                name = name ?: userData.value?.name,
                number = number ?: userData.value?.number,
                imageUrl = imageUrl ?: userData.value?.imageUrl
            )

            inProcess.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener { document ->
                if (document.exists()) {
                    // Update user data
                    db.collection(USER_NODE).document(uid).set(useData, SetOptions.merge())
                        .addOnSuccessListener {
                            inProcess.value = false
                        }
                        .addOnFailureListener { exception ->
                            inProcess.value = false
                        }
                } else {
                    // Create new user data
                    db.collection(USER_NODE).document(uid).set(useData)
                        .addOnSuccessListener {
                            inProcess.value = false
                            getUserData(uid)
                        }
                        .addOnFailureListener { exception ->
                            inProcess.value = false
                        }
                }
            }.addOnFailureListener { exception ->
                inProcess.value = false
            }
        }
    }

    fun signUp(name: String, number: String, email: String, password: String, context: Context) {
        inProcess.value = true
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            Toast.makeText(
                context, "Please Fill",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        inProcess.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        signIn.value = true
                        createOrUpdateProfile(name, number)
                        Toast.makeText(
                            context, "Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context, "Sign up failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    context, "number already exit",
                    Toast.LENGTH_SHORT
                ).show()
                inProcess.value = false
            }
        }
    }

    fun loginIn(email: String, password: String, context: Context, actions: MainActions) {
        if (email.isEmpty() or password.isEmpty()) {
            Toast.makeText(
                context, "Please Fill",
                Toast.LENGTH_SHORT
            ).show()
            return

        } else {
            inProcess.value = true
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (email == "admin@gmail.com" && password == "123456789") {
//                        signIn.value = false
//                        inProcess.value = true
                        // Navigate to admin screen
                        actions.gotoAdminBookList()
                    } else
                        if (it.isSuccessful && email != "admin@gmail.com") {
                            signIn.value = true
                            inProcess.value = false
                            auth.currentUser?.uid?.let {
                                getUserData(it)
                            }
                        } else {
                            Toast.makeText(
                                context, "Login fail",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
        }

    }


    fun logOut() {
        auth.signOut()
        signIn.value = false
        userData.value = null
        eventMutableState.value = Event("Logged Out")
    }

    private fun getUserData(uid: String) {
        inProcess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("getUserData", "Con not retreive User")
            }
            if (value != null) {
                var user = value.toObject<UseData>()
                userData.value = user
                inProcess.value = false
            }
        }
    }


    fun loadUser() = viewModelScope.launch {
        _usersViewState.value = UsersViewState.Loading
        try {
            val userId = auth.currentUser?.uid ?: return@launch
            val userSnapshot = firestore.collection("User").document(userId).get().await()
            if (userSnapshot.exists()) {
                val userData = userSnapshot.toObject(UseData::class.java)
                userData?.let {
                    _usersViewState.value = UsersViewState.Success(listOf(it))
                } ?: run {
                    _usersViewState.value = UsersViewState.Empty
                }
            } else {
                _usersViewState.value = UsersViewState.Empty
            }
        } catch (e: Exception) {
            _usersViewState.value = UsersViewState.Error(e)
            Log.d("loadUser", "Load user failed", e)
        }
    }

    fun updateProfile(
        uid: String,
        name: String,
        number: String,
        imageUrl: String,
        context: Context
    ) = viewModelScope.launch {
        val usersData = hashMapOf(
            "userId" to uid,
            "name" to name,
            "number" to number,
            "imageUrl" to imageUrl
        )
        try {
            firestore.collection("User")
                .document(uid)
                .update(usersData as Map<String, Any>)
                .await()
            loadUser()
            Toast.makeText(
                context, "Success",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context, "Fail",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}

@Composable
fun CheckSignedIn(viewModel: MainViewModel, actions: MainActions) {
    val alreadySignIn = remember { mutableStateOf(false) }
    val signIn = viewModel.signIn.value
    if (signIn && !alreadySignIn.value) {
        alreadySignIn.value = true
        actions.gotoBookList()
    }
}