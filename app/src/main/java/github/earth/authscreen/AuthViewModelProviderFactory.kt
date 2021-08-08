package github.earth.authscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import github.earth.TutorialRepository

class AuthViewModelProviderFactory(
    private val TutorialRepository: TutorialRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AuthViewModel(TutorialRepository) as T
    }
}