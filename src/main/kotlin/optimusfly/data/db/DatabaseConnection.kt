package optimusfly.data.db

import org.ktorm.database.Database

object DatabaseConnection {
    val database = Database.connect(url = "jdbc:mysql://pxukqohrckdfo4ty.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/fchie35hl9k8xp42",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "ajrccsdluf760l8a",
        password = "in7ejte8rftnwlky")
}