package instaU.ayush.com.di

import instaU.ayush.com.chat.GetHistoryMessagesUseCase
import instaU.ayush.com.chat.data.source.ChatDataSource
import instaU.ayush.com.chat.data.source.ChatDataSourceImpl
import instaU.ayush.com.chat.domain.repository.ChatRepository
import instaU.ayush.com.chat.domain.repository.ChatRepositoryImpl
import instaU.ayush.com.chat.resource.data.ConnectToSocketUseCase
import instaU.ayush.com.chat.resource.usecase.FriendListUseCase
import instaU.ayush.com.dao.PostComment.PostCommentsDao
import instaU.ayush.com.dao.PostComment.PostCommentsDaoImpl
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
import instaU.ayush.com.repository.follows.FollowRepository
import instaU.ayush.com.repository.follows.FollowRepositoryImpl
import instaU.ayush.com.repository.post.PostRepository
import instaU.ayush.com.repository.post.PostRepositoryImpl
import instaU.ayush.com.repository.post_likes.PostLikesRepository
import instaU.ayush.com.repository.post_likes.PostLikesRepositoryImpl
import instaU.ayush.com.repository.profile.ProfileRepository
import instaU.ayush.com.repository.profile.ProfileRepositoryImpl
import instaU.ayush.com.repository.qna.QnaRepository
import instaU.ayush.com.repository.qna.QnaRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
    single<FollowsDao>{ FollowsDaoImpl()}
    single <ChatDataSource>{ ChatDataSourceImpl()}
    single<FollowRepository>{ FollowRepositoryImpl(get(), get()) }
    single <PostLikesDao>{ PostLikesDaoImpl()}
    single <PostDao>{ PostDaoImpl()}
    single<PostRepository> { PostRepositoryImpl(get(), get(), get()) }
    single <ProfileRepository> { ProfileRepositoryImpl(get(), get()) }
    single<PostCommentsDao> { PostCommentsDaoImpl() }
    single<PostCommentsRepository> { PostCommentsRepositoryImpl(get(), get()) }
    single<PostLikesRepository> { PostLikesRepositoryImpl(get(), get()) }
    single < ChatRepository> { ChatRepositoryImpl(get()) }
    single <QnaRepository>{ QnaRepositoryImpl(get()) }


    single { FriendListUseCase(get()) }
    single { ConnectToSocketUseCase(get()) }
    single { GetHistoryMessagesUseCase(get()) }



}