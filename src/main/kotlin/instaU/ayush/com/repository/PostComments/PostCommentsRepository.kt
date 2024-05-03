package instaU.ayush.com.repository.PostComments

import instaU.ayush.com.model.CommentResponse
import instaU.ayush.com.model.GetCommentResponse
import instaU.ayush.com.model.NewCommentParams
import instaU.ayush.com.model.RemoveCommentParams
import instaU.ayush.com.util.Response

interface PostCommentsRepository {

    suspend fun addComment(params : NewCommentParams): Response<CommentResponse>

    suspend fun removeComment(params : RemoveCommentParams): Response<CommentResponse>

    suspend fun getComments(postId : Long , pageNumber : Int , pageSize : Int ): Response<GetCommentResponse>



}