package dam.nathan.models.repositories

import dam.nathan.models.User
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.gson.gson
import kotlinx.serialization.json.Json
import java.net.ConnectException
import java.net.SocketTimeoutException


class UserRepository {
    /* Client that will connect to the KTOR server */
    val client = HttpClient() {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 8000
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

    suspend fun followGenre(genreId: String, user: User, token: String) : String {
        try {
            val response : HttpResponse = client.put("${urlUsers}/${user.id}") {
                contentType(ContentType.Application.Json)
                setBody(
                    user
                )
                bearerAuth(token)
            }
            if (response.status == HttpStatusCode.OK) {
                return "ok"
            } else {
                println(response.status)
                return "error"
            }
        } catch (e: ConnectException) {
            return "timedout"
        } catch (e: SocketTimeoutException) {
            return "timedout"
        }
    }

    suspend fun register(u: User): HttpStatusCode {
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

    suspend fun login(u: User): String {
        try {
            println("urlLogin: $urlLogin")
            val response: HttpResponse = client.post(urlLogin) {
                contentType(ContentType.Application.Json)
                setBody(
                    u
                )
            }
            print(response.status)
            if (response.status == HttpStatusCode.OK) {
                val decoded = Json.decodeFromString<HashMap<String, String>>(response.bodyAsText())
                return decoded["token"].toString()
            } else {
                return ""
            }
        } catch (e: ConnectException) {
            print(e.message)
            return "timedout"
        } catch (e: SocketTimeoutException) {
            print(e.message)
            return "timedout"
        }
    }

    suspend fun getUser(u: User): User? {
        try {
            //Used in case the username is separated by spaces
            val userPart = u.username.split(" ")
            var usernameQuery = ""
            var first = true
            var count = 0
            if (userPart.size > 1) {
                for (p in userPart) {
                    if (first) {
                        usernameQuery = p + "%20"
                        count += 1
                        first = false
                    } else {
                        if (count == (userPart.size-1)) {
                            usernameQuery = usernameQuery + p
                        } else {
                            count += 1
                            usernameQuery = usernameQuery + p + "%20"
                        }
                    }
                }
            } else {
                usernameQuery = u.username
            }
            val response: HttpResponse = client.get("${urlUsers}user/${usernameQuery}") {
                contentType(ContentType.Application.Json)
            }
            if (response.status == HttpStatusCode.OK) {
                return Json.decodeFromString<User>(response.bodyAsText())
            } else {
                return null
            }
        } catch (e: ConnectException) {
            return null
        } catch (e: SocketTimeoutException) {
            return null
        }
    }

    suspend fun getUserById(id: String): User? {
        try {
            val response: HttpResponse = client.get("${urlUsers}${id}") {
                contentType(ContentType.Application.Json)
            }
            println(response.status)
            if (response.status == HttpStatusCode.OK) {
                return Json.decodeFromString<User>(response.bodyAsText())
            } else {
                return null
            }
        } catch (e: ConnectException) {
            return null
        } catch (e: SocketTimeoutException) {
            return null
        }
    }

    suspend fun removeAccount(id:String, token:String) : String {
        try {
            val response: HttpResponse = client.delete("${urlUsers}$id") {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
            }
            if (response.status == HttpStatusCode.NoContent) {
                return "ok"
            } else {
                return "error"
            }
        } catch (e: ConnectException) {
            return "error"
        } catch (e: SocketTimeoutException) {
            return "error"
        }
    }

    suspend fun updateAccount(id:String, token: String, newData: User) : String {
        try {
            println("UR id: $id")
            println("UR token: $token")
            val response : HttpResponse = client.put("${urlUsers}$id") {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
                setBody(
                    newData
                )
            }
            if (response.status == HttpStatusCode.OK) {
                println("OK")
                return "ok"
            } else {
                println("NO OK : ${response.status}")
                return "error"
            }
        } catch (e: ConnectException) {
            println("TOUT")
            return "error"
        } catch (e: SocketTimeoutException) {
            println("TOUT")
            return "error"
        }
    }
}