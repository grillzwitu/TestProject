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

        //Get Person by id
        get("/{id}") {
            with(call) {
                val id = requireNotNull(parameters["id"]).toInt()
                val person = personsService.findById(id)

                if (person == null) {
                    respond(HttpStatusCode.NotFound)
                } else {
                    respond(person)
                }
            }
        }

        //Update
        put("/{id}") {
            with(call) {
                //get id from the url
                val id = requireNotNull(parameters["id"]).toInt()

                //receive body parameters
                val params = receiveParameters()

                val name = requireNotNull(params["name"])
                val age = params["age"]?.toInt()

                //update the service
                personsService.update(id, name, age)

            }
        }


        //Delete Person
        delete("/{id}") {
            with(call) {
                val id = requireNotNull(parameters["id"]).toInt()
                personsService.delete(id)
            }
        }
    }
}
