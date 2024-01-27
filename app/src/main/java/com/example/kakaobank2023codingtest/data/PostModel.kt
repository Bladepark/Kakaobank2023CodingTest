package com.example.kakaobank2023codingtest.data

import android.net.Uri


data class PostModel(
    val thumbnailUrl : Uri,
    val siteName : String,
    val postedTime : String,
    var isFavorite : Boolean = false
)
