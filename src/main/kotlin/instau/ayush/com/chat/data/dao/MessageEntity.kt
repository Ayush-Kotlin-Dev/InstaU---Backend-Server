package instau.ayush.com.chat.resource

data class MessageEntity(

    val messageId: Long ,
    val sessionId: Long,
    val textMessage: String,
    val sender: Long,
    val receiver: Long,
    val timestamp:String
)
