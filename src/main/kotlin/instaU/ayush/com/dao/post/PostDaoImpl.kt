package instaU.ayush.com.dao.post

import instaU.ayush.com.dao.DatabaseFactory.dbQuery
import instaU.ayush.com.dao.user.UserTable
import instaU.ayush.com.util.IdGenerator
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.plus

class PostDaoImpl : PostDao {
    override suspend fun createPost(userId: Long, imageUrl: String, caption: String): Boolean {

        return dbQuery {
            val insertStatement = PostTable.insert {
                it[postId] = IdGenerator.generateId()
                it[PostTable.caption] = caption
                it[PostTable.imageUrl] = imageUrl
                it[likesCount] = 0
                it[commentsCount] = 0
                it[PostTable.userId] = userId
            }
            insertStatement.resultedValues?.singleOrNull() != null
        }
    }

    override suspend fun getFeedsPost(
        userId: Long,
        follows: List<Long>,
        pageNumber: Int,
        pageSize: Int
    ): List<PostRow> {
        return dbQuery {
            if (follows.size > 1) {
                getPost(follows, pageNumber, pageSize)
            } else {
                PostTable.join(
                    otherTable = UserTable,
                    onColumn = PostTable.userId,
                    otherColumn = UserTable.id,
                    joinType = JoinType.INNER
                )
                    .selectAll()
                    .orderBy(column = PostTable.likesCount, order = SortOrder.DESC)
                    .limit(n = pageSize, offset = ((pageNumber - 1) * pageSize).toLong())
                    .map {
                        toPostRow(it)
                    }
            }
        }

    }

    override suspend fun getPostByUser(userId: Long, pageNumber: Int, pageSize: Int): List<PostRow> {
        return dbQuery {
           getPost(listOf(userId), pageNumber, pageSize )
        }
    }

    override suspend fun getPost(postId: Long): PostRow? {
        return dbQuery {
            PostTable
                .join(
                    otherTable = UserTable,
                    onColumn = PostTable.userId,
                    otherColumn = UserTable.id,
                    joinType = JoinType.INNER
                )
                .select { PostTable.postId eq postId }
                .singleOrNull()
                ?.let {
                    toPostRow(it)
                }
        }
    }

    override suspend fun updateLikesCount(postId: Long, decrement: Boolean): Boolean {
        return dbQuery {
            val value = if (decrement) -1 else 1
            PostTable.update(where = { PostTable.postId eq postId }) {
                it.update(column = likesCount, value = likesCount.plus(value))
            } > 0
        }
    }

    override suspend fun updateCommentsCount(postId: Long, decrement: Boolean): Boolean {
        return dbQuery {
            val value = if (decrement) -1 else 1
            PostTable.update(where = { PostTable.postId eq postId }) {
                it.update(column = commentsCount, value = commentsCount.plus(value))
            } > 0
        }
    }

    override suspend fun deletePost(postId: Long): Boolean {
        return dbQuery {
            PostTable.deleteWhere { PostTable.postId eq postId } > 0
        }
    }

    override suspend fun getPostCount(): Int {
        return dbQuery {
            PostTable.selectAll().count().toInt()
        }
    }

    private fun getPost(users : List<Long>, pageNumber: Int, pageSize: Int): List<PostRow> {
        return PostTable
            .join(
                otherTable = UserTable,
                onColumn = PostTable.userId,
                otherColumn = UserTable.id,
                joinType = JoinType.INNER
            )
            .select { PostTable.userId inList users }
            .orderBy(PostTable.createdAt, SortOrder.DESC)
            .limit(n = pageSize, offset = ((pageNumber - 1) * pageSize).toLong())
            .map {
                toPostRow(it)
            }
    }




    private fun toPostRow(row: ResultRow): PostRow {
        return PostRow(
            postId = row[PostTable.postId],
            caption = row[PostTable.caption],
            imageUrl = row[PostTable.imageUrl],
            likesCount = row[PostTable.likesCount],
            commentsCount = row[PostTable.commentsCount],
            userId = row[PostTable.userId],
            createdAt = row[PostTable.createdAt].toString(),
            userName = row[UserTable.name],
            userImageUrl = row[UserTable.imageUrl]
        )
    }
}
