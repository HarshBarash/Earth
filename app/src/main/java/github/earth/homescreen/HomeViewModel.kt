package github.earth.homescreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import github.earth.TutorialRepository
import github.earth.models.Tutorial
import github.earth.utils.LOG_HOME_VIEW_MODEL
import github.earth.utils.Resource
import kotlinx.coroutines.launch


class HomeViewModel(
    private val tutorialRepository: TutorialRepository
) : ViewModel() {

    val uploadTutorialState = MutableLiveData<Resource?>()
    val getTutorialsState = MutableLiveData<Resource>()

    val tutorialImageUri = MutableLiveData<Uri?>()
    val username = MutableLiveData<String>()
    private val email = MutableLiveData<String>()
    val profileImageUri = MutableLiveData<String>()
    var tutorialList = MutableLiveData<ArrayList<Tutorial>>()

    init {
        getAllTutorial()
    }


    fun userLogout() {
        val auth = FirebaseAuth.getInstance()
        auth.signOut()
    }

    fun getCurrentUserDetails() = viewModelScope.launch {
        try {
            val currentUser = tutorialRepository.getCurrentlyLoggedInUserDetails()
            if (currentUser.profileImageUrl != null) {
                profileImageUri.postValue(currentUser.profileImageUrl!!)
            }
            username.postValue(currentUser.username)
            email.postValue(currentUser.email)
        } catch (e: Exception) {
            Log.d(LOG_HOME_VIEW_MODEL, "getCurrentUserDetails: ${e.message}")
        }
    }


    fun uploadTutorialDetailsToFirestore(
        title: String,
        materials: String,
        time: Int,
        description: String,
    ) {
        uploadTutorialState.postValue(Resource.Loading())
        try {
            if (title.isNotEmpty() && description.isNotEmpty() && tutorialImageUri.value != null && time > 0) {
                uploadTutorialImageToFirebaseStorage(title, materials, description, time)
            } else {
                uploadTutorialState.postValue(Resource.Error("Please Fill the Details or Select Image"))
            }
        } catch (e: Exception) {
            uploadTutorialState.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun uploadTutorialImageToFirebaseStorage(
        title: String,
        materials: String,
        description: String,
        time: Int,
    ) =
        viewModelScope.launch {
            try {
                tutorialImageUri.value?.let {
                    val uploadedTutorialImageUri = tutorialRepository.uploadTutorialImage(it)
                    saveTutorialToFirestoreDatabase(
                        title,
                        materials,
                        description,
                        time,
                        uploadedTutorialImageUri.toString()
                    )
                }
            } catch (e: Exception) {
                uploadTutorialState.postValue(e.message?.let { github.earth.utils.Resource.Error(it) })
            }
        }

    private fun saveTutorialToFirestoreDatabase(
        title: String,
        materials: String,
        description: String,
        time: Int,
        uploadedTutorialImageUri: String

    ) = viewModelScope.launch {
        val tutorial = Tutorial(
            title,
            materials,
            description,
            time,
            uploadedTutorialImageUri,
            username.value.toString(),
            profileImageUri.value.toString(),
            System.currentTimeMillis()
        )
        try {
            tutorialRepository.saveTutorialToFirestore(tutorial)
            uploadTutorialState.postValue(Resource.Success("Tutorial Uploaded Successfully"))
        } catch (e: Exception) {
            uploadTutorialState.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    private fun getAllTutorial() {
        getTutorialsState.postValue(Resource.Loading())
        try {
            tutorialList = tutorialRepository.getAllTutorials()
            getTutorialsState.postValue(Resource.Success("New Tutorial"))
        } catch (e: Exception) {
            getTutorialsState.postValue(e.message?.let { Resource.Error(it) })
        }
    }

    fun setTutorialImageUri(uri: Uri) {
        tutorialImageUri.value = uri
    }

    fun doneTutorialImageUri() {
        tutorialImageUri.postValue(null)
    }

    fun doneTutorialState() {
        uploadTutorialState.postValue(null)
    }
}