package hr.vlahov.newsdemo.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.vlahov.newsdemo.base.errors.BaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    protected val errors = MutableStateFlow<BaseError?>(null)
    val errorsFlow = errors.asStateFlow()

    protected fun launchIn(
        scope: CoroutineScope = viewModelScope,
        action: suspend CoroutineScope.() -> Unit,
    ) {
        scope.launch {
            action()
        }
    }
}