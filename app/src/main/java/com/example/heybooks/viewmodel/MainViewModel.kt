package com.example.heybooks.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.books.model.BookItem
import com.example.heybooks.Data.Event
import com.example.heybooks.Data.USER_NODE
import com.example.heybooks.Data.UseData
import com.example.heybooks.utils.DetailViewState
import com.example.heybooks.utils.LoginViewState
import com.example.heybooks.utils.SignUpViewState
import com.example.heybooks.utils.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage

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


    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    private val _detailViewState = MutableStateFlow<DetailViewState>(DetailViewState.Loading)
    private val _loginViewState = MutableStateFlow<LoginViewState>(LoginViewState.Loading)
    private val _signUpViewState = MutableStateFlow<SignUpViewState>(SignUpViewState.Loading)


    val books = _viewState.asStateFlow()
    val bookDetails = _detailViewState.asStateFlow()
    val login = _loginViewState.asStateFlow()
    val signup = _signUpViewState.asStateFlow()
    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }

    }


    // Helps to format the JSON
    val format = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
    }


    // get all the data from the Book.json
    fun getAllBooks(context: Context) = viewModelScope.launch {
        try {

            // read JSON File
            val myJson = context.assets.open("books.json").bufferedReader().use {
                it.readText()
            }

            // format JSON
            val bookList = format.decodeFromString<List<BookItem>>(myJson)
            _viewState.value = ViewState.Success(bookList)

        } catch (e: Exception){
            _viewState.value = ViewState.Error(e)
        }
    }


    // get book by ID
    fun getBookByID(context: Context, isbnNO:String) = viewModelScope.launch {
        try {

            // read JSON File
            val myJson = context.assets.open("books.json").bufferedReader().use {
                it.readText()
            }

            // format JSON
            val bookList = format.decodeFromString<List<BookItem>>(myJson) .filter { it.isbn.contentEquals(isbnNO)}.first()
            _detailViewState.value = DetailViewState.Success(bookList)

        } catch (e: Exception){
            _detailViewState.value = DetailViewState.Error(e)
        }
    }

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

    fun loginIn(email: String, password: String, context: Context){
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
                    if (it.isSuccessful) {
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
        eventMutableState.value=Event("Logged Out")
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