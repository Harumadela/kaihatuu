package com.example.kaihatu

data class MyItem(
    val id: Int,
    val title: String,
    val detail: String,
    var isExpanded: Boolean = false
)