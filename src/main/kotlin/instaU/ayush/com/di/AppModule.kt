package instaU.ayush.com.di

import instaU.ayush.com.dao.PostComment.PostCommentsDao
import instaU.ayush.com.dao.PostComment.PostCommentsDaoImpl
import instaU.ayush.com.dao.chat.MessageDao
import instaU.ayush.com.dao.chat.MessageDaoImpl
import instaU.ayush.com.dao.chat.MessageRow
import instaU.ayush.com.dao.follows.FollowsDao
import instaU.ayush.com.dao.follows.FollowsDaoImpl
import instaU.ayush.com.dao.post.PostDao
import instaU.ayush.com.dao.post.PostDaoImpl
import instaU.ayush.com.dao.postlikes.PostLikesDao
import instaU.ayush.com.dao.postlikes.PostLikesDaoImpl
import instaU.ayush.com.dao.user.UserDao
import instaU.ayush.com.dao.user.UserDaoImpl
import instaU.ayush.com.repository.PostComments.PostCommentsRepository
import instaU.ayush.com.repository.PostComments.PostCommentsRepositoryImpl
import instaU.ayush.com.repository.auth.AuthRepository
import instaU.ayush.com.repository.auth.AuthRepositoryImpl
import instaU.ayush.com.repository.chat.MessageRepository
import instaU.ayush.com.repository.chat.MessageRepositoryImpl
import instaU.ayush.com.repository.follows.FollowRepository
import instaU.ayush.com.repository.follows.FollowRepositoryImpl
import instaU.ayush.com.repository.post.PostRepository
import instaU.ayush.com.repository.post.PostRepositoryImpl
import instaU.ayush.com.repository.post_likes.PostLikesRepository
import instaU.ayush.com.repository.post_likes.PostLikesRepositoryImpl
import instaU.ayush.com.repository.profile.ProfileRepository
import instaU.ayush.com.repository.profile.ProfileRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
    single<FollowsDao>{ FollowsDaoImpl()}
    single<FollowRepository>{ FollowRepositoryImpl(get(), get()) }
    single <PostLikesDao>{ PostLikesDaoImpl()}
    single <PostDao>{ PostDaoImpl()}
    single<PostRepository> { PostRepositoryImpl(get(), get(), get()) }
    single <ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single<PostCommentsDao> { PostCommentsDaoImpl() }
    single<PostCommentsRepository> { PostCommentsRepositoryImpl(get(), get()) }
    single<PostLikesRepository> { PostLikesRepositoryImpl(get(), get()) }
    single <MessageDao> { MessageDaoImpl() }
     single <MessageRepository>{ MessageRepositoryImpl(get()) }


}