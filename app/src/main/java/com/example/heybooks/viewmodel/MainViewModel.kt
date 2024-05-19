package com.example.heybooks.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.model.BookItems
import com.example.books.model.Review
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
import com.example.heybooks.utils.UserState
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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

@HiltViewModel
class MainViewModel @Inject constructor(
    val auth: FirebaseAuth,
    var db : FirebaseFirestore,
    val storage: FirebaseStorage
): ViewModel() {


    var inProcess = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UseData?>(null)
    private val firestore = FirebaseFirestore.getInstance()



    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    private val _detailViewState = MutableStateFlow<DetailViewState>(DetailViewState.Loading)
    private val _loginViewState = MutableStateFlow<LoginViewState>(LoginViewState.Loading)
    private val _signUpViewState = MutableStateFlow<SignUpViewState>(SignUpViewState.Loading)
    private val _savedBooksState = MutableStateFlow<SavedBooksState>(SavedBooksState.Loading)
    private val _detailViewStateTab = MutableStateFlow<DetailViewStateTab>(DetailViewStateTab.Loading)
//    private val _reviewsViewState = MutableStateFlow<ReviewState>(ReviewState.Loading)
//    private val _usersViewState = MutableStateFlow<UsersViewState>(UsersViewState.Loading)

    val savedBooksState = _savedBooksState.asStateFlow()
    val books = _viewState.asStateFlow()
    val bookDetails = _detailViewState.asStateFlow()
    val login = _loginViewState.asStateFlow()
    val signup = _signUpViewState.asStateFlow()
    val bookDetailsTab = _detailViewStateTab.asStateFlow()
//    val reviews =  _reviewsViewState.asStateFlow()
//    val users =  _usersViewState.asStateFlow()
    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }

    }


    private val _reviewsViewState = MutableStateFlow<ReviewState>(ReviewState.Loading)
    val reviewsViewState: StateFlow<ReviewState> get() = _reviewsViewState

    private val _usersViewState = MutableStateFlow<UserState>(UserState.Loading)
    val usersViewState: StateFlow<UserState> get() = _usersViewState
//    fun getBookByIsbn(isbn: String?): BookItems? {
//        return _viewState.value.find { it.isbn == isbn }
//    }
    fun addReview(isbn: String, comment: String) = viewModelScope.launch {
        val userId = auth.currentUser?.uid ?: return@launch
        val reviewId = firestore.collection("Reviews").document().id
        val review = Review(
            reviewId = reviewId,
            isbn = isbn,
            userId = userId,
            comment = comment
        )
        firestore.collection("Reviews").document(reviewId).set(review).await()
        loadReviews(isbn)
    }

    fun loadReviews(isbn: String) = viewModelScope.launch {
        _reviewsViewState.value = ReviewState.Loading
        try {
            val snapshot = firestore.collection("Reviews")
                .whereEqualTo("isbn", isbn)
                .get()
                .await()
            val reviewsList = snapshot.toObjects(Review::class.java)
            _reviewsViewState.value = ReviewState.Success(reviewsList)
        } catch (e: Exception) {
            _reviewsViewState.value = ReviewState.Error(e)
        }
    }

    fun loadUsers() = viewModelScope.launch {
        _usersViewState.value = UserState.Loading
        try {
            val snapshot = firestore.collection("Users").get().await()
            val usersList = snapshot.toObjects(UseData::class.java)
            _usersViewState.value = UserState.Success(usersList)
        } catch (e: Exception) {
            _usersViewState.value = UserState.Error(e)
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
            val bookWithUserId = book.copy(userId = userId) // Tạo một bản sao của sách với userId mới
            firestore.collection("SavedBooks")
                .document(userId)
                .collection("Books")
                .document(book.isbn)
                .set(bookWithUserId)
                .await()
        } catch (e: Exception) {
            // Xử lý lỗi
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
    // get all the data from the Book.json
    fun getAllBooks() = viewModelScope.launch {
        try {
            val booksSnapshot = firestore.collection("Books").get().await()
            val bookList = booksSnapshot.toObjects(BookItems::class.java)
            _viewState.value = ViewState.Success(bookList)
        } catch (e: Exception) {
            _viewState.value = ViewState.Error(e)
        }
    }

    fun fetchBookData(callback: (List<BookItems>) -> Unit) = viewModelScope.launch  {
        val db = FirebaseFirestore.getInstance()
        val productsCollection = db.collection("Books")

        productsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val toList = mutableListOf<BookItems>()
                for (document in querySnapshot.documents) {
                    val product = document.toObject(BookItems::class.java)
                    product?.let {
                        toList.add(it)
                    }
                }
                callback(toList)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting products", exception)
            }
    }
    fun addBook(
        title: String, authors: String, categories: String,bookintroduction: String, bookcontent:String, imageUrl: String, context: Context
    ) {
        // Tạo một tham chiếu đến Firestore.
        val db = FirebaseFirestore.getInstance()

        // Tạo một collection reference cho bảng "Books".
        val dbBooks = db.collection("Books")

        // Tạo một document mới với key tự động.
        val newBookDoc = dbBooks.document()

        // Lấy key của document mới tạo.
        val isbn = newBookDoc.id

        // Tạo một đối tượng BookItems với các thông tin cần thiết.
        val bookItem = BookItems(isbn = isbn, title = title, authors = authors, categories = categories,bookintroduction = bookintroduction, bookcontent = bookcontent, imageUrl = imageUrl)

        // Thêm dữ liệu vào Firestore.
        newBookDoc.set(bookItem)
            .addOnSuccessListener {
                // Thành công.
                Toast.makeText(
                    context, "Thành công", Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener { e ->
                // Thất bại.
                Toast.makeText(
                    context, "Thất bại\n$e", Toast.LENGTH_SHORT
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
                _detailViewState.value = DetailViewState.Error(Exception("Book not found for ISBN: $isbnNO"))
            }
        } catch (e: Exception) {
            _detailViewState.value = DetailViewState.Error(e)
        }
    }


//    fun getBookByID(context: Context, isbnNO:String) = viewModelScope.launch {
//        try {
//
//            // read JSON File
//            val myJson = context.assets.open("books.json").bufferedReader().use {
//                it.readText()
//            }
//
//            // format JSON
//            val bookList = format.decodeFromString<List<BookItem>>(myJson) .filter { it.isbn.contentEquals(isbnNO)}.first()
//            _detailViewState.value = DetailViewState.Success(bookList)
//
//        } catch (e: Exception){
//            _detailViewState.value = DetailViewState.Error(e)
//        }
//    }

    //check email

//    fun loginIn(email: String, password: String) {
//
//        if (email.isEmpty() or password.isEmpty()) {
////            handleException(customMessage = "Please fill")
//            return
//
//        } else {
////            getAllBooks(context = context)
//        }
//    }


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
                            // You can add success handling here
                        }
                        .addOnFailureListener { exception ->
                            inProcess.value = false
                            handleException(exception, "Cannot update user")
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
                            handleException(exception, "Cannot create user")
                        }
                }
            }.addOnFailureListener { exception ->
                inProcess.value = false
                handleException(exception, "Cannot retrieve user")
            }
        }
    }

    fun signUp(name: String, number: String, email: String, password: String, context: Context) {
        inProcess.value = true
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = " Please Fill")
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
                        createOrUpdateProfile(name, number,)
                        Log.d("TAG", "signup: User Logged In")
                        Toast.makeText(
                            context, "Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        handleException(it.exception, customMessage = "sign up failed")
                        Toast.makeText(
                            context, "Sign up failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                handleException(customMessage = "number already exit")
                inProcess.value = false
            }
        }
    }

    fun loginIn(email: String, password: String, context: Context, actions: MainActions){
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill")
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
                        actions.gotoAdmin()
                    }
                    if (it.isSuccessful && email != "admin@gmail.com") {
                        signIn.value = true
                        inProcess.value = false
                        auth.currentUser?.uid?.let {
                            getUserData(it)
                        }
                    } else {
                        handleException(exception = it.exception, customMessage = " login failed")
                        Toast.makeText(
                            context, "Login fail",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }

    fun logOut(){
        auth.signOut()
        signIn.value=false
        userData.value=null
//        depopulateMessage()
        eventMutableState.value= Event("Logged Out")
    }

    private fun getUserData(uid: String) {
        inProcess.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->


            if (error != null) {
                handleException(error, " Con not retreive User")
            }
            if (value != null) {
                var user = value.toObject<UseData>()
                userData.value = user
                inProcess.value = false
//                populateChats()
//                populateStatuses()
            }
        }
    }

    fun updateProfile(){

    }

}