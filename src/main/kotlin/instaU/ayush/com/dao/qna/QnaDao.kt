package instaU.ayush.com.dao.qna

interface QnaDao {
    //Question related functions

    suspend fun createQuestion(authorId: Long, content: String): Boolean

    suspend fun getQuestions(pageNumber: Int, pageSize: Int): List<QuestionRow>

    suspend fun getQuestion(questionId: Long): QuestionRow?

    suspend fun deleteQuestion(questionId: String): Boolean


    //Answer related functions

    suspend fun createAnswer(questionId: String, authorId: Long, content: String): Boolean

    suspend fun getAnswers(questionId: String, pageNumber: Int, pageSize: Int): List<AnswerRow>

    suspend fun getAnswer(answerId: String): AnswerRow?

    suspend fun deleteAnswer(answerId: String): Boolean

    //  suspend fun updateAnswerCommentsCount(answerId: String, decrement: Boolean = false): Boolean //TODO: Implement this
}
