package com.example.kakaobank2023codingtest.api

import com.example.kakaobank2023codingtest.data.ImageModel
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


private const val KAKAO_REST_API_KEY = "c6c54f0c6b9d6a75d418c5a16f0232b2"

interface KakaoAPI {
    @GET("image") //카카오 이미지
    suspend fun getSearchImages(
        @Header("Authorization")
        apiKey: String = "KakaoAK $KAKAO_REST_API_KEY",
        @Query("query")
        query: String, //검색을 원하는 질의어
    ) : ImageModel
}