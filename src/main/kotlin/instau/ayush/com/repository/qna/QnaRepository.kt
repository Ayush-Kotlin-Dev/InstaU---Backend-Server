package instau.ayush.com.repository.qna

import instau.ayush.com.model.qna.*
import instau.ayush.com.util.Response

interface QnaRepository {
    //Question related functions

    suspend fun createQuestion(qnaTextParams: QnaTextParams): Response<QuestionResponse>

    suspend fun getQuestions(pageNumber: Int, pageSize: Int): Response<QuestionsResponse>

    suspend fun getQuestion(questionId: Long):  Response<QuestionResponse>

    suspend fun deleteQuestion(questionId: Long): Response<QuestionResponse>

    //Answer related functions

    suspend fun createAnswer(answerTextParams: AnswerTextParams): Response<AnswerResponse>

    suspend fun getAnswers(questionId: Long, pageNumber: Int, pageSize: Int): Response<AnswersResponse>


    suspend fun deleteAnswer(answerId: Long): Response<AnswerResponse>

    //  suspend fun updateAnswerCommentsCount(answerId: String, decrement: Boolean = false): Boolean //TODO: Implement this




}