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

    /*val directoryEnv = if (
        System.getProperty("os.name").lowercase(Locale.getDefault()).contains("Win") ||
        System.getProperty("os.name").lowercase(Locale.getDefault()).contains("nix") ||
        System.getProperty("os.name").lowercase(Locale.getDefault()).contains("nux") ||
        System.getProperty("os.name").lowercase(Locale.getDefault()).contains("mac")
        ) {
            "./.."
        } else {
            "assets/"
    }*/

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

    suspend fun register(u: User) : HttpResponse {
        val response : HttpResponse = client.post(urlUsers) {
            contentType(ContentType.Application.Json)
            setBody(
                u
            )
        }
        return response
    }

    suspend fun login(u: User) : String {
        val response : HttpResponse = client.post(urlLogin) {
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
    }

    suspend fun getUser(u: User): User? {
        val response : HttpResponse = client.get("${urlUsers}user/{${u.username}") {
            contentType(ContentType.Application.Json)
        }
        if (response.status == HttpStatusCode.OK) {
            return Json.decodeFromString<User>(response.bodyAsText())
        } else {
            return null
        }
    }
}