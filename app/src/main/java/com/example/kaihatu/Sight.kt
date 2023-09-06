package com.example.kaihatu

import android.content.res.Resources
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.BufferedReader
import java.io.InputStreamReader

@Serializable
class Sight (
    val name: String,
    val imageName: String,
    val detail: String
)

fun getSights(resources: Resources): List<Sight>{
    val assetManager = resources.assets
    val inputStream = assetManager.open("sight.json")
    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
    val str: String = bufferedReader.readText()
    val obj = Json(JsonConfiguration.Stable).parse(Sight.serializer().list,str)
    return obj
}

