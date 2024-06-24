package instau.ayush.com.repository.qna

import instau.ayush.com.dao.fcm.FcmDao
import instau.ayush.com.dao.qna.QnaDao
import instau.ayush.com.dao.qna.QuestionRow
import instau.ayush.com.model.qna.*
import instau.ayush.com.repository.fcm.FcmRepository
import instau.ayush.com.util.Response
import io.ktor.http.*

class QnaRepositoryImpl(
    private val qnaDao: QnaDao,
    private val fcmDao : FcmDao
) : QnaRepository {
    override suspend fun createQuestion(qnaTextParams: QnaTextParams): Response<QuestionResponse> {
        val name = fcmDao.getUserName(qnaTextParams.userId)

        val questionIsCreated = qnaDao.createQuestion(name ,qnaTextParams.userId, qnaTextParams.content)

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
            val mostRecentAnswer = qnaDao.getAnswer(it.id)
            if(mostRecentAnswer != null) {
                toQuestion(it, mostRecentAnswer.content)
            }else{
                toQuestion(it)
            }
        }
        return Response.Success(
            data = QuestionsResponse(
                success = true,
                questions = questions
            )
        )
    }

    override suspend fun getQuestion(questionId: Long): Response<QuestionResponse> {
        val questionRow = qnaDao.getQuestion(questionId)
        return if (questionRow != null) {
            Response.Success(
                data = QuestionResponse(
                    success = true,
                    question =  toQuestion(questionRow)
                )
            )
        } else {
            Response.Error(
                HttpStatusCode.NotFound,
                data = QuestionResponse(
                    success = false
                )
            )
        }
    }

    override suspend fun deleteQuestion(questionId: Long): Response<QuestionResponse> {
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

    override suspend fun createAnswer(answerTextParams: AnswerTextParams): Response<AnswerResponse> {
        val name = fcmDao.getUserName(answerTextParams.authorId)
        val answerIsCreated = qnaDao.createAnswer(name , answerTextParams.questionId, answerTextParams.authorId, answerTextParams.content)

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

    override suspend fun getAnswers(questionId: Long, pageNumber: Int, pageSize: Int): Response<AnswersResponse> {
        val answerRows = qnaDao.getAnswers(questionId, pageNumber, pageSize)
        val answers = answerRows.map {
            Answer(
                id = it.id,
                questionId = it.questionId,
                authorId = it.authorId,
                authorName = it.authorName,
                answer = it.content,
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


    override suspend fun deleteAnswer(answerId: Long): Response<AnswerResponse> {
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

    private fun toQuestion(questionRow: QuestionRow , mostRecentAnswer : String? = null ): Question {
        return Question(
            id = questionRow.id,
            authorId = questionRow.authorId,
            authorName = questionRow.authorName,
            question = questionRow.content,
            createdAt = questionRow.createdAt,
            mostRecentAnswer = mostRecentAnswer
        )
    }
}