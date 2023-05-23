
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.server.testing.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class ApplicationTest {

    @Test
    fun emptyPath() {
        testApplication {
            application { mainModule() }
            val cal = client.get("/")

            Assertions.assertEquals(HttpStatusCode.OK, cal.status)
        }
    }

    @Test
    fun validValue() {
        testApplication {
            application { mainModule() }


//            val cal = client.get("/Jetson")
//
//
//            assertEquals(HttpStatusCode.OK, cal.status)
//            assertEquals(
//                """
//                {
//                    "Person name" : "Jetson"
//                }
//            """.asJson(), cal.bodyAsText().asJson()
//            )
        }
    }

}

//parse json with object mapper for consistency
fun String.asJson() = ObjectMapper().readTree(this)
