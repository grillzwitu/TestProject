package persons

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.personsRouter(personsService: PersonsService) {
    route("/persons") {

        //Create a person
        post {
            with(call) {
                val params = receiveParameters()
                val name = requireNotNull(params["name"])
                val age = params["age"]?.toInt()

                val personId = personsService.create(name, age)
                respond(HttpStatusCode.Created, personId)
            }
        }

        //Get all persons
        get {
            call.respond(personsService.all())
        }
    }
}
