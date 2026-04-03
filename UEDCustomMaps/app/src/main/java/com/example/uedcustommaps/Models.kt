package com.example.uedcustommaps

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Định nghĩa một địa điểm trên bản đồ
@Parcelize
data class Place(
    val title: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
) : Parcelable

// Định nghĩa một bản đồ chứa danh sách các địa điểm
@Parcelize
data class UserMap(
    val title: String,
    val places: List<Place>
) : Parcelable