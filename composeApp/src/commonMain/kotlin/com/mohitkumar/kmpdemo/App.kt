package com.mohitkumar.kmpdemo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mohitkumar.kmpdemo.data.DatabaseDriverFactory
import com.mohitkumar.kmpdemo.data.PostsApi
import com.mohitkumar.kmpdemo.data.PostsRepository
import com.mohitkumar.kmpdemo.db.AppDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    val store = remember {
        PostsStore(PostsRepository(PostsApi(), AppDatabase(driverFactory.createDriver())))
    }
    DisposableEffect(store) { onDispose { store.clear() } }
    val state by store.state.collectAsState()

    MaterialTheme {
        Scaffold(topBar = { TopAppBar(title = { Text("KMP Posts") }) }) { padding ->
            Column(Modifier.fillMaxSize().padding(padding)) {
                if (state.isRefreshing) CircularProgressIndicator(Modifier.padding(16.dp))
                state.error?.let {
                    Text(
                        "Offline: showing cached data ($it)",
                        Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error,
                    )
                    Button(onClick = store::refresh, modifier = Modifier.padding(horizontal = 16.dp)) { Text("Retry") }
                }
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(state.posts, key = { it.id }) { post ->
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(12.dp)) {
                                Text(post.title, style = MaterialTheme.typography.titleMedium)
                                Text(post.body, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
