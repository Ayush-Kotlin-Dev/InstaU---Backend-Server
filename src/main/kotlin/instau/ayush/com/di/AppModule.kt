package instau.ayush.com.di

import instau.ayush.com.chat.GetHistoryMessagesUseCase
import instau.ayush.com.chat.data.source.ChatDataSource
import instau.ayush.com.chat.data.source.ChatDataSourceImpl
import instau.ayush.com.chat.domain.repository.ChatRepository
import instau.ayush.com.chat.domain.repository.ChatRepositoryImpl
import instau.ayush.com.chat.resource.data.ConnectToSocketUseCase
import instau.ayush.com.chat.resource.usecase.FriendListUseCase
import instau.ayush.com.dao.PostComment.PostCommentsDao
import instau.ayush.com.dao.PostComment.PostCommentsDaoImpl
import instau.ayush.com.dao.follows.FollowsDao
import instau.ayush.com.dao.follows.FollowsDaoImpl
import instau.ayush.com.dao.post.PostDao
import instau.ayush.com.dao.post.PostDaoImpl
import instau.ayush.com.dao.postlikes.PostLikesDao
import instau.ayush.com.dao.postlikes.PostLikesDaoImpl
import instau.ayush.com.dao.qna.QnaDao
import instau.ayush.com.dao.qna.QnaDaoImpl
import instau.ayush.com.dao.user.UserDao
import instau.ayush.com.dao.user.UserDaoImpl
import instau.ayush.com.repository.PostComments.PostCommentsRepository
import instau.ayush.com.repository.PostComments.PostCommentsRepositoryImpl
import instau.ayush.com.repository.auth.AuthRepository
import instau.ayush.com.repository.auth.AuthRepositoryImpl
import instau.ayush.com.repository.follows.FollowRepository
import instau.ayush.com.repository.follows.FollowRepositoryImpl
import instau.ayush.com.repository.post.PostRepository
import instau.ayush.com.repository.post.PostRepositoryImpl
import instau.ayush.com.repository.post_likes.PostLikesRepository
import instau.ayush.com.repository.post_likes.PostLikesRepositoryImpl
import instau.ayush.com.repository.profile.ProfileRepository
import instau.ayush.com.repository.profile.ProfileRepositoryImpl
import instau.ayush.com.repository.qna.QnaRepository
import instau.ayush.com.repository.qna.QnaRepositoryImpl
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
    single<QnaDao> { QnaDaoImpl() }
    single <QnaRepository>{ QnaRepositoryImpl(get()) }


    single { FriendListUseCase(get()) }
    single { ConnectToSocketUseCase(get()) }
    single { GetHistoryMessagesUseCase(get()) }



}