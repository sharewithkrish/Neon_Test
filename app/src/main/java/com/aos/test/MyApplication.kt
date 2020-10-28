package com.aos.test

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.aos.test.utilities.Utility

class MyApplication : Application(), Application.ActivityLifecycleCallbacks {

    override fun onCreate() {
        super.onCreate()
        myApplication = this@MyApplication
        registerActivityLifecycleCallbacks(this)
    }
    override fun onTerminate() {
        super.onTerminate()
        myApplication = null
        Log.d("myapplication","terminate")
    }

    companion object {
        var myApplication: MyApplication? = null
        fun getInstance(): MyApplication? {
            return myApplication
        }
    }

    override fun onActivityPaused(activity: Activity?) {
        Log.d("myapplication","pause")
    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {
        Utility.hideKeyboard(activity!!)

    }

    override fun onActivityDestroyed(activity: Activity?) {
        Utility.hideKeyboard(activity!!)
        Log.d("myapplication","destroy")
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {
        Log.d("myapplication","stop")
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }
}