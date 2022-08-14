package com.jitzimoto.api

import com.jitzimoto.dataModels.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface API {
//    @POST("emailcheck.php")
//    suspend fun postit(@Body post: Array<String>): emailDataClass
    //maybe remobe the
//    /v3/2b28c331-0a44-4c0c-a7a1-6e1368961924
//@GET("emailcheck.php")

    @GET("/v3/2b28c331-0a44-4c0c-a7a1-6e1368961924")
    suspend fun checkHome(): Response<ResponseBody>
    //not sure about the data type

    @POST("emailcheck.php")
    suspend fun emailcheck(@Body post: String): Response<ResponseBody>

    @POST("signupapi.php")
    suspend fun signupPost(@Body post: Array<String>): Response<ResponseBody>

    @POST("verifyapi.php")
    suspend fun verifyPost(@Body post: String): Response<ResponseBody>

//    @POST("loginapi.php")
//    suspend fun loginPost(@Body post: Array<String>): Response<ResponseBody>

    @POST("loginapi.php")
    suspend fun loginPost(@Body post: Array<String>): Response<Logindetails>

    @POST("authenticateapi.php")
    suspend fun loginAuthenticate(@Body post: Array<String>): Response<ResponseBody>

    @POST("listingapi.php")
    suspend fun listingSearch(@Body post: Array<String>): myServiceModelApiDataClass

    @POST("createserviceapi.php")
    suspend fun createServiceApi(@Body post: Array<String>): Response<ResponseBody>

//    myservicesapi.php
//    Response<ResponseBody>
    @POST("myservicesapi.php")
    suspend fun myServicesApi(@Body post: String): myServiceModelApiDataClass

    @POST("editmyservicesapi.php")
    suspend fun editServiceApi(@Body post: Array<String>): Response<ResponseBody>

    @POST("deletemyservicesapi.php")
    suspend fun deleteServiceApi(@Body post: String): Response<ResponseBody>

    @POST("bizlistingapi.php")
    suspend fun bizListingSearch(@Body post: Array<String>): myBizListingModel

    @POST("bookingapi.php")
    suspend fun bookingApi(@Body post: Array<String>): Response<ResponseBody>

    @POST("mybookingsapi.php")
    suspend fun myBookingsApi(@Body post: String): Response<mybookingModel>

    @POST("personalbookingsapi.php")
    suspend fun personalBookingsApi(@Body post: String): Response<mybookingModel>

    @POST("editbookingapi.php")
    suspend fun editBookingsApi(@Body post: Array<String>): Response<ResponseBody>
//deletebookingapi

    @POST("deletebookingapi.php")
    suspend fun deleteBookingsApi(@Body post: Array<String>): Response<ResponseBody>

}