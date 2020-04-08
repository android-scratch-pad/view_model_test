package com.example.myapplication

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

class TestViewModel : ViewModel() {
    val guid = UUID.randomUUID().toString()

    override fun toString(): String {
        return "TestViewModel(guid='${guid.subSequence(0, 7)}')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TestViewModel

        if (guid != other.guid) return false

        return true
    }

    override fun hashCode(): Int {
        return guid.hashCode()
    }

    override fun onCleared() {
        super.onCleared()
        Log.e("DEBUG", "${this} was cleared")
    }
}