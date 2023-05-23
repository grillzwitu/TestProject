import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import persons.PersonsServiceDB
import persons.personsRouter


fun main() {
    val port = 8080

    val server = embeddedServer(Netty, port, module = Application::mainModule)

    server.start(wait = true)

}


fun Application.mainModule() {

    //initialize db
    DB.db

    //Create db table
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Persons)
    }

    //Content negotiation, serialization & deserialization
    install(ContentNegotiation) {
        jackson { enable(SerializationFeature.INDENT_OUTPUT) }
    }

    //Routing
    routing {

        trace {
            application.log.debug(it.buildText())
        }

        get {
            context.respond(mapOf("Welcome" to "Ktor Web Project"))
        }

        //passing parameters
//        get("/{name}") {
//            val name = call.parameters["name"]
//            call.respond(mapOf("Person name" to name)) //call can replace context
//        }

        personsRouter(PersonsServiceDB())
    }
}
