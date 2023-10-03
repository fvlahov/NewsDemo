package hr.vlahov.newsdemo.presentation.news_module.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.size.Size
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.profile.Profile
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.ui.theme.NewsDemoTheme
import hr.vlahov.newsdemo.utils.buildAsyncImageModel
import hr.vlahov.newsdemo.utils.dialogs.GenericAlertDialog
import hr.vlahov.newsdemo.utils.dialogs.rememberDialogState
import hr.vlahov.newsdemo.utils.dummyNewsArticles
import hr.vlahov.newsdemo.utils.shimmerBackground

@Composable
fun ProfileScreen(
    viewModel: ProfileScreenViewModel = hiltViewModel(),
) {
    val profile = viewModel.profile.collectAsStateWithLifecycle().value
    val likedArticles = viewModel.likedArticles.collectAsStateWithLifecycle(null).value

    ProfileBody(
        profile = profile,
        likedArticles = likedArticles,
        onSeeAllLikedArticlesClicked = viewModel::navigateToAllLikedArticles,
        onLikedArticleClicked = viewModel::navigateToLikedArticle,
        onChangeUserClicked = viewModel::changeProfile
    )
}

@Composable
private fun ProfileBody(
    profile: Profile?,
    likedArticles: List<NewsArticle>?,
    onSeeAllLikedArticlesClicked: () -> Unit,
    onLikedArticleClicked: (NewsArticle) -> Unit,
    onChangeUserClicked: () -> Unit,
) {
    val areYouSureDialogState = rememberDialogState()

    GenericAlertDialog(
        dialogState = areYouSureDialogState,
        text = stringResource(id = R.string.are_you_sure),
        confirmButtonText = stringResource(id = R.string.yes),
        dismissButtonText = stringResource(id = R.string.no),
        onConfirmButtonClicked = onChangeUserClicked
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = profile?.name?.let { stringResource(id = R.string.hello_val, it) }.orEmpty(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .shimmerBackground(showShimmer = profile == null)
                    .weight(1f)

            )
            OutlinedButton(
                onClick = { areYouSureDialogState.value = true },
                border = BorderStroke(
                    1.dp, MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(8.dp, 0.dp, 0.dp, 0.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = stringResource(id = R.string.change_profile),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        LikedNewsArticles(
            likedArticles = likedArticles,
            onSeeAllLikedArticlesClicked = onSeeAllLikedArticlesClicked,
            onLikedArticleClicked = onLikedArticleClicked,
        )
    }
}

@Composable
private fun ColumnScope.LikedNewsArticles(
    likedArticles: List<NewsArticle>?,
    onSeeAllLikedArticlesClicked: () -> Unit,
    onLikedArticleClicked: (NewsArticle) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = stringResource(id = R.string.liked_articles))
        Spacer(modifier = Modifier.weight(1f))
        if (likedArticles.isNullOrEmpty().not())
            TextButton(onClick = onSeeAllLikedArticlesClicked) {
                Text(
                    text = stringResource(id = R.string.see_all),
                    color = MaterialTheme.colorScheme.primary
                )
            }
    }

    SkeletonLikedNewsArticles(
        isVisible = likedArticles == null,
    )

    if (likedArticles == null)
        return

    NoNewsArticles(
        isVisible = likedArticles.isEmpty()
    )

    val numberOfArticlesToShow = 4

    likedArticles.take(numberOfArticlesToShow).forEach { newsArticle ->
        Row(
            modifier = Modifier
                .heightIn(min = 70.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(4.dp))
                .clickable { onLikedArticleClicked(newsArticle) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            val titleHeight = rememberSaveable { mutableIntStateOf(0) }
            val imageHeight = with(LocalDensity.current) { titleHeight.intValue.toDp() + 16.dp }

            if (newsArticle.imageUrl != null)
                AsyncImage(
                    model = buildAsyncImageModel(
                        data = newsArticle.imageUrl,
                        size = Size(150, 100)
                    ),
                    contentDescription = newsArticle.title,
                    modifier = Modifier
                        .width(100.dp)
                        .heightIn(min = 70.dp)
                        .height(imageHeight),
                    contentScale = ContentScale.Crop
                )

            if (newsArticle.imageUrl == null)
                Icon(
                    painter = painterResource(id = R.drawable.ic_newspaper),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.width(100.dp)
                )

            Text(
                text = newsArticle.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(8.dp)
                    .onGloballyPositioned { titleHeight.intValue = it.size.height },
                overflow = TextOverflow.Ellipsis
            )

        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    if (likedArticles.size > numberOfArticlesToShow)
        Row(
            modifier = Modifier
                .heightIn(min = 70.dp)
                .fillMaxWidth()
                .shimmerBackground(
                    showShimmer = true,
                    shape = RoundedCornerShape(4.dp),
                    delayBetweenEachShimmerMillis = 1000
                )
                .clickable { onSeeAllLikedArticlesClicked() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.see_all_val, likedArticles.size.toString()),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
}

@Composable
private fun ColumnScope.SkeletonLikedNewsArticles(
    isVisible: Boolean,
    count: Int = 4,
) {
    AnimatedVisibility(isVisible) {
        Column {
            repeat(count) {
                Row(
                    modifier = Modifier
                        .padding(0.dp, 8.dp, 0.dp, 0.dp)
                        .height(70.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .fillMaxHeight()
                            .shimmerBackground(showShimmer = true, shape = RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmerBackground(showShimmer = true, shape = RoundedCornerShape(8.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.NoNewsArticles(
    isVisible: Boolean,
) {
    AnimatedVisibility(isVisible) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp, 0.dp, 0.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(id = R.string.no_liked_news_articles))
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
@Preview
private fun ProfileScreenPreview() {
    NewsDemoTheme {
        ProfileBody(
            profile = Profile(name = "Asurbanipal"),
            likedArticles = dummyNewsArticles,
            onSeeAllLikedArticlesClicked = { },
            onLikedArticleClicked = { },
            onChangeUserClicked = { }
        )
    }
}