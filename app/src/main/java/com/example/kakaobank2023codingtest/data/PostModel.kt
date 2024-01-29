package com.example.kakaobank2023codingtest.data

import android.net.Uri
import java.util.UUID


data class PostModel(
    val id: String = UUID.randomUUID().toString(),
    val thumbnailUrl: Uri,
    val siteName: String,
    val postedTime: String,
    var isFavorite: Boolean = false
)
