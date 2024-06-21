package instau.ayush.com.dao.PostComment

import instau.ayush.com.dao.DatabaseFactory.dbQuery
import instau.ayush.com.dao.user.UserTable
import instau.ayush.com.util.IdGenerator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class PostCommentsDaoImpl : PostCommentsDao {
    override suspend fun addComment(postId: Long, userId: Long, content: String): PostCommentRow? {
        return dbQuery {
            val commentId = IdGenerator.generateId()

            PostCommentTable.insert {
                it[PostCommentTable.commentId] = commentId
                it[PostCommentTable.postId] = postId
                it[PostCommentTable.userId] = userId
                it[PostCommentTable.comment] = content
            }

            PostCommentTable
                .join(
                    otherTable = UserTable,
                    onColumn = PostCommentTable.userId,
                    otherColumn = UserTable.id,
                    joinType = JoinType.INNER
                )
                .select { (PostCommentTable.postId eq postId) and (PostCommentTable.commentId eq commentId) }
                .singleOrNull()
                ?.let { toPostCommentRow(it) }
        }
    }

    override suspend fun removeComment(commentId: Long, postId: Long): Boolean {
        return dbQuery {
            PostCommentTable.deleteWhere {
                (PostCommentTable.commentId eq commentId) and (PostCommentTable.postId eq postId)
            } > 0
        }
    }

    override suspend fun findComment(commentId: Long, postId: Long): PostCommentRow? {
        return dbQuery {
            PostCommentTable
                .join(
                    otherTable = UserTable,
                    onColumn = PostCommentTable.userId,
                    otherColumn = UserTable.id,
                    joinType = JoinType.INNER
                )
                .select { (PostCommentTable.postId eq postId) and (PostCommentTable.commentId eq commentId) }
                .singleOrNull()
                ?.let { toPostCommentRow(it) }
        }
    }

    override suspend fun getComments(postId: Long, pageNumber: Int, pageSize: Int): List<PostCommentRow> {
        return dbQuery {
            PostCommentTable
                .join(
                    otherTable = UserTable,
                    onColumn = PostCommentTable.userId,
                    otherColumn = UserTable.id,
                    joinType = JoinType.INNER
                )
                .select { PostCommentTable.postId eq postId }
                .orderBy(PostCommentTable.createdAt, SortOrder.DESC)
                .limit(pageSize, ((pageNumber - 1) * pageSize).toLong())
                .map { toPostCommentRow(it) }
        }
    }

    private fun toPostCommentRow(resultRow:  ResultRow): PostCommentRow {
        return PostCommentRow(
            commentId = resultRow[PostCommentTable.commentId],
            content = resultRow[PostCommentTable.comment],
            postId = resultRow[PostCommentTable.postId],
            userId = resultRow[PostCommentTable.userId],
            userName = resultRow[UserTable.name],
            userImageUrl = resultRow[UserTable.imageUrl],
            createdAt = resultRow[PostCommentTable.createdAt].toString()
        )
    }
}


