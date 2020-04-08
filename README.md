# View Model Test

This test app adds logs to test how view models work, and their scopes.

## Activity scope

View models obtained by passing in an activity scope to ViewModelProvider: 

`ViewModelProvider(this).get(TestViewModel::class.java)`

have an interesting property, in that they can 'persist' past an activity's lifecycle, **in the case that the activity is destroyed due to a configuration change** ([https://developer.android.com/guide/topics/resources/runtime-changes](https://developer.android.com/guide/topics/resources/runtime-changes)), such as rotation.

In this case, the viewmodel returned will be the **same** viewmodel: 

> MainActivity(guid='acb50a1').onCreate  
> MainActivity(guid='acb50a1').onCreate>> got view model: TestViewModel(guid='**efa412c**')  
> MainActivity(guid='acb50a1').onStart  
> MainActivity(guid='acb50a1').onResume, view model store: androidx.lifecycle.ViewModelStore@46092f  
> MainActivity(guid='acb50a1').onResume>> get app scoped view model TestViewModel(guid='847f565')  
> MainActivity(guid='acb50a1').onPause  
> MainActivity(guid='acb50a1').onStop  
> MainActivity(guid='acb50a1').onDestroy  
> MainActivity(guid='e4c49e8').onCreate  
> MainActivity(guid='e4c49e8').onCreate>> got view model: TestViewModel(guid='**efa412c**')  
> MainActivity(guid='e4c49e8').onStart  
> MainActivity(guid='e4c49e8').onResume, view model store: androidx.lifecycle.ViewModelStore@46092f  
> MainActivity(guid='e4c49e8').onResume>> get app scoped view model TestViewModel(guid='847f565')  

A view model with guid **efa412c** is returned. As you can see, the reason this works is that the `ViewModelStore` instance is the same, even though the activity is a completely different instance. Interested readers should dig into the Android source code to see how this works under the hood.

However, if an activity is destroyed 'normally' (by pressing back for instance), then the next time the activity is started, the viewmodel returned is different:

> MainActivity(guid='b0bbefe').onPause  
> NestedActivity(guid='e8f2c78').onCreate  
> NestedActivity(guid='e8f2c78').onCreate>> got view model: TestViewModel(guid='**f3b5959**')  
> TestFragment(guid='03219844').onCreateView>> got view model: TestViewModel(guid='9fa6cbe')  
> TestFragment(guid='03219844').onStart  
> NestedActivity(guid='e8f2c78').onStart  
> NestedActivity(guid='e8f2c78').onResume, view model store: androidx.lifecycle.ViewModelStore@**28a2e12**  
> NestedActivity(guid='e8f2c78').onResume>> fetching activity scoped view model again: TestViewModel(guid='**f3b5959**')  
> NestedActivity(guid='e8f2c78').onResume>> get app scoped view model TestViewModel(guid='847f565')  
> TestFragment(guid='03219844').onResume>> got view model: TestViewModel(guid='9fa6cbe')  
> MainActivity(guid='b0bbefe').onStop  
> TestFragment(guid='03219844').onPause  
> NestedActivity(guid='e8f2c78').onPause  
> MainActivity(guid='b0bbefe').onStart  
> MainActivity(guid='b0bbefe').onResume, view model store: androidx.lifecycle.ViewModelStore@46092f  
> MainActivity(guid='b0bbefe').onResume>> get app scoped view model TestViewModel(guid='847f565')  
> TestFragment(guid='03219844').onStop  
> NestedActivity(guid='e8f2c78').onStop  
> TestViewModel(guid='f3b5959') was cleared  
> TestViewModel(guid='9fa6cbe') was cleared  
> TestFragment(guid='03219844').onDestroy  
> NestedActivity(guid='e8f2c78').onDestroy  
> MainActivity(guid='b0bbefe').onPause  
> NestedActivity(guid='6b8868d').onCreate  
> NestedActivity(guid='6b8868d').onCreate>> got view model: TestViewModel(guid='**55f07e5**')  
> TestFragment(guid='862a7bef').onCreateView>> got view model: TestViewModel(guid='b1759f3')  
> TestFragment(guid='862a7bef').onStart  
> NestedActivity(guid='6b8868d').onStart  
> NestedActivity(guid='6b8868d').onResume, view model store: androidx.lifecycle.ViewModelStore@**cc2397e**  
> NestedActivity(guid='6b8868d').onResume>> fetching activity scoped view model again: TestViewModel(guid='**55f07e5**')  
> NestedActivity(guid='6b8868d').onResume>> get app scoped view model TestViewModel(guid='847f565')  
> TestFragment(guid='862a7bef').onResume>> got view model: TestViewModel(guid='b1759f3')  
> MainActivity(guid='b0bbefe').onStop  

As you can see, `NestedActivity` starts with a `ViewModelStore` with id **28a2e12**. It is then finished normally, then started again by a UI interaction (pressing a button). The new instance has a `ViewModelStore` id of **cc2397e**. The associated `TestViewModel` instance has a different guid from the previous one, as we'd expect.

In testing, it seems that the previous activity's scoped viewmodels are *not* persisted in memory, for the case that the activity was destroyed normally. Running the memory profiler, it seems there are no references to the GC root, so they are eligible to be GC'ed.

Also notice the viewmodel clear methods are called:

> TestViewModel(guid='f3b5959') was cleared


## Fragment scope

Fragment scoped viewmodels work in a similar fashion. The test app *replaces* fragment instances. This means that we should expect that, as fragments are swapped out, that their associated viewmodels should *not* be preserved. The logs below represent the following UI actions:

1. Start nested activity
2. Tap on **Tab 2**
3. Tap on **Tab 1**

> MainActivity(guid='b0bbefe').onPause  
> NestedActivity(guid='2dbce0b').onCreate  
> NestedActivity(guid='2dbce0b').onCreate>> got view model: TestViewModel(guid='de910de')  
> TestFragment(guid='c7ba42b9').onCreateView>> got view model: TestViewModel(guid='0a0147b')  
> TestFragment(guid='c7ba42b9').onStart  
> NestedActivity(guid='2dbce0b').onStart  
> NestedActivity(guid='2dbce0b').onResume, view model store: androidx.lifecycle.ViewModelStore@2b66c77  
> NestedActivity(guid='2dbce0b').onResume>> fetching activity scoped view model again: TestViewModel(guid='de910de')  
> NestedActivity(guid='2dbce0b').onResume>> get app scoped view model TestViewModel(guid='847f565')  
> TestFragment(guid='c7ba42b9').onResume, view model store: androidx.lifecycle.ViewModelStore@240142c
> TestFragment(guid='c7ba42b9').onResume>> got view model: TestViewModel(guid='0a0147b')  
> MainActivity(guid='b0bbefe').onStop  
> TestFragment(guid='c7ba42b9').onPause  
> TestFragment(guid='c7ba42b9').onStop  
> TestViewModel(guid='0a0147b') was cleared  
> TestFragment(guid='c7ba42b9').onDestroy  
> TestFragment(guid='be377bcc').onCreateView>> got view model: TestViewModel(guid='5e649f8')  
> TestFragment(guid='be377bcc').onStart  
> TestFragment(guid='c7ba42b9').onResume, view model store: androidx.lifecycle.ViewModelStore@138239f  
> TestFragment(guid='be377bcc').onResume>> got view model: TestViewModel(guid='5e649f8')  
> TestFragment(guid='be377bcc').onPause  
> TestFragment(guid='be377bcc').onStop  
> TestViewModel(guid='5e649f8') was cleared  
> TestFragment(guid='be377bcc').onDestroy  
> TestFragment(guid='34f82550').onCreateView>> got view model: TestViewModel(guid='f6b7497')  
> TestFragment(guid='34f82550').onStart  
> TestFragment(guid='c7ba42b9').onResume, view model store: androidx.lifecycle.ViewModelStore@ebbdf52  
> TestFragment(guid='34f82550').onResume>> got view model: TestViewModel(guid='f6b7497')  

As you can see, the viewmodels returned are all different from each other, and the viewmodel stores are different.

One thing to note is what happens when you rotate an activity with fragments. The observed behavior is:

* Activity view model store is the same
* Fragments are simply recreated, so have *different* view model stores

That is to say, unless explicitly configured otherwise, there is no automagical saving of fragment viewmodel stores during configuration changes:

> MainActivity(guid='bb8f600').onPause  
> NestedActivity(guid='cf13c62').onCreate  
> NestedActivity(guid='cf13c62').onCreate>> got view model: TestViewModel(guid='7774183')  
> TestFragment(guid='541c533').onCreateView>> got view model: TestViewModel(guid='d45c64e')  
> TestFragment(guid='541c533').onStart  
> NestedActivity(guid='cf13c62').onStart  
> **NestedActivity(guid='cf13c62').onResume, view model store: androidx.lifecycle.ViewModelStore@dc24839**  
> NestedActivity(guid='cf13c62').onResume>> fetching activity scoped view model again: TestViewModel(guid='7774183')  
> NestedActivity(guid='cf13c62').onResume>> get app scoped view model TestViewModel(guid='3ee823c')  
> **TestFragment(guid='541c533').onResume, view model store: androidx.lifecycle.ViewModelStore@240142c**  
> TestFragment(guid='541c533').onResume>> got view model: TestViewModel(guid='d45c64e')  
> MainActivity(guid='bb8f600').onStop  
> TestFragment(guid='541c533').onPause  
> NestedActivity(guid='cf13c62').onPause  
> TestFragment(guid='541c533').onStop  
> NestedActivity(guid='cf13c62').onStop  
> TestFragment(guid='541c533').onDestroy  
> NestedActivity(guid='cf13c62').onDestroy  
> NestedActivity(guid='25cef1a').onCreate  
> NestedActivity(guid='25cef1a').onCreate>> got view model: TestViewModel(guid='7774183')  
> TestFragment(guid='1f5adfe').onCreateView>> got view model: TestViewModel(guid='d45c64e')  
> TestViewModel(guid='d45c64e') was cleared  
> TestFragment(guid='1f5adfe').onDestroy  
> TestFragment(guid='6723a66').onCreateView>> got view model: TestViewModel(guid='5c50f02')  
> TestFragment(guid='6723a66').onStart  
> NestedActivity(guid='25cef1a').onStart  
> **NestedActivity(guid='25cef1a').onResume, view model store: androidx.lifecycle.ViewModelStore@dc24839**  
> NestedActivity(guid='25cef1a').onResume>> fetching activity scoped view model again: TestViewModel(guid='7774183')  
> NestedActivity(guid='25cef1a').onResume>> get app scoped view model TestViewModel(guid='3ee823c')  
> **TestFragment(guid='6723a66').onResume, view model store: androidx.lifecycle.ViewModelStore@5e1246f**  
> TestFragment(guid='6723a66').onResume>> got view model: TestViewModel(guid='5c50f02')  

`NestedActivity`'s viewmodel store is 'persisted', however `TestFragment`'s viewmodel store is not. This results in the activity obtaining the same viewmodel, and the fragment obtaining a completely new one.