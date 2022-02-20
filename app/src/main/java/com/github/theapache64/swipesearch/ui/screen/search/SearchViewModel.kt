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

/**
 * One object to hold entire UI state.
 * TODO: Consider create mutually exclusive state (using sealed class) if grows.
 */
data class SearchUiState(
    /**
     * To show message with progressbar
     */
    val loadingMsg: String? = null,
    /**
     * To show message (without progressbar)
     */
    val blockingMsg: String? = "🔍 Use the above text field to start explore!",
    val subTitle: String? = null,
    val query: String = "",
    // Using SnapshotStateList because remove/addCall will trigger a recomposition automatically.
    // No need to mutate the entire
    val items: SnapshotStateList<Item> = mutableStateListOf()
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepo: SearchRepo
) : ViewModel() {

    /**
     * To support request debounce
     */
    private var searchJob: Job? = null
    private var pageNo = 0

    /**
     * -1 = first API not done yet
     */
    private var totalPages = -1
    var uiState by mutableStateOf(SearchUiState())
        private set

    fun onQueryChanged(newQuery: String) {
        // To update textField UI
        uiState = uiState.copy(query = newQuery)
        // reset page no
        pageNo = 1
        totalPages = -1

        // make first API call
        loadPage(debounce = 300)
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
                blockingMsg = "No more results",
                loadingMsg = null
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
                            loadingMsg = null,
                            blockingMsg = it.errorData
                        )
                    }
                    is Resource.Loading -> {
                        /**
                         * If it's the first API call, we'll simply show 'Loading first page',
                         * otherwise we'll show loading with current page and total page available.
                         */
                        val message = if (totalPages == -1) {
                            "Loading first page"
                        } else {
                            "Loading page $pageNo/$totalPages"
                        }
                        uiState = uiState.copy(
                            loadingMsg = message,
                            blockingMsg = null,
                            subTitle = if (pageNo == 1) {
                                null
                            } else {
                                uiState.subTitle
                            }
                        )
                    }
                    is Resource.Success -> {
                        val remoteItems = it.data.items
                        with(uiState) {
                            this.items.clear()
                            if (remoteItems.isNotEmpty()) {
                                totalPages = it.data.totalCount / remoteItems.size
                                this.items.addAll(remoteItems)
                                uiState = copy(
                                    loadingMsg = null,
                                    blockingMsg = null,
                                    subTitle = "Found ${it.data.totalCount} repo(s)"
                                )

                            } else {
                                totalPages = -1
                                uiState = copy(
                                    loadingMsg = null,
                                    blockingMsg = "No result found for '$query'",
                                    subTitle = null
                                )
                            }
                        }

                    }
                }
            }
        }
    }

}