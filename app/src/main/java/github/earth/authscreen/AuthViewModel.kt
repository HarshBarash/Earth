package github.earth.authscreen

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import github.earth.TutorialRepository
import github.earth.models.User
import github.earth.utils.Resource
import kotlinx.coroutines.launch


class AuthViewModel(
    private val tutorialRepository: TutorialRepository
) : ViewModel() {

    val registerState = MutableLiveData<Resource?>()
    val loginState = MutableLiveData<Resource?>()
    val profileImageUri = MutableLiveData<Uri>()

    fun registerUser(
        username: String,
        email: String,
        password: String,
    ) =
        viewModelScope.launch {

            registerState.postValue(Resource.Loading())
            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()) {
                    try {
                        tutorialRepository.registerUser(email, password)
                        uploadImageToFirebaseStorage(username, email)
                    } catch (e: Exception) {
                        registerState.postValue(e.message?.let { Resource.Error(it) })
                    }
            } else {
                registerState.postValue(Resource.Error("Please Fill The Details"))
            }
        }

    fun loginUser(email: String, password: String) = viewModelScope.launch {

        loginState.postValue(Resource.Loading())
        if (email.isNotEmpty() && password.isNotEmpty()) {
            try {
                tutorialRepository.loginUser(email, password)
                loginState.postValue(Resource.Success("Successfully Logged In"))
            } catch (e: Exception) {
                loginState.postValue(e.message?.let { Resource.Error(it) })
            }
        } else {
            loginState.postValue(Resource.Error("Please Fill The Details!"))
        }
    }

    private fun uploadImageToFirebaseStorage(username: String, email: String) =
        viewModelScope.launch {
            try {
                if (profileImageUri.value == null) {
                    saveUserToFirestoreDatabaseWithoutImage(username, email)
                }
                profileImageUri.value?.let {
                    val profileImageUri = tutorialRepository.uploadProfileImage(it)
                    saveUserToFirestoreDatabase(username, email, profileImageUri.toString())
                }
            } catch (e: Exception) {
                registerState.postValue(e.message?.let { Resource.Error(it) })
            }
        }

    private fun saveUserToFirestoreDatabase(
        username: String,
        email: String,
        profileImageUri: String
    ) = viewModelScope.launch {
        val user = User(username, email, profileImageUri)
        try {
            tutorialRepository.saveUserToFirestore(user)
            registerState.postValue(Resource.Success("Successfully Registered"))
        } catch (e: Exception) {
            registerState.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun saveUserToFirestoreDatabaseWithoutImage(
        username: String,
        email: String,
    ) = viewModelScope.launch {
        val user = User(username, email)
        try {
            tutorialRepository.saveUserToFirestore(user)
            registerState.postValue(Resource.Success("Successfully Registered"))
        } catch (e: Exception) {
            registerState.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    fun setProfileImageUri(uri: Uri) {
        profileImageUri.postValue(uri)
    }

    fun doneRegisterState() {
        registerState.postValue(null)
    }

    fun doneLoginState() {
        loginState.postValue(null)
    }
}