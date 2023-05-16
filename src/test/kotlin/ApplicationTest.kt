
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.server.testing.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.Test


class ApplicationTest {

    @Test
    fun emptyPath() {
        testApplication {
            application { mainModule() }
            val cal = client.get("/")

            assertEquals(HttpStatusCode.OK, cal.status)
        }
    }

    @Test
    fun validValue() {
        testApplication {
            application { mainModule() }

            val cal = client.get("/Jetson")

            assertEquals(HttpStatusCode.OK, cal.status)
            assertEquals(
                """
                {
                    "Person name" : "Jetson"
                }
            """.asJson(), cal.bodyAsText().asJson()
            )
        }
    }
}

//parse json with object mapper for consistency
fun String.asJson() = ObjectMapper().readTree(this)
