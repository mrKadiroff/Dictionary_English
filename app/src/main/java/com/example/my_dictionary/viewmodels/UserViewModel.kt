package com.example.my_dictionary.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_dictionary.models.Dictionary
import com.example.my_dictionary.retrofit.ApiService
import com.example.my_dictionary.retrofit.RetrofitClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.StringBuilder

class UserViewModel : ViewModel() {

    private val liveData = MutableLiveData<Resource<List<Dictionary>>>()

    fun getWord(word:String): LiveData<Resource<List<Dictionary>>> {
        val apiService1 = RetrofitClient.apiService()

        viewModelScope.launch {
            liveData.postValue(Resource.loading(null))

            try {
                coroutineScope {

                    val async1 = async { apiService1.getUsers(word) }

                    val await1 = async1.await()

                    liveData.postValue(Resource.success(await1))

                }
            }catch (e:Exception){
                liveData.postValue(Resource.error(e.message ?: "Error",null))
            }



        }
        return liveData
    }

}