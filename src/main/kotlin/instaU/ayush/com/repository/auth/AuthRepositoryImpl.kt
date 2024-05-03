package instaU.ayush.com.repository.auth

import instaU.ayush.com.dao.user.UserDao
import instaU.ayush.com.model.AuthResponse
import instaU.ayush.com.model.AuthResponseData
import instaU.ayush.com.model.SignInParams
import instaU.ayush.com.model.SignUpParams
import instaU.ayush.com.plugins.generateToken
import instaU.ayush.com.security.hashPassword
import instaU.ayush.com.util.Response
import io.ktor.http.*

class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun signUp(params: SignUpParams): Response<AuthResponse> {
        return if (userAlreadyExist(params.email)) {
            Response.Error(
                code = HttpStatusCode.Conflict,
                data = AuthResponse(
                    errorMessage = "A user with this email already exists!"
                )
            )
        } else {
            val insertedUser = userDao.insert(params)

            if (insertedUser == null) {
                Response.Error(
                    code = HttpStatusCode.InternalServerError,
                    data = AuthResponse(
                        errorMessage = "Oops, sorry we could not register the user, try later!"
                    )
                )
            } else {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            id = insertedUser.id,
                            name = insertedUser.name,
                            bio = insertedUser.bio,
                            token = generateToken(params.email)
                        )
                    )
                )
            }
        }
    }

    override suspend fun signIn(params: SignInParams): Response<AuthResponse> {
        val user = userDao.findByEmail(params.email)

        return if (user == null) {
            Response.Error(
                code = HttpStatusCode.NotFound,
                data = AuthResponse(
                    errorMessage = "Invalid credentials, no user with this email!"
                )
            )
        } else {
            val hashedPassword = hashPassword(params.password)

            if (user.password == hashedPassword) {
                Response.Success(
                    data = AuthResponse(
                        data = AuthResponseData(
                            id = user.id,
                            name = user.name,
                            bio = user.bio,
                            avatar = user.imageUrl,
                            token = generateToken(params.email),
                            followingCount = user.followingCount,
                            followersCount = user.followersCount
                        )
                    )
                )
            } else {
                Response.Error(
                    code = HttpStatusCode.Forbidden,
                    data = AuthResponse(
                        errorMessage = "Invalid credentials, wrong password!"
                    )
                )
            }
        }
    }

    private suspend fun userAlreadyExist(email: String): Boolean {
        return userDao.findByEmail(email) != null
    }
}













