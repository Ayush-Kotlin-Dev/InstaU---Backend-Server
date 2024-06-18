package instaU.ayush.com.model.qna

import kotlinx.serialization.Serializable

@Serializable
data class Answer(
    val id: Long,
    val questionId: Long,
    val authorId: Long,
    val content: String,
    val createdAt: String,
)
@Serializable
data class AnswerResponse(
    val success: Boolean,
    val answer: Answer? = null,
    val message: String? = null
)

@Serializable
data class AnswersResponse(
    val success: Boolean,
    val answers: List<Answer> = listOf(),
    val message: String? = null
)