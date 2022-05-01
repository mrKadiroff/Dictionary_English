package com.example.my_dictionary.retrofit

import com.example.my_dictionary.models.Dictionary
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("{word}")
   suspend fun getUsers(@Path("word") word:String): List<Dictionary>




}