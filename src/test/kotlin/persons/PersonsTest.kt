package persons

import DB
import Persons
import asJson
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import mainModule
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PersonsTest {
    @Test
    fun `Create Person`() {
        testApplication {
            application { mainModule() }
            val cal = createPerson("Larry Paige", 45)
            Assertions.assertEquals(HttpStatusCode.Created, cal.status)
        }
    }

    @Test
    fun `All Persons`() {
        testApplication {
            application { mainModule() }

            //initial get all
            val beforeCreate = client.get("/persons")
            Assertions.assertEquals("[]".asJson(), beforeCreate.bodyAsText().asJson())

            //create a person
            createPerson("Soko", 20)

            //get all persons
            val afterCreate = client.get("/persons")
            Assertions.assertEquals(
                """[{"id":1,"name":"Soko","age":20}]""".asJson(),
                afterCreate.bodyAsText().asJson()
            )
        }
    }

    @Test
    fun `Person by ID`() {
        testApplication {
            application { mainModule() }

            //create the person
            val createCall = createPerson("Samson", 12)

            //get the id
            val id = createCall.bodyAsText()

            //get the person by id
            val afterCreate = client.get("/persons/$id")
            Assertions.assertEquals("""{"id":1, "name":"Samson", "age":12}""".asJson(), afterCreate.bodyAsText().asJson())
        }
    }

    @Test
    fun `Update Person`() {
        testApplication {
            application { mainModule() }

            //create the person
            val cal = createPerson("Larry Paige", 45)

            //get the id
            val id = cal.bodyAsText()

            //update the rec
            updatePerson(id, "Larry Gaga", 60)

            //confirm the update
            val afterUpdate = client.get("/persons/$id")
            Assertions.assertEquals("""{"id":1, "name":"Larry Gaga", "age":60}""".asJson(), afterUpdate.bodyAsText().asJson())
        }
    }

    @Test
    fun `Delete Person`() {
        testApplication {
            application { mainModule() }

            //create the person
            val createCall = createPerson("Samson", 12)
            val id = createCall.bodyAsText()

            //delete the person
            client.delete("/persons/$id")

            //confirm deletion
            val afterDelete = client.get("/persons/$id")
            Assertions.assertEquals(HttpStatusCode.NotFound, afterDelete.status)
        }
    }

    //clean up db before each test
    @BeforeEach
    fun cleanup() {
        DB.db
        transaction {
            SchemaUtils.drop(Persons)
        }
    }
}

//create person method
suspend fun ApplicationTestBuilder.createPerson(name: String, age: Int): HttpResponse {
    return client.post("/persons") {
        header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
        setBody(listOf("name" to name, "age" to age.toString()).formUrlEncode())
    }
}

//update person method
suspend fun ApplicationTestBuilder.updatePerson(id: String, name: String, age: Int): HttpResponse {
    return client.put("/persons/$id") {
        header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
        setBody(listOf("name" to name, "age" to age.toString()).formUrlEncode())
    }
}
