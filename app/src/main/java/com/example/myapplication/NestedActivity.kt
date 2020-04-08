package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import java.util.*

class NestedActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    val guid = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nested)
        Log.e("DEBUG", "${this}.onCreate")
        val testViewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        Log.e("DEBUG", "${this}.onCreate>> got view model: $testViewModel")

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        tabLayout.addOnTabSelectedListener(this)
        ViewModelCache.cacheNested(testViewModel)
        tabLayout.selectTab(tabLayout.getTabAt(0))
    }

    override fun onPause() {
        super.onPause()
        Log.e("DEBUG", "${this}.onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.e("DEBUG", "${this}.onResume")
        val testViewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        Log.e(
            "DEBUG",
            "${this}.onResume>> fetching activity scoped view model again: $testViewModel"
        )

        val apptestViewModel =
            ViewModelProvider(TestApplication.APP_INSTANCE).get(TestViewModel::class.java)
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
        return "NestedActivity(guid='${guid.subSequence(0, 7)}')"
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        tab?.position?.let {
            when (it) {
                0 -> {
                    val txn = supportFragmentManager.beginTransaction()
                    txn.replace(R.id.frag_root, TestFragment())
                    txn.commit()
                }
                1 -> {
                    val txn = supportFragmentManager.beginTransaction()
                    txn.replace(R.id.frag_root, TestFragment().apply {
                        is1 = false
                    })
                    txn.commit()
                }
                else -> {

                }
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.position?.let {
            when (it) {
                0 -> {
                    val txn = supportFragmentManager.beginTransaction()
                    txn.replace(R.id.frag_root, TestFragment())
                    txn.commit()
                }
                1 -> {
                    val txn = supportFragmentManager.beginTransaction()
                    txn.replace(R.id.frag_root, TestFragment().apply {
                        is1 = false
                    })
                    txn.commit()
                }
                else -> {

                }
            }
        }
    }

    class TestFragment : Fragment() {
        val guid = UUID.randomUUID().toString()
        var is1 = true

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val testViewModel = ViewModelProvider(this).get(TestViewModel::class.java)
            Log.e("DEBUG", "${this}.onCreateView>> got view model: $testViewModel")
            return if (is1) {
                inflater.inflate(R.layout.tab_1, container, false)
            } else {
                inflater.inflate(R.layout.tab_2, container, false)
            }
        }

        override fun onResume() {
            super.onResume()
            val testViewModel = ViewModelProvider(this).get(TestViewModel::class.java)
            Log.e("DEBUG", "${this}.onResume>> got view model: $testViewModel")
        }

        override fun onDestroy() {
            super.onDestroy()
            Log.e("DEBUG", "${this}.onDestroy")
        }

        override fun onPause() {
            super.onPause()
            Log.e("DEBUG", "${this}.onPause")
        }

        override fun onStop() {
            super.onStop()
            Log.e("DEBUG", "${this}.onStop")
        }

        override fun onStart() {
            super.onStart()
            Log.e("DEBUG", "${this}.onStart")
        }

        override fun toString(): String {
            return "TestFragment(guid='$guid')"
        }
    }
}
