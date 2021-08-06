package github.earth.homescreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.google.firebase.auth.FirebaseAuth
import github.earth.models.Tutorial

import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val blogRepository: BlogRepository
) : ViewModel() {

    val uploadTutorialState = MutableLiveData<Resource>()
    val getTutorialsState = MutableLiveData<Resource>()

    val postImageUri = MutableLiveData<Uri>()
    val username = MutableLiveData<String>()
    private val email = MutableLiveData<String>()
    val profileImageUri = MutableLiveData<String>()
    var tutorialList = MutableLiveData<ArrayList<Tutorial>>()

    init {
        getAllTutorial()
    }



    fun getCurrentUserDetails() = viewModelScope.launch {
        try {
            val currentUser = blogRepository.getCurrentlyLoggedInUserDetails()
            username.postValue(currentUser.username)
            email.postValue(currentUser.email)
            profileImageUri.postValue(currentUser.profileImageUrl)
        } catch (e: Exception) {
            Log.d(TAG, "getCurrentUserDetails: ${e.message}")
        }
    }

    fun uploadPostDetailsToFirestore(title: String, description: String) {
        uploadTutorialState.postValue(Resource.Loading())
        try {
            if (title.isNotEmpty() && description.isNotEmpty() && postImageUri.value != null) {
                uploadPostImageToFirebaseStorage(title, description)
            } else {
                uploadTutorialState.postValue(Resource.Error("Please Fill the Details or Select Image"))
            }
        } catch (e: Exception) {
            uploadTutorialState.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun uploadPostImageToFirebaseStorage(title: String, description: String) =
        viewModelScope.launch {
            try {
                postImageUri.value?.let {
                    val uploadedPostImageUri = blogRepository.uploadPostImage(it)
                    savePostToFirestoreDatabase(title, description, uploadedPostImageUri.toString())
                }
            } catch (e: Exception) {
                uploadTutorialState.postValue(e.message?.let { Resource.Error(it) })
            }
        }

    private fun savePostToFirestoreDatabase(
        title: String,
        description: String,
        uploadedPostImageUri: String
    ) = viewModelScope.launch {
        val tutorial = Tutorial(
            title,
            description,
            uploadedPostImageUri,
            email.value.toString(),
            username.value.toString(),
            profileImageUri.value.toString(),
            System.currentTimeMillis()
        )
        try {
            blogRepository.savePostToFirestore(tutorial)
            uploadTutorialState.postValue(Resource.Success("Tutorial Uploaded Successfully"))
        } catch (e: Exception) {
            uploadTutorialState.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun getAllPost() {
        getTutorialsState.postValue(Resource.Loading())
        try {
            tutorialList = blogRepository.getAllPosts()
            getTutorialsState.postValue(Resource.Success("New Tutorial"))
        } catch (e: Exception) {
            getTutorialsState.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    fun setTutorialImageUri(uri: Uri) {
        postImageUri.value = uri
    }

    fun doneTutorialImageUri() {
        postImageUri.postValue(null)
    }

    fun doneTutorialState() {
        uploadTutorialState.postValue(null)
    }
}