package instaU.ayush.com.di

import instaU.ayush.com.dao.user.UserDao
import instaU.ayush.com.dao.user.UserDaoImpl
import instaU.ayush.com.repository.auth.AuthRepository
import instaU.ayush.com.repository.auth.AuthRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<UserDao> { UserDaoImpl() }

}