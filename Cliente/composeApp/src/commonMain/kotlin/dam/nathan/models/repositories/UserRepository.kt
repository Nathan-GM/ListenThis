package dam.nathan.models.repositories

import dam.nathan.models.User
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.util.Locale


class UserRepository {
    /* Client that will connect to the KTOR server */
    val client = HttpClient() {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
            }
        }
    }

    val env = try {
        dotenv {
            directory = "/assets"
            filename = "information.env"
        }
    } catch (e: Exception) {
        dotenv {
            directory = "./.."
            filename = "information.env"
        }
    }

    val serverLocation = env["SERVER_IP"]
    val urlUsers = "${serverLocation}${env["USERS_ROUTE"]}"
    val urlLogin = "$serverLocation${env["LOGIN_ROUTE"]}"

    suspend fun register(u: User) : HttpStatusCode {
        try {
            val response: HttpResponse = client.post(urlUsers) {
                contentType(ContentType.Application.Json)
                setBody(
                    u
                )
            }
            return response.status
        } catch (e: ConnectException) {
            return HttpStatusCode.RequestTimeout
        }
    }

    suspend fun login(u: User) : String {
        try {
            val response: HttpResponse = client.post(urlLogin) {
                contentType(ContentType.Application.Json)
                setBody(
                    u
                )
            }
            if (response.status == HttpStatusCode.OK) {
                val decoded = Json.decodeFromString<HashMap<String, String>>(response.bodyAsText())
                return decoded["token"].toString()
            } else {
                return ""
            }
        } catch (e: ConnectException) {
            return "timedout"
        }
    }

    suspend fun getUser(u: User): User? {
        try {
            val response: HttpResponse = client.get("${urlUsers}user/{${u.username}") {
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                return Json.decodeFromString<User>(response.bodyAsText())
            } else {
                return null
            }
        } catch (e: ConnectException) {
            return null
        }
    }
}