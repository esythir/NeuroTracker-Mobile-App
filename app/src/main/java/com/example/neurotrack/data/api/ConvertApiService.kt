package com.example.neurotrack.data.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ConvertApiService {
    @Multipart
    @POST("convert")
    suspend fun convertFile(
        @Query("Secret") apiSecret: String,
        @Query("ApiKey") apiKey: String,
        @Query("FromFormat") fromFormat: String,
        @Query("ToFormat") toFormat: String,
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>
} 