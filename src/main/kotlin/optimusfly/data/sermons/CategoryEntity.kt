package optimusfly.data.sermons

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text

object CategoryEntity: Table<Nothing>("Category") {
    val id = int("idCategory").primaryKey()
    val name  = text("name")
}