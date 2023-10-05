package hr.vlahov.newsdemo.utils

import android.content.Context
import android.text.format.DateFormat
import hr.vlahov.domain.models.news.NewsArticle
import java.util.Date

fun NewsArticle.getFormattedDate(context: Context): String =
    DateFormat.getDateFormat(context).format(Date(this.publishedAt))