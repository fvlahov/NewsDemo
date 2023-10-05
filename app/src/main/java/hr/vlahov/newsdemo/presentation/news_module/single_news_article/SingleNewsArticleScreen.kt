package hr.vlahov.newsdemo.presentation.news_module.single_news_article

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.presentation.news_module.shared.StatefulAsyncImage
import hr.vlahov.newsdemo.ui.theme.NewsDemoTheme
import hr.vlahov.newsdemo.utils.buildAsyncImageModel
import hr.vlahov.newsdemo.utils.dummyNewsArticles
import hr.vlahov.newsdemo.utils.getFormattedDate

@Composable
fun SingleNewsArticleScreen(
    viewModel: SingleNewsArticleViewModel = hiltViewModel(),
) {
    val newsArticle = viewModel.newsArticle.collectAsStateWithLifecycle().value

    val uriHandler = LocalUriHandler.current

    SingleNewsArticleBody(
        newsArticle = newsArticle,
        onNavigateBack = viewModel::navigateBack,
        onToggleLikeNewsArticle = viewModel::toggleNewsArticleLiked,
        onReadFullArticle = { uriHandler.openUri(it) }
    )
}

@Composable
private fun SingleNewsArticleBody(
    newsArticle: NewsArticle?,
    onNavigateBack: () -> Unit,
    onToggleLikeNewsArticle: (isLiked: Boolean) -> Unit,
    onReadFullArticle: (url: String) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            )
        },
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (newsArticle == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {

                val isLiked = rememberSaveable { mutableStateOf(newsArticle.isLiked) }

                Column {
                    StatefulAsyncImage(
                        model = buildAsyncImageModel(
                            data = newsArticle.imageUrl,
                        ),
                        contentDescription = newsArticle.title,
                        modifier = Modifier
                            .heightIn(max = 200.dp)
                            .fillMaxWidth()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 16.dp, 16.dp, 0.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = newsArticle.getFormattedDate(LocalContext.current))
                            Spacer(modifier = Modifier.weight(1f))
                            newsArticle.author?.let { author ->
                                Text(
                                    text = stringResource(
                                        id = R.string.by_val,
                                        author
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = newsArticle.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        newsArticle.description?.let { description ->
                            Text(text = description)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        newsArticle.content?.let { content ->
                            Text(text = content)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    isLiked.value = isLiked.value.not()
                                    onToggleLikeNewsArticle(isLiked.value)
                                },
                                modifier = Modifier.padding(8.dp),
                            ) {
                                AnimatedVisibility(isLiked.value) {
                                    Icon(
                                        imageVector = Icons.Default.Favorite,
                                        contentDescription = "Liked article",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                AnimatedVisibility(isLiked.value.not()) {
                                    Icon(
                                        imageVector = Icons.Default.FavoriteBorder,
                                        contentDescription = "Not liked article",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            TextButton(onClick = { onReadFullArticle(newsArticle.originalArticleUrl) }) {
                                Text(
                                    text = stringResource(id = R.string.read_full_article),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun SingleNewsArticleScreenPreview() {
    NewsDemoTheme {
        SingleNewsArticleBody(
            newsArticle = dummyNewsArticles.first(),
            onNavigateBack = { },
            onToggleLikeNewsArticle = { },
            onReadFullArticle = { }
        )
    }
}