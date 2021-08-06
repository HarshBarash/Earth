package github.earth.homescreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.google.firebase.auth.FirebaseAuth
import github.earth.TutorialRepository
import github.earth.models.Tutorial

import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val tutorialRepository: TutorialRepository
) : ViewModel() {

    val uploadTutorialState = MutableLiveData<github.earth.utils.Resource>()
    val getTutorialsState = MutableLiveData<github.earth.utils.Resource>()

    val tutorialImageUri = MutableLiveData<Uri>()
    val username = MutableLiveData<String>()
    private val email = MutableLiveData<String>()
    val profileImageUri = MutableLiveData<String>()
    var tutorialList = MutableLiveData<ArrayList<Tutorial>>()
}// временно

//    init {
//        getAllTutorial()
//    }



//    fun getCurrentUserDetails() = viewModelScope.launch {
//        try {
//            val currentUser = tutorialRepository.getCurrentlyLoggedInUserDetails()
//            username.tutorialValue(currentUser.username)
//            email.tutorialValue(currentUser.email)
//            profileImageUri.tutorialValue(currentUser.profileImageUrl)
//        } catch (e: Exception) {
//            Log.d(TAG, "getCurrentUserDetails: ${e.message}")
//        }
//    }
//
//    fun uploadTutorialDetailsToFirestore(title: String, description: String) {
//        uploadTutorialState.tutorialValue(github.earth.utils.Loading())
//        try {
//            if (title.isNotEmpty() && description.isNotEmpty() && tutorialImageUri.value != null) {
//                uploadTutorialImageToFirebaseStorage(title, description)
//            } else {
//                uploadTutorialState.tutorialValue(github.earth.utils.Error("Please Fill the Details or Select Image"))
//            }
//        } catch (e: Exception) {
//            uploadTutorialState.tutorialValue(e.message?.let { github.earth.utils..Error(it) })
//        }
//    }
//
//    private fun uploadTutorialImageToFirebaseStorage(title: String, description: String) =
//        viewModelScope.launch {
//            try {
//                tutorialImageUri.value?.let {
//                    val uploadedTutorialImageUri = tutorialRepository.uploadTutorialImage(it)
//                    saveTutorialToFirestoreDatabase(title, description, uploadedTutorialImageUri.toString())
//                }
//            } catch (e: Exception) {
//                uploadTutorialState.tutorialValue(e.message?.let { github.earth.utils.Resource.Error(it) })
//            }
//        }
//
//    private fun saveTutorialToFirestoreDatabase(
//        title: String,
//        description: String,
//        uploadedTutorialImageUri: String
//    ) = viewModelScope.launch {
//        val tutorial = Tutorial(
//            title,
//            description,
//            uploadedTutorialImageUri,
//            email.value.toString(),
//            username.value.toString(),
//            profileImageUri.value.toString(),
//            System.currentTimeMillis()
//        )
//        try {
//            tutorialRepository.saveTutorialToFirestore(tutorial)
//            uploadTutorialState.tutorialValue(github.earth.utils.Resource.Success("Tutorial Uploaded Successfully"))
//        } catch (e: Exception) {
//            uploadTutorialState.tutorialValue(e.message?.let { github.earth.utils.Resource.Error(it) })
//        }
//    }
//
//    private fun getAllTutorial() {
//        getTutorialsState.tutorialValue(github.earth.utils.Resource.Loading())
//        try {
//            tutorialList = tutorialRepository.getAllTutorials()
//            getTutorialsState.tutorialValue(github.earth.utils.Resource.Success("New Tutorial"))
//        } catch (e: Exception) {
//            getTutorialsState.tutorialValue(e.message?.let { github.earth.utils.Resource.Error(it) })
//        }
//    }
//
//    fun setTutorialImageUri(uri: Uri) {
//        tutorialImageUri.value = uri
//    }
//
//    fun doneTutorialImageUri() {
//        tutorialImageUri.tutorialValue(null)
//    }
//
//    fun doneTutorialState() {
//        uploadTutorialState.tutorialValue(null)
//    }
//}