package hr.vlahov.newsdemo.utils

import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.profile.Profile
import java.util.UUID

val randomId
    get() = UUID.randomUUID().toString()

val dummyProfiles = listOf(
    Profile("Goran"),
    Profile("Ante"),
    Profile("Franko"),
)

val dummyNewsArticles = listOf(
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = true
    ),
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = false
    ),
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = false
    ),
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = false
    ),
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = false
    ),
    NewsArticle(
        author = "Ante Antić",
        title = "Something shocking happened, be afraid!",
        description = "A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles A short description of the shocking titles",
        content = "A somewhat longer content of the description with a lot of 'wait for its'",
        originalArticleUrl = randomId,
        imageUrl = null,
        isLiked = false
    )
)