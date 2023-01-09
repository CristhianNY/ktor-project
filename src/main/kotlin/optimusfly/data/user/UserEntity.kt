package optimusfly.data.user

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object UserEntity: Table<Nothing>("user") {
    val id = int("iduser").primaryKey()
    val name  = varchar("name")
    val lastName = varchar("last_name")
    val email = varchar("email")
    val password = varchar("password")
}