package com.mohitkumar.kmpdemo

import com.mohitkumar.kmpdemo.data.PostsApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class PostsApiTest {
    @Test
    fun parsesPostsFromJson() = runTest {
        val engine = MockEngine {
            respond(
                content = """[{"userId":1,"id":7,"title":"Hello","body":"World"}]""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val client = HttpClient(engine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }

        val posts = PostsApi(client).fetchPosts()

        assertEquals(1, posts.size)
        assertEquals(7L, posts.first().id)
        assertEquals("Hello", posts.first().title)
    }
}
