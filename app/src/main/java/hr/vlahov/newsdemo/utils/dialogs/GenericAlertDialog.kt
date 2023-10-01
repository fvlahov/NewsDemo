package hr.vlahov.newsdemo.utils.dialogs

import androidx.annotation.DrawableRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import hr.vlahov.newsdemo.R

@Composable
fun GenericAlertDialog(
    dialogState: MutableState<Boolean>,
    text: String?,
    title: String? = null,
    @DrawableRes iconRes: Int? = null,
    confirmButtonText: String = stringResource(id = R.string.yes),
    onConfirmButtonClicked: () -> Unit = {},
    dismissButtonText: String? = null,
    onDismissButtonClicked: () -> Unit = {},
    isCancelable: Boolean = true,
) {
    if (dialogState.value)
        AlertDialog(
            text = {
                text?.let { Text(text = it) }
            },
            title = {
                title?.let { Text(text = it) }
            },
            onDismissRequest = {
                if (isCancelable)
                    dialogState.value = false
            },
            confirmButton = {
                Button(onClick = {
                    dialogState.value = false
                    onConfirmButtonClicked()
                }) {
                    Text(text = confirmButtonText)
                }
            },
            dismissButton = {
                dismissButtonText?.let {
                    Button(onClick = {
                        dialogState.value = false
                        onDismissButtonClicked()
                    }) {
                        Text(text = dismissButtonText)
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            icon = {
                iconRes?.let {
                    Icon(painter = painterResource(id = iconRes), contentDescription = null)
                }
            }
        )
}