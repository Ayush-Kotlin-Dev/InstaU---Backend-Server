package instaU.ayush.com.model.qna

import kotlinx.serialization.Serializable

@Serializable
data class QnaTextParams(
    val content: String,
    val userId: Long,
)
@Serializable
data class Question(
    val id: Long,
//    val techStackId: String ? = null, TODO later use this for forum section QNA
    val authorId: Long,
    val content: String,
    val createdAt: String,
)

@Serializable
data class QuestionResponse(
    val success: Boolean,
    val question: Question? = null,
    val message: String? = null
)

@Serializable
data class QuestionsResponse(
    val success: Boolean,
    val questions: List<Question> = listOf(),
    val message: String? = null
)