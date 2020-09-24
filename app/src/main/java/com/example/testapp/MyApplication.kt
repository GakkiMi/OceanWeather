package com.example.testapp

import android.app.Application
import android.content.Context

/**
 * 文 件 名：MyApplication
 * 描   述：TODO
 */
class MyApplication: Application() {

    companion object{
        const val TOKEN="0Id8aRHvCNACQeNF"
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }

}