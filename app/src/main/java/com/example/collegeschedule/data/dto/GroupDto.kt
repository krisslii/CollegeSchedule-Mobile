package com.example.collegeschedule.data.dto

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("groupId")
    val groupId: Int,

    @SerializedName("groupName")
    val groupName: String,

    @SerializedName("course")
    val course: Int,

    @SerializedName("specialtyId")
    val specialtyId: Int,

    @SerializedName("specialtyName")
    val specialtyName: String?
)