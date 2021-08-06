package github.earth.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.viveksharma.firebaseblog.repository.TutorialRepository

class HomeViewModelProviderFactory(
    private val blogRepository: TutorialRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(blogRepository) as T
    }
}