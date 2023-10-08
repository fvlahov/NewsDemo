package hr.vlahov.newsdemo.presentation.create_profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.base.errors.BaseError
import hr.vlahov.newsdemo.ui.theme.NewsDemoTheme
import hr.vlahov.newsdemo.utils.ErrorHandler

@Composable
fun CreateProfileScreen(
    viewModel: CreateProfileViewModel = hiltViewModel(),
) {
    val error = viewModel.errors.collectAsStateWithLifecycle()
    CreateProfileBody(
        onConfirm = viewModel::confirm,
        error = error.value
    )
}

@Composable
private fun CreateProfileBody(
    onConfirm: (profileName: String) -> Unit,
    error: BaseError?,
) {
    ErrorHandler(error = error)
    Box(modifier = Modifier
        .fillMaxSize()
        .semantics { contentDescription = "Create profile" }) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(60.dp, 0.dp)
        ) {
            val profileNameState = rememberSaveable { mutableStateOf("") }
            OutlinedTextField(
                value = profileNameState.value,
                onValueChange = { profileNameState.value = it },
                placeholder = {
                    Text(text = stringResource(id = R.string.your_name))
                },
                trailingIcon = {
                    AnimatedVisibility(visible = profileNameState.value.isNotEmpty()) {
                        IconButton(onClick = { profileNameState.value = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear profile name"
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { onConfirm(profileNameState.value) },
                modifier = Modifier.fillMaxWidth()
            )
            {
                Text(
                    text = stringResource(id = R.string.next),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
@Preview
private fun ChooseProfileScreenPreview() {
    NewsDemoTheme {
        CreateProfileBody(
            onConfirm = { },
            error = null
        )
    }
}