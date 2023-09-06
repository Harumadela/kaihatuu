package com.example.kaihatu

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Data: RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var name: String? = null
    var win_black: Int = 0
    var match_black: Int = 0
    var accuracy: Int = 0
    var win_high_low: Int = 0
    var match_high_low: Int = 0
    var win_osero_black: Int = 0
    var win_osero_white: Int = 0
}