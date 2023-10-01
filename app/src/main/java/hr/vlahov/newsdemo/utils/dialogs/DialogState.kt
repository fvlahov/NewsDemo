package hr.vlahov.newsdemo.utils.dialogs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun rememberDialogState(isOpenedInitially: Boolean = false): MutableState<Boolean> {
    return rememberSaveable { mutableStateOf(isOpenedInitially) }
}