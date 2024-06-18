package instaU.ayush.com.dao.qna

import instaU.ayush.com.dao.DatabaseFactory.dbQuery
import instaU.ayush.com.util.IdGenerator
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class QnaDaoImpl : QnaDao {
    override suspend fun createQuestion(authorId: Long, content: String): Boolean {
        return dbQuery {
            val insertStatement = QuestionsTable.insert {
                it[QuestionsTable.questionId] = IdGenerator.generateId()
                it[QuestionsTable.authorId] = authorId
                it[QuestionsTable.question] = content
            }
            insertStatement.resultedValues?.singleOrNull() != null
        }
    }

    override suspend fun getQuestions(pageNumber: Int, pageSize: Int): List<QuestionRow> {
        return dbQuery {
            QuestionsTable.selectAll()
                .limit(n = pageSize, offset = ((pageNumber - 1) * pageSize).toLong())
                .map {
                    QuestionRow(
                        id = it[QuestionsTable.questionId],
                        authorId = it[QuestionsTable.authorId],
                        content = it[QuestionsTable.question],
                        createdAt = it[QuestionsTable.createdAt].toString()
                    )
                }
        }
    }

    override suspend fun getQuestion(questionId: Long): QuestionRow? {
        return dbQuery {
            QuestionsTable.select { QuestionsTable.questionId eq questionId }
                .map {
                    QuestionRow(
                        id = it[QuestionsTable.questionId],
                        authorId = it[QuestionsTable.authorId],
                        content = it[QuestionsTable.question],
                        createdAt = it[QuestionsTable.createdAt].toString()
                    )
                }
                .singleOrNull()
        }
    }

    override suspend fun deleteQuestion(questionId: Long): Boolean {
        return dbQuery {
            QuestionsTable.deleteWhere { QuestionsTable.questionId eq questionId } > 0
        }
    }

    override suspend fun createAnswer(questionId: Long, authorId: Long, content: String): Boolean {
        return dbQuery {
            val insertStatement = AnswersTable.insert {
                it[AnswersTable.answerId] = IdGenerator.generateId()
                it[AnswersTable.questionId] = questionId.toLong()
                it[AnswersTable.userId] = authorId
                it[AnswersTable.answer] = content
            }
            insertStatement.resultedValues?.singleOrNull() != null
        }
    }

    override suspend fun getAnswers(questionId: Long, pageNumber: Int, pageSize: Int): List<AnswerRow> {
        return dbQuery {
            AnswersTable.select { AnswersTable.questionId eq questionId }
                .limit(n = pageSize, offset = ((pageNumber - 1) * pageSize).toLong())
                .map {
                    AnswerRow(
                        id = it[AnswersTable.answerId],
                        questionId = it[AnswersTable.questionId],
                        authorId = it[AnswersTable.userId],
                        content = it[AnswersTable.answer],
                        createdAt = it[AnswersTable.createdAt].toString()
                    )
                }
        }
    }

    override suspend fun getAnswer(answerId: Long): AnswerRow? {
        return dbQuery {
            AnswersTable.select { AnswersTable.answerId eq answerId}
                .map {
                    AnswerRow(
                        id = it[AnswersTable.answerId],
                        questionId = it[AnswersTable.questionId],
                        authorId = it[AnswersTable.userId],
                        content = it[AnswersTable.answer],
                        createdAt = it[AnswersTable.createdAt].toString()
                    )
                }
                .singleOrNull()
        }
    }


    override suspend fun deleteAnswer(answerId: Long): Boolean {
        return dbQuery {
            AnswersTable.deleteWhere { AnswersTable.answerId eq answerId } > 0
        }
    }
}