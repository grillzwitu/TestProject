import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database

object DB {
     val db by lazy {
        //DB config
         val host = "localhost" // System.getenv("DB_HOST")
         val port = "5555" // System.getenv("DB_PORT")
         val dbName = "test_web_db" // System.getenv("DB_NAME")
         val dbUser = "test_user" // System.getenv("DB_USER")
         val dbPassword = "test" // System.getenv("DB_PASSWORD")
         Database.connect("jdbc:postgresql://$host:$port/$dbName", driver = "org.postgresql.Driver", user = dbUser, password = dbPassword)
    }
}

object Persons: IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
    val age = integer("age").default(0)
}

data class Person(val id: Int,
                  val name: String,
                  val age: Int)
