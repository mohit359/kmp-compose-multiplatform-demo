package com.mohitkumar.kmpdemo

import com.mohitkumar.kmpdemo.data.Post
import com.mohitkumar.kmpdemo.data.PostsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PostsUiState(
    val posts: List<Post> = emptyList(),
    val isRefreshing: Boolean = false,
    val error: String? = null,
)

/** Shared presentation logic (acts as a ViewModel on both platforms). */
class PostsStore(
    private val repository: PostsRepository,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main),
) {
    private val _state = MutableStateFlow(PostsUiState())
    val state: StateFlow<PostsUiState> = _state.asStateFlow()

    init {
        scope.launch { repository.posts.collect { list -> _state.update { it.copy(posts = list) } } }
        refresh()
    }

    fun refresh() {
        scope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }
            try {
                repository.refresh()
                _state.update { it.copy(isRefreshing = false) }
            } catch (e: Exception) {
                _state.update { it.copy(isRefreshing = false, error = e.message ?: "Network error") }
            }
        }
    }

    fun clear() = scope.cancel()
}
