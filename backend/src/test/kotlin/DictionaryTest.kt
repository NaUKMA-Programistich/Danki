import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class DictionaryTest {

    @Test
    fun controllerTest() = testApplication {

        startApplication()

        val response = client.get("/")
    }
}