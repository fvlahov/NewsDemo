package hr.vlahov.newsdemo.utils

import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsSource
import hr.vlahov.domain.models.profile.Profile
import java.util.Date
import java.util.UUID

val randomId
    get() = UUID.randomUUID().toString()

val dummyProfiles = listOf(
    Profile("Goran"),
    Profile("Ante"),
    Profile("Franko"),
)

val dummyNewsSources = listOf(
    NewsSource(
        "id1",
        name = "ABC NEWS",
        description = "Some description",
        url = "",
        country = "us"
    ),
    NewsSource(
        "id2",
        name = "BBC NEWS",
        description = "Some description",
        url = "",
        country = "uk"
    ),
    NewsSource(
        "id3",
        name = "NBC NEWS",
        description = "Some description",
        url = "",
        country = "us"
    ),
)

val dummyNewsArticles = listOf(
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = true,
        publishedAt = Date().time
    ),
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = false,
        publishedAt = Date().time

    ),
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = false,
        publishedAt = Date().time

    ),
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = false,
        publishedAt = Date().time

    ),
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = false,
        publishedAt = Date().time

    ),
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = false,
        publishedAt = Date().time

    )
)