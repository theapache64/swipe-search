package com.github.theapache64.swipesearch.data.remote

import com.github.theapache64.swipesearch.util.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {
    @GET("search/repositories")
    fun searchRepos(
        @Query("q") query: String,
        @Query("page") page : Int,
        @Query("per_page") perPage : Int = 10
    ): Flow<Resource<SearchResponse>>
}