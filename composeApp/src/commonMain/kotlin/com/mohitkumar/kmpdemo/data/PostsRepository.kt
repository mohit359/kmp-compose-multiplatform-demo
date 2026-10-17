package com.mohitkumar.kmpdemo.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.mohitkumar.kmpdemo.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Offline-first: the UI always reads from SQLite; [refresh] updates the cache from the network. */
class PostsRepository(
    private val api: PostsApi,
    private val database: AppDatabase,
) {
    private val queries = database.postQueries

    val posts: Flow<List<Post>> = queries.selectAll()
        .asFlow()
        .mapToList(Dispatchers.Default)
        .map { rows -> rows.map { Post(it.id, it.title, it.body) } }

    suspend fun refresh() {
        val remote = api.fetchPosts()
        database.transaction {
            queries.deleteAll()
            remote.forEach { queries.upsert(it.id, it.title, it.body) }
        }
    }
}
