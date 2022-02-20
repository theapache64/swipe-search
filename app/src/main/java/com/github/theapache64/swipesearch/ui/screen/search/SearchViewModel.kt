package com.github.theapache64.swipesearch.ui.screen.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.theapache64.swipesearch.data.remote.Item
import com.github.theapache64.swipesearch.data.repo.SearchRepo
import com.github.theapache64.swipesearch.ui.composable.twyper.SwipedOutDirection
import com.github.theapache64.swipesearch.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class SearchUiState(
    val loadingMessage: String? = null,
    val errorMessage: String? = null,
    val query: String = "",
    val items: SnapshotStateList<Item> = mutableStateListOf()
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepo: SearchRepo
) : ViewModel() {

    private var searchJob: Job? = null
    var pageNo = 0
    var totalPages = -1
    var uiState by mutableStateOf(SearchUiState())
        private set

    init {
        onQueryChanged("OkHttp")
    }

    fun onQueryChanged(newQuery: String) {
        uiState = uiState.copy(query = newQuery)
        pageNo = 1
        totalPages = -1
        loadPage(debounce = 600)
    }

    fun onItemSwipedOut(item: Item, direction: SwipedOutDirection) {
        println("Swiped out: $direction")
        uiState.items.remove(item)
    }

    fun onPageEndReached() {
        pageNo++
        loadPage(debounce = 0)
    }

    private fun loadPage(
        debounce: Long
    ) {
        searchJob?.cancel()

        if (totalPages != -1 && pageNo > totalPages) {
            uiState = uiState.copy(
                errorMessage = "No more results",
                loadingMessage = null
            )
            return
        }

        val query = uiState.query
        if (query.isBlank()) return
        searchJob = viewModelScope.launch {
            delay(debounce)
            searchRepo.search(
                query = query,
                page = pageNo
            ).collect {
                when (it) {
                    is Resource.Error -> {
                        Timber.d("onPageEndReached: Error: '${it.errorData}'")
                        uiState = uiState.copy(
                            loadingMessage = null,
                            errorMessage = it.errorData
                        )
                    }
                    is Resource.Loading -> {
                        val message = if (totalPages == -1) {
                            "Loading first page"
                        } else {
                            "Loading page $pageNo/$totalPages"
                        }
                        uiState = uiState.copy(
                            loadingMessage = message,
                            errorMessage = null
                        )
                    }
                    is Resource.Success -> {
                        val items = it.data.items
                        if (items.isNotEmpty()) {
                            totalPages = it.data.totalCount / items.size
                            with(uiState) {
                                this.items.clear()
                                this.items.addAll(items)
                                uiState = copy(loadingMessage = null, errorMessage = null)
                            }
                        } else {
                            totalPages = -1
                            uiState = uiState.copy(
                                loadingMessage = null,
                                errorMessage = "No result found for '$query'"
                            )
                        }

                    }
                }
            }
        }
    }

}