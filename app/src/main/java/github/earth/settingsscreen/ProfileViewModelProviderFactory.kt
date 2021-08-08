package github.earth.settingsscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import github.earth.TutorialRepository

class ProfileViewModelProviderFactory(
    private val tutorialRepository: TutorialRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(tutorialRepository) as T
    }
}