package instau.ayush.com.dao.events

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object EventTable : Table() {
    val id = long("id").autoIncrement()
    val name = varchar("name", 255)
    val description = text("description")
    val imageUrl = varchar("image_url", 255)
    val dateTime = datetime("date_time")
    val organizer = varchar("organizer", 255)
    val howToJoin = text("how_to_join")
    val additionalInfo = text("additional_info")
    val location = varchar("location", 255)


    override val primaryKey = PrimaryKey(id)
}