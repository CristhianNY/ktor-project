package optimusfly.data.sermons

import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.text

object SermonEntity: Table<Nothing>("sermon") {
    val id = int("idsermon").primaryKey()
    val sermonContent  = text("sermon_content")
    val userId = int("userUserId")
    val categoryId = int("categoryId")
}