package edu.temple.convoy

import kotlinx.serialization.Serializable

@Serializable
data class ConvoyApiResponse(
    val status: String,
    val convoy_id: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val session_key: String? = null,
    val message: String? = null
)