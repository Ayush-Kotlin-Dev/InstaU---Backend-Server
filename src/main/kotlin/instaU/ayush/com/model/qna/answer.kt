package instaU.ayush.com.model.qna

data class Answer(
    val id: String,
    val questionId: String,
    val authorId: String,
    val content: String,
    val createdAt: String,
)
