package instaU.ayush.com.repository.auth

import instaU.ayush.com.model.AuthResponse
import instaU.ayush.com.model.SignInParams
import instaU.ayush.com.model.SignUpParams
import instaU.ayush.com.util.Response

interface AuthRepository {
    suspend fun signUp(params: SignUpParams): Response<AuthResponse>
    suspend fun signIn(params: SignInParams): Response<AuthResponse>
}