package instaU.ayush.com.di

import instaU.ayush.com.dao.follows.FollowsDao
import instaU.ayush.com.dao.follows.FollowsDaoImpl
import instaU.ayush.com.dao.user.UserDao
import instaU.ayush.com.dao.user.UserDaoImpl
import instaU.ayush.com.repository.auth.AuthRepository
import instaU.ayush.com.repository.auth.AuthRepositoryImpl
import instaU.ayush.com.repository.follows.FollowRepository
import instaU.ayush.com.repository.follows.FollowRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }
    single<FollowsDao>{ FollowsDaoImpl()}
    single<FollowRepository>{ FollowRepositoryImpl(get(), get()) }

}