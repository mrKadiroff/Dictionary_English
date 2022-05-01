package com.example.my_dictionary.viewmodel2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MyEditData {

    private var liveData = MutableLiveData<String>()

    fun set(value: String) {
        liveData.value = value
    }

    fun get(): LiveData<String> {
        return liveData
    }

}