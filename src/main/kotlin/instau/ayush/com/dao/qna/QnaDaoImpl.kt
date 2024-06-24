package instau.ayush.com.dao.qna

import instau.ayush.com.dao.DatabaseFactory.dbQuery
import instau.ayush.com.util.IdGenerator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class QnaDaoImpl : QnaDao {
    override suspend fun createQuestion(authorName: String , authorId: Long, content: String): Boolean {
        return dbQuery {
            val insertStatement = QuestionsTable.insert {

                it[QuestionsTable.questionId] = IdGenerator.generateId()
                it[QuestionsTable.userName] = authorName
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
                        authorName = it[QuestionsTable.userName],
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
                        authorName = it[QuestionsTable.userName],
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

    override suspend fun createAnswer(userName : String , questionId: Long, authorId: Long, content: String): Boolean {
        return dbQuery {
            val insertStatement = AnswersTable.insert {
                it[AnswersTable.answerId] = IdGenerator.generateId()
                it[AnswersTable.userName] = userName
                it[AnswersTable.questionId] = questionId
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
                        authorName = it[AnswersTable.userName],
                        content = it[AnswersTable.answer],
                        createdAt = it[AnswersTable.createdAt].toString()
                    )
                }
        }
    }

    override suspend fun getAnswer(questionId: Long): AnswerRow? {
        return dbQuery {
            AnswersTable.select { AnswersTable.questionId eq questionId }
                .orderBy(AnswersTable.createdAt, SortOrder.DESC)
                .limit(1)
                .map {
                    AnswerRow(
                        id = it[AnswersTable.answerId],
                        questionId = it[AnswersTable.questionId],
                        authorId = it[AnswersTable.userId],
                        authorName = it[AnswersTable.userName],
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