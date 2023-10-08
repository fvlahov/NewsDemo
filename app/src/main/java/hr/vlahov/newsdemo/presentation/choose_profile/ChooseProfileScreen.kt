package hr.vlahov.newsdemo.presentation.choose_profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hr.vlahov.domain.models.profile.Profile
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.ui.theme.NewsDemoTheme
import hr.vlahov.newsdemo.utils.dummyProfiles

@Composable
fun ChooseProfileScreen(
    viewModel: ChooseProfileViewModel = hiltViewModel(),
) {
    val profiles = viewModel.allProfiles.collectAsStateWithLifecycle(emptyList()).value
    ChooseProfileBody(
        profiles = profiles,
        onProfileClicked = { viewModel.selectProfile(it.name) },
        onNewProfileClicked = viewModel::createNewProfile
    )
}

@Composable
private fun ChooseProfileBody(
    profiles: List<Profile>,
    onProfileClicked: (Profile) -> Unit,
    onNewProfileClicked: () -> Unit,
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .semantics { contentDescription = "Choose profile" }) {
        LazyColumn(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(60.dp, 0.dp)
        ) {
            items(profiles, key = { it.name }) { profile ->
                ProfileButton(
                    profile = profile,
                    onClick = { onProfileClicked(profile) },
                    modifier = Modifier.padding(0.dp, 4.dp)
                )
            }
            item {
                CreateProfileButton(
                    onClick = onNewProfileClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 24.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileButton(
    profile: Profile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp, 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = profile.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = R.drawable.ic_play_arrow),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun CreateProfileButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onClick,
        modifier = modifier
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.new_user),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
@Preview
private fun ChooseProfileScreenPreview() {
    NewsDemoTheme {
        ChooseProfileBody(
            profiles = dummyProfiles,
            onProfileClicked = {},
            onNewProfileClicked = {}
        )
    }
}