package optimusfly.data.whatsapp

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object WhatsappMessageEntity: Table<Nothing>("messages") {
    val id = int("idmessages").primaryKey()
    val idMessage  = varchar("idmessage")
}