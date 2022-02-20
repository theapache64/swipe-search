package com.github.theapache64.swipesearch.data.repo

import com.github.theapache64.swipesearch.data.remote.GitHubApi
import com.github.theapache64.swipesearch.data.remote.SearchResponse
import com.github.theapache64.swipesearch.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepo @Inject constructor(
    private val githubApi: GitHubApi
) {
    // TODO: Create a domain model and mapper to avoid using remote model at UI level
    fun search(query: String, page: Int): Flow<Resource<SearchResponse>> {
        return githubApi.searchRepos(query, page)
    }
}