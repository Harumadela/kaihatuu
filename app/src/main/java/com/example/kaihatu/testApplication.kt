package com.example.kaihatu

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class testApplication: Application() {
    override fun onCreate(){
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .allowWritesOnUiThread(true).build()
        Realm.setDefaultConfiguration(config)
    }
}