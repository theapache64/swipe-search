package com.github.theapache64.swipesearch.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class SearchResponse(
    @Json(name = "incomplete_results")
    val incompleteResults: Boolean, // false
    @Json(name = "items")
    val items: List<Item>,
    @Json(name = "total_count")
    val totalCount: Int // 171081
)

@JsonClass(generateAdapter = true)
data class Item(
    @Json(name = "description")
    val description: String, // The Kotlin Programming Language.
    @Json(name = "html_url")
    val htmlUrl: String, // https://github.com/JetBrains/kotlin
    @Json(name = "name")
    val name: String, // kotlin
    @Json(name = "owner")
    val owner: Owner,
    @Json(name = "stargazers_count")
    val stargazersCount: Int, // 40405
)

@JsonClass(generateAdapter = true)
data class Owner(
    @Json(name = "avatar_url")
    val avatarUrl: String, // https://avatars.githubusercontent.com/u/878437?v=4
    @Json(name = "login")
    val login : String
)