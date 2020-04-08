package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("DEBUG", "${this}.onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.e("DEBUG", "${this}.onResume, view model store: ${this.viewModelStore}")
    }

    override fun onPause() {
        super.onPause()
        Log.e("DEBUG", "${this}.onPause")
    }

    override fun onStart() {
        super.onStart()
        Log.e("DEBUG", "${this}.onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.e("DEBUG", "${this}.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("DEBUG", "${this}.onDestroy")
    }
}