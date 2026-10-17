package com.mohitkumar.kmpdemo.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class PostsApi(private val client: HttpClient = defaultClient()) {

    suspend fun fetchPosts(): List<Post> =
        client.get("$BASE_URL/posts").body()

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com"

        fun defaultClient(): HttpClient = HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
}
