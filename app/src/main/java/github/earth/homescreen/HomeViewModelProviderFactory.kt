package github.earth.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import github.earth.TutorialRepository

class HomeViewModelProviderFactory(
    private val tutorialRepository: TutorialRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(tutorialRepository) as T
    }
}