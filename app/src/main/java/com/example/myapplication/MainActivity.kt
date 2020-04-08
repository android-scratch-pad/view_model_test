package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import java.util.*

class MainActivity : TestActivity() {
    val guid = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, NestedActivity::class.java))
        }
        val testViewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        Log.e("DEBUG", "${this}.onCreate>> got view model: $testViewModel")
        ViewModelCache.cacheMain(testViewModel)
    }

    override fun onResume() {
        super.onResume()
        val apptestViewModel =
            ViewModelProvider(TestApplication.APP_INSTANCE).get(TestViewModel::class.java)
        Log.e("DEBUG", "${this}.onResume>> get app scoped view model $apptestViewModel")
    }

    override fun toString(): String {
        return "MainActivity(guid='${guid.subSequence(0, 7)}')"
    }
}
