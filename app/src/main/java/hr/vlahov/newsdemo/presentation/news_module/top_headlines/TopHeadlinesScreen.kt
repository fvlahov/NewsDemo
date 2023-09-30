package hr.vlahov.newsdemo.presentation.news_module.top_headlines

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.newsdemo.ui.theme.NewsDemoTheme
import hr.vlahov.newsdemo.utils.dummyNewsArticles

@Composable
fun TopHeadlinesScreen(
    viewModel: TopHeadlinesViewModel = hiltViewModel(),
) {
    val topHeadlines = viewModel.topHeadlines.collectAsStateWithLifecycle().value
    TopHeadlinesBody(
        items = topHeadlines,
        onItemClick = { viewModel.navigateToSingleNewsArticle(it) }
    )
}

@Composable
private fun TopHeadlinesBody(
    items: List<NewsArticle>,
    onItemClick: (NewsArticle) -> Unit,
) {
    LazyColumn() {
        items(items, key = { it.title }) { newsArticle ->
            NewsArticleItem(
                newsArticle = newsArticle,
                onItemClick = { onItemClick(newsArticle) },
                modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp)
            )
        }
    }
}

@Composable
private fun NewsArticleItem(
    newsArticle: NewsArticle,
    onItemClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onItemClick,
        colors = CardDefaults.cardColors(),
        modifier = modifier
    ) {
        AsyncImage(
            model = newsArticle.imageUrl,
            contentDescription = newsArticle.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Text(
            text = newsArticle.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Text(
            text = newsArticle.description.orEmpty(),
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
    }
}

@Composable
@Preview
private fun TopHeadlinesPreview() {
    NewsDemoTheme {
        TopHeadlinesBody(items = dummyNewsArticles, onItemClick = {})
    }
}