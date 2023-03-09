package optimusfly.data.user

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object PhoneNumberEntity: Table<Nothing>("phone") {
    val id = int("idPhone").primaryKey()
    val phoneNumber  = varchar("phone_number")
    val userId = int("user_iduser")
}