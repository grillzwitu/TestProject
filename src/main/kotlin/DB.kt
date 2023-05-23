import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database

object DB {
    val db by lazy {
        //DB config
        val host = "localhost"
        val port = 5555
        val dbName = "test_web_db"
        val dbUser = "test_user"
        val dbPassword = "test"
        Database.connect("jdbc:postgresql://$host:$port/$dbName", driver = "org.postgresql.Driver",
            user = dbUser, password = dbPassword)
    }
}

object Persons: IntIdTable() {
    val name = varchar("name", 50).uniqueIndex()
    val age = integer("age").default(0)
}

data class Person(val id: Int,
                  val name: String,
                  val age: Int)