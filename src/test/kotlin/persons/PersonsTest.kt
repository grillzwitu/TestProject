package persons

import DbSettings
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
    fun `All persons`() {
        testApplication {
            application { mainModule() }
            val beforeCreate = client.get("/persons")
            Assertions.assertEquals("[]".asJson(), beforeCreate.bodyAsText().asJson())

            createPerson("Soko", 20)

            val afterCreate = client.get("/persons")
            Assertions.assertEquals(
                """[{"id":1,"name":"Soko","age":20}]""".asJson(),
                afterCreate.bodyAsText()?.asJson()
            )
        }
    }

//    @Test
//    fun `Person by ID`() {
//        testApplication {
//            application { mainModule() }
//            val createCall = createPerson("Samson", 12)
//            val id = createCall.bodyAsText()
//
//            val afterCreate = client.get("/persons/$id")
//
//            Assertions.assertEquals(
//                """{"id":1,"name":"Apollo","age":12}""".asJson(),
//                afterCreate.bodyAsText()?.asJson()
//            )
//        }
//    }

    @BeforeEach
    fun cleanup() {
        DbSettings.db
        transaction {
            SchemaUtils.drop(Persons)
        }
    }
}

suspend fun ApplicationTestBuilder.createPerson(name: String, age: Int): HttpResponse {
    return client.post("/persons") {
        header(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
        setBody(listOf("name" to name, "age" to age.toString()).formUrlEncode())
    }
}
