package instau.ayush.com.dao.qna

interface QnaDao {
    //Question related functions

    suspend fun createQuestion(authorName : String , authorId: Long, content: String): Boolean

    suspend fun getQuestions(pageNumber: Int, pageSize: Int): List<QuestionRow>

    suspend fun getQuestion(questionId: Long): QuestionRow?

    suspend fun deleteQuestion(questionId: Long): Boolean


    //Answer related functions

    suspend fun createAnswer(userName : String,questionId: Long, authorId: Long, content: String): Boolean

    suspend fun getAnswers(questionId: Long, pageNumber: Int, pageSize: Int): List<AnswerRow>

    suspend fun getAnswer(questionId: Long): AnswerRow?

    suspend fun deleteAnswer(answerId: Long): Boolean

    //  suspend fun updateAnswerCommentsCount(answerId: String, decrement: Boolean = false): Boolean //TODO: Implement this
}
