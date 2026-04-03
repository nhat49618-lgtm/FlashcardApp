package com.example.uedcustommaps

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

class StorageManager(context: Context) {
    private val prefs = context.getSharedPreferences("ued_maps_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Lưu danh sách bản đồ
    fun saveMaps(maps: List<UserMap>) {
        val json = gson.toJson(maps)
        prefs.edit { putString("saved_maps", json) }
    }

    // Đọc danh sách bản đồ đã lưu
    fun loadMaps(): List<UserMap> {
        val json = prefs.getString("saved_maps", null) ?: return emptyList()
        val type = object : TypeToken<List<UserMap>>() {}.type
        return gson.fromJson(json, type)
    }
}