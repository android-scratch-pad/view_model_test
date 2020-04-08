package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import java.util.*

class MainActivity : AppCompatActivity() {
    val guid = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button).setOnClickListener {
            startActivity(Intent(this, NestedActivity::class.java))
        }
        Log.e("DEBUG", "${this}.onCreate")
        val testViewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        Log.e("DEBUG", "${this}.onCreate>> got view model: $testViewModel")
        ViewModelCache.cacheMain(testViewModel)
    }

    override fun onPause() {
        super.onPause()
        Log.e("DEBUG", "${this}.onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.e("DEBUG", "${this}.onResume")
//        Handler().postDelayed({
//            ViewModelCache.nestedViewModelCache.entries.forEach {
//                Log.e("DEBUG", "Dumping nested ${it.key} ref: ${it.value.get()}")
//            }
//        }, 2000)
        val apptestViewModel = ViewModelProvider(TestApplication.APP_INSTANCE).get(TestViewModel::class.java)
        Log.e("DEBUG", "${this}.onResume>> get app scoped view model $apptestViewModel")
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

    override fun toString(): String {
        return "MainActivity(guid='${guid.subSequence(0, 7)}')"
    }
}
