package instaU.ayush.com.repository.qna

import instaU.ayush.com.dao.qna.AnswerRow
import instaU.ayush.com.dao.qna.QuestionRow
import instaU.ayush.com.model.PostTextParams
import instaU.ayush.com.model.qna.*
import instaU.ayush.com.util.Response

interface QnaRepository {
    //Question related functions

    suspend fun createQuestion(qnaTextParams: QnaTextParams): Response<QuestionResponse>

    suspend fun getQuestions(pageNumber: Int, pageSize: Int): Response<QuestionsResponse>

    suspend fun getQuestion(questionId: Long):  Response<QuestionResponse>

    suspend fun deleteQuestion(questionId: Long): Response<QuestionResponse>

    //Answer related functions

    suspend fun createAnswer(questionId: String, authorId: Long, content: String): Response<AnswerResponse>

    suspend fun getAnswers(questionId: String, pageNumber: Int, pageSize: Int): Response<AnswersResponse>

    suspend fun getAnswer(answerId: String): Response<AnswerResponse>

    suspend fun deleteAnswer(answerId: String): Response<AnswerResponse>

    //  suspend fun updateAnswerCommentsCount(answerId: String, decrement: Boolean = false): Boolean //TODO: Implement this




}