package edu.temple.convoy

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val status: String,
    val session_key: String? = null,
    val message: String? = null
)