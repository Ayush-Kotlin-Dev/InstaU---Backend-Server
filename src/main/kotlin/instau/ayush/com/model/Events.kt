package instau.ayush.com.model

import kotlinx.serialization.Serializable


@Serializable
data class Event(
    val id: Long? = null,
    val name: String,
    val description: String,
    val imageUrl: String = "",
    val dateTime: String,
    val organizer: String,
    val howToJoin: String,
    val additionalInfo: String,
    val location: String,
)

@Serializable
data class EventParams(
    val name: String,
    val description: String,
    val imageUrl: String = "",
    val dateTime: String,
    val organizer: String,
    val howToJoin: String,
    val additionalInfo: String,
    val location: String
)

@Serializable
data class EventResponse(
    val success: Boolean,
    val event: Event? = null,
    val message: String? = null
)

@Serializable
data class EventsResponse(
    val success: Boolean,
    val events: List<Event> = listOf(),
    val message: String? = null
)