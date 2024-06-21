package instau.ayush.com.repository.auth

import instau.ayush.com.model.AuthResponse
import instau.ayush.com.model.SignInParams
import instau.ayush.com.model.SignUpParams
import instau.ayush.com.util.Response

interface AuthRepository {
    suspend fun signUp(params: SignUpParams): Response<AuthResponse>
    suspend fun signIn(params: SignInParams): Response<AuthResponse>
}