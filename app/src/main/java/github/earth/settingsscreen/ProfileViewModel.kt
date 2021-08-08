package github.earth.settingsscreen

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import github.earth.TutorialRepository
import github.earth.models.User
import github.earth.utils.Resource
import kotlinx.coroutines.launch


class ProfileViewModel(

    private val tutorialRepository: TutorialRepository
) : ViewModel() {

    private lateinit var currentUser: User

    val profileImageUri = MutableLiveData<String?>()
    val profileUsername = MutableLiveData<String?>()
    val updateState = MutableLiveData<Resource?>()

    fun getCurrentUserDetails() = viewModelScope.launch {
        try {
            currentUser = tutorialRepository.getCurrentlyLoggedInUserDetails()
            if (currentUser.profileImageUrl != null) {
                profileImageUri.postValue(currentUser.profileImageUrl!!)
            }
            profileUsername.postValue(currentUser.username)
        } catch (e: Exception) {
            updateState.postValue(Resource.Error(e.message.toString()))
        }
    }

    fun setImageUri(uri: Uri) {
        profileImageUri.value = uri.toString()
    }

    fun updateProfile(username: String) {
        profileUsername.value = username
        if (currentUser.profileImageUrl != profileImageUri.value) {             //if user selected new image to update, then uploading
            uploadImage()
        } else {
            updateDataInFirestore()
        }
    }

    private fun uploadImage() = viewModelScope.launch {
        updateState.postValue(Resource.Loading())
        try {
            val uploadedImageUri = tutorialRepository.uploadProfileImage(Uri.parse(profileImageUri.value))
            profileImageUri.value = uploadedImageUri.toString()
            updateDataInFirestore()
        } catch (e: Exception) {
            updateState.postValue(Resource.Error(e.message.toString()))
        }
    }

    private fun updateDataInFirestore() = viewModelScope.launch {
        updateState.postValue(Resource.Loading())
        try {
            val map = getUpdatedUserMap()
            if (map.isEmpty()) {
                updateState.postValue(Resource.Error("Nothing To Update"))
                return@launch
            }
            tutorialRepository.updateProfile(currentUser, map)
            updateState.postValue(Resource.Success("Updated"))
            getCurrentUserDetails()                                     //regular profile update
        } catch (e: Exception) {
            updateState.postValue(Resource.Error(e.message.toString()))
        }
    }

    private fun getUpdatedUserMap(): Map<String, Any> {
        val profileImageUrl = profileImageUri.value
        val username = profileUsername.value
        val map = mutableMapOf<String, Any>()
        if (profileImageUrl!!.isNotEmpty() && profileImageUrl != currentUser.profileImageUrl) {
            map["profileImageUrl"] = profileImageUrl
        }
        if (username!!.isNotEmpty() && username != currentUser.username) {
            map["username"] = username
        }
        return map
    }

    fun doneProfileImageAndUsername() {
        profileImageUri.postValue(null)
        profileUsername.postValue(null)
    }

    fun doneUpdateState() {
        updateState.postValue(null)
    }
}