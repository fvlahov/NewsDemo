package hr.vlahov.newsdemo.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.base.errors.BaseError

@Composable
fun ErrorHandler(
    error: BaseError?,
) {
    if (error == null)
        return


    when (error.displayType) {
        BaseError.ErrorDisplayType.SNACKBAR -> HandleSnackbar(error)
        BaseError.ErrorDisplayType.DIALOG -> HandleDialog(error)
        BaseError.ErrorDisplayType.NONE -> {}
    }
}

@Composable
private fun HandleSnackbar(error: BaseError) {
    val errorMessage = stringResource(id = error.messageRes)
    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }

    LaunchedEffect(error) {
        snackbarHostState.value.showSnackbar(
            message = errorMessage,
            withDismissAction = true,
            duration = SnackbarDuration.Short
        )
    }

    SnackbarHost(
        hostState = snackbarHostState.value,
        modifier = Modifier
            .padding(8.dp)
            .zIndex(999f)
    ) { snackbarData ->
        Snackbar(
            containerColor = MaterialTheme.colorScheme.error,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = snackbarData.visuals.message
            )
        }
    }
}

@Composable
private fun HandleDialog(error: BaseError) {
    val isDialogVisible = remember { mutableStateOf(true) }
    LaunchedEffect(error) {
        isDialogVisible.value = true
    }
    if (!isDialogVisible.value)
        return
    AlertDialog(
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        text = {
            Text(text = stringResource(id = error.messageRes))
        },
        onDismissRequest = { isDialogVisible.value = false },
        confirmButton = {
            Button(onClick = { isDialogVisible.value = false }) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer
    )
}

