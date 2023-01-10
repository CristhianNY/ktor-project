package optimusfly.data.db

import org.ktorm.database.Database

object DatabaseConnection {
    val database = Database.connect(url = "jdbc:mysql://qz8si2yulh3i7gl3.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/gkca1ci1l87sdote",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "ngq0tfy3g2ejesr9",
        password = "o3jjbdzgooc49n7y")
}