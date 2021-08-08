package github.earth.homescreen

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import github.earth.TutorialRepository
import github.earth.models.Tutorial
import github.earth.models.User
import github.earth.utils.FirebaseHelper
import github.earth.utils.LOG_HOME_VIEW_MODEL
import kotlinx.coroutines.launch


class HomeViewModel(
    private val tutorialRepository: TutorialRepository
) : ViewModel() {

    private lateinit var mFirebaseHelper: FirebaseHelper
    private lateinit var mUser : User

    val uploadTutorialState = MutableLiveData<github.earth.utils.Resource?>()
    val getTutorialsState = MutableLiveData<github.earth.utils.Resource>()

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
            username.postValue(mUser.username)
            email.postValue(mUser.email)
            if (mUser.photo != null) {
                profileImageUri.postValue(mUser.photo!!)
            }
        } catch (e: Exception) {
            Log.d(LOG_HOME_VIEW_MODEL, "getCurrentUserDetails: ${e.message}")
        }
    }

    fun uploadTutorialDetailsToFirestore(title: String, materials: String, time: Int, description: String, link: String) {
        uploadTutorialState.postValue(github.earth.utils.Resource.Loading())
        try {
            if (title.isNotEmpty() && description.isNotEmpty() && tutorialImageUri.value != null && link.isNotEmpty() && time > 0) {
                uploadTutorialImageToFirebaseStorage(title, materials, description, time, link)
            } else {
                uploadTutorialState.postValue(github.earth.utils.Resource.Error("Please Fill the Details or Select Image"))
            }
        } catch (e: Exception) {
            uploadTutorialState.postValue(e.message?.let { github.earth.utils.Resource.Error(it) })
        }
    }

    private fun uploadTutorialImageToFirebaseStorage(title: String, materials: String, description: String, time: Int, link: String) =
        viewModelScope.launch {
            try {
                tutorialImageUri.value?.let {
                    val uploadedTutorialImageUri = tutorialRepository.uploadTutorialImage(it)
                    saveTutorialToFirestoreDatabase(title, materials, description, time, link, uploadedTutorialImageUri.toString())
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
        link: String,
        uploadedTutorialImageUri: String

    ) = viewModelScope.launch {
        val tutorial = Tutorial(
            title,
            materials,
            description,
            time,
            link,
            uploadedTutorialImageUri,
            username.value.toString(),
            profileImageUri.value.toString(),
            System.currentTimeMillis()
        )
        try {
            tutorialRepository.saveTutorialToFirestore(tutorial)
            uploadTutorialState.postValue(github.earth.utils.Resource.Success("Tutorial Uploaded Successfully"))
        } catch (e: Exception) {
            uploadTutorialState.postValue(e.message?.let { github.earth.utils.Resource.Error(it) })
        }
    }

    private fun getAllTutorial() {
        getTutorialsState.postValue(github.earth.utils.Resource.Loading())
        try {
            tutorialList = tutorialRepository.getAllTutorials()
            getTutorialsState.postValue(github.earth.utils.Resource.Success("New Tutorial"))
        } catch (e: Exception) {
            getTutorialsState.postValue(e.message?.let { github.earth.utils.Resource.Error(it) })
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