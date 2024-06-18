package instaU.ayush.com.dao.qna

import instaU.ayush.com.dao.post.PostTable.defaultExpression
import instaU.ayush.com.dao.post.PostTable.references
import instaU.ayush.com.dao.post.PostTable.uniqueIndex
import instaU.ayush.com.dao.user.UserTable
import instaU.ayush.com.util.CurrentDateTime
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object QuestionsTable  : Table("questions") {
    val questionId = long("question_id").uniqueIndex()
    val question = varchar("question" , length = 300)
    val authorId = long("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
}

object AnswersTable : Table("answers") {
    val answerId = long("answer_id").uniqueIndex()
    val questionId = long("question_id").references(QuestionsTable.questionId, onDelete = ReferenceOption.CASCADE)
    val userId = long("user_id").references(UserTable.id, onDelete = ReferenceOption.CASCADE)
    val answer = varchar("answer" , length = 300)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
}

data class AnswerRow(
    val id: Long,
    val questionId: Long,
    val authorId: Long,
    val content: String,
    val createdAt: String,
)

data class QuestionRow(
    val id: Long,
    val authorId: Long,
    val content: String,
    val createdAt: String,
)
