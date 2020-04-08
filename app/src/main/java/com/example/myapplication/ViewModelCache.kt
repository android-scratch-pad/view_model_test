package com.example.myapplication

import java.lang.ref.WeakReference

object ViewModelCache {
    val mainViewModelCache = mutableMapOf<String, WeakReference<TestViewModel>>()
    val nestedViewModelCache = mutableMapOf<String, WeakReference<TestViewModel>>()

    fun cacheNested(testViewModel: TestViewModel) {
        cacheNested(testViewModel, nestedViewModelCache)
    }

    fun cacheMain(testViewModel: TestViewModel) {
        cacheNested(testViewModel, mainViewModelCache)
    }

    private fun cacheNested(
        testViewModel: TestViewModel,
        viewModelCache: MutableMap<String, WeakReference<TestViewModel>>
    ) {
//        val currentWeakRef = viewModelCache[testViewModel.guid]
//        if (currentWeakRef == null) {
//            viewModelCache[testViewModel.guid] = WeakReference<TestViewModel>(testViewModel)
//        } else {
//            if (currentWeakRef.get() != testViewModel) {
//                viewModelCache[testViewModel.guid] = WeakReference<TestViewModel>(testViewModel)
//            }
//        }
    }
}