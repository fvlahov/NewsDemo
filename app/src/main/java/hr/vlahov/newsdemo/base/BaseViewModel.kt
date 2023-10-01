package hr.vlahov.newsdemo.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hr.vlahov.newsdemo.base.errors.BaseError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    protected val localErrors = MutableStateFlow<BaseError?>(null)
    val errors = localErrors.asStateFlow()

    protected val localUIState = MutableStateFlow<UIState>(UIState.Idle)
    val uiState = localUIState.asStateFlow()

    protected fun launchIn(
        scope: CoroutineScope = viewModelScope,
        action: suspend CoroutineScope.() -> Unit,
    ) {
        launchWithProgressIn(
            scope = scope,
            loadingState = null,
            successState = null,
            action = action
        )
    }

    protected fun launchWithProgressIn(
        scope: CoroutineScope = viewModelScope,
        loadingState: UIState? = UIState.Loading,
        successState: UIState? = UIState.Idle,
        action: suspend CoroutineScope.() -> Unit,
    ) {
        scope.launch {
            loadingState?.let { localUIState.emit(it) }
            action()
            successState?.let { localUIState.emit(it) }
        }
    }
}

interface UIState {
    object Idle : UIState
    object Loading : UIState

    data class Error(@StringRes val errorMessage: Int) : UIState
}