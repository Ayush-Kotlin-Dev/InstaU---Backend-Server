package instau.ayush.com.repository.PostComments

import instau.ayush.com.model.CommentResponse
import instau.ayush.com.model.GetCommentResponse
import instau.ayush.com.model.NewCommentParams
import instau.ayush.com.model.RemoveCommentParams
import instau.ayush.com.util.Response

interface PostCommentsRepository {

    suspend fun addComment(params : NewCommentParams): Response<CommentResponse>

    suspend fun removeComment(params : RemoveCommentParams): Response<CommentResponse>

    suspend fun getComments(postId : Long , pageNumber : Int , pageSize : Int ): Response<GetCommentResponse>



}