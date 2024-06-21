package instau.ayush.com.util

object Constants {
    const val DEFAULT_PAGE_SIZE: Int = 10
    const val BASE_URL = "http://127.0.0.1:8081/"
    const val POST_IMAGES_FOLDER = "post_images/"
    const val POST_IMAGES_FOLDER_PATH = "build/resources/main/static/$POST_IMAGES_FOLDER"
    const val PROFILE_IMAGES_FOLDER = "profile_images/"
    const val PROFILE_IMAGES_FOLDER_PATH = "build/resources/main/static/$PROFILE_IMAGES_FOLDER"
    const val PAGE_NUMBER_PARAMETER = "page"
    const val PAGE_LIMIT_PARAMETER = "limit"
    const val SUGGESTED_FOLLOWING_LIMIT = 5
    const val UNEXPECTED_ERROR_MESSAGE = "An unexpected error has occurred, try again!"
    const val MISSING_PARAMETERS_ERROR_MESSAGE = "Missing required parameters!"
    const val USER_ID_PARAMETER = "userId"

    //Chat
    const val ENDPOINT_FRIEND_LIST = "/chat/friends-list"
    const val ENDPOINT_CHAT_HISTORY = "/chat/chat-history"
    const val ENDPOINT_CHAT_CONNECT = "/chat/connect"

}