package instaU.ayush.com.model.qna

data class Question(
    val id: String,
//    val techStackId: String ? = null, TODO later use this for forum section QNA
    val authorId: String,
    val content: String,
    val createdAt: String,
)
