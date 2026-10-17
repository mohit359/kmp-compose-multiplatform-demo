package com.mohitkumar.kmpdemo.data

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val title: String,
    val body: String,
)
