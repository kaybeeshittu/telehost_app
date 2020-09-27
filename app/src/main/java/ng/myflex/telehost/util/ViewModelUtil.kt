package ng.myflex.telehost.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelUtil {
    companion object {
        fun <T : ViewModel> create(viewModel: T): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return if (!modelClass.isAssignableFrom(viewModel.javaClass)) {
                        throw IllegalArgumentException("unknown model class $modelClass")
                    } else {
                        @Suppress("UNCHECKED_CAST")
                        viewModel as T
                    }
                }

            }
        }
    }
}