package instaU.ayush.com.repository.qna

import instaU.ayush.com.dao.qna.AnswerRow
import instaU.ayush.com.dao.qna.QnaDao
import instaU.ayush.com.dao.qna.QuestionRow
import instaU.ayush.com.model.PostResponse
import instaU.ayush.com.model.qna.*
import instaU.ayush.com.util.Response
import io.ktor.http.*

class QnaRepositoryImpl(
    private val qnaDao: QnaDao
) : QnaRepository {
    override suspend fun createQuestion(authorId: Long, content: String): Response<QuestionResponse> {
        val questionIsCreated = qnaDao.createQuestion(authorId, content)

        return if (questionIsCreated) {
            Response.Success(
                data = QuestionResponse(
                    success = true
                )
            )
        } else {
            Response.Error(
                HttpStatusCode.InternalServerError,
                data = QuestionResponse(
                    success = false
                )
            )
        }
    }

    override suspend fun getQuestions(pageNumber: Int, pageSize: Int): Response<QuestionsResponse> {
        val questionRows = qnaDao.getQuestions(pageNumber, pageSize)
        val questions = questionRows.map {
            toQuestion(it)
        }
        return Response.Success(
            data = QuestionsResponse(
                success = true,
                questions = questions
            )
        )
    }

    override suspend fun getQuestion(questionId: Long): Response<QuestionResponse?> {
        val questionRow = qnaDao.getQuestion(questionId)
        return if (questionRow != null) {
            Response.Success(
                data = QuestionResponse(
                    success = true,
                    question = toQuestion(questionRow)
                )
            )
        } else {
            Response.Success(
                data = null
            )
        }
    }

    override suspend fun deleteQuestion(questionId: String): Response<QuestionResponse> {
        val questionIsDeleted = qnaDao.deleteQuestion(questionId)

        return if (questionIsDeleted) {
            Response.Success(
                data = QuestionResponse(
                    success = true
                )
            )
        } else {
            Response.Error(
                HttpStatusCode.InternalServerError,
                data = QuestionResponse(
                    success = false
                )
            )
        }
    }

    override suspend fun createAnswer(questionId: String, authorId: Long, content: String): Response<AnswerResponse> {
        val answerIsCreated = qnaDao.createAnswer(questionId, authorId, content)

        return if (answerIsCreated) {
            Response.Success(
                data = AnswerResponse(
                    success = true
                )
            )
        } else {
            Response.Error(
                HttpStatusCode.InternalServerError,
                data = AnswerResponse(
                    success = false
                )
            )
        }
    }

    override suspend fun getAnswers(questionId: String, pageNumber: Int, pageSize: Int): Response<AnswersResponse> {
        val answerRows = qnaDao.getAnswers(questionId, pageNumber, pageSize)
        val answers = answerRows.map {
            Answer(
                id = it.id,
                questionId = it.questionId,
                authorId = it.authorId,
                content = it.content,
                createdAt = it.createdAt
            )
        }
        return Response.Success(
            data = AnswersResponse(
                success = true,
                answers = answers
            )
        )
    }

    override suspend fun getAnswer(answerId: String): Response<AnswerResponse> {
        val answerRow = qnaDao.getAnswer(answerId)
        return if (answerRow != null) {
            Response.Success(
                data = AnswerResponse(
                    success = true,
                    answer = Answer(
                        id = answerRow.id,
                        questionId = answerRow.questionId,
                        authorId = answerRow.authorId,
                        content = answerRow.content,
                        createdAt = answerRow.createdAt
                    )
                )
            )
        } else {
            Response.Error(
                HttpStatusCode.NotFound,
                data = AnswerResponse(
                    success = false
                )
            )
        }
    }

    override suspend fun deleteAnswer(answerId: String): Response<AnswerResponse> {
        val answerIsDeleted = qnaDao.deleteAnswer(answerId)

        return if (answerIsDeleted) {
            Response.Success(
                data = AnswerResponse(
                    success = true
                )
            )
        } else {
            Response.Error(
                HttpStatusCode.InternalServerError,
                data = AnswerResponse(
                    success = false
                )
            )
        }
    }

    private fun toQuestion(questionRow: QuestionRow): Question {
        return Question(
            id = questionRow.id,
            authorId = questionRow.authorId,
            content = questionRow.content,
            createdAt = questionRow.createdAt
        )
    }
}