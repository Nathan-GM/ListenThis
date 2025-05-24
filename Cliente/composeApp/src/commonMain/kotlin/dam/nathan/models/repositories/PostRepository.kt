package dam.nathan.models.repositories

import dam.nathan.models.Post
import dam.nathan.models.UserwithToken
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
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

class PostRepository {

    val client = HttpClient() {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
                serializeNulls()
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
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
    val urlPosts = "${serverLocation}${env["POSTS_ROUTE"]}"

    suspend fun getPosts() : MutableList<Post>? {
        try {
            val response : HttpResponse = client.get(urlPosts) {
                contentType(ContentType.Application.Json)
            }
            println(response.status)
            if (response.status == HttpStatusCode.OK) {
                return Json.decodeFromString<MutableList<Post>>(response.bodyAsText())
            } else {
                return null
            }
        } catch (e: ConnectException) {
            return null
        } catch (e: SocketTimeoutException) {
            return null
        }
    }

    suspend fun createPost(post: Post, token: String) : Post?{
        try {
            val response : HttpResponse = client.post(urlPosts) {
                contentType(ContentType.Application.Json)
                setBody(post)
                bearerAuth(token)
            }
            if (response.status == HttpStatusCode.Created) {
                return Json.decodeFromString<Post>(response.bodyAsText())
            } else {
                return null
            }
        } catch (e: ConnectException) {
            return null
        } catch (e: SocketTimeoutException) {
            return null
        }
    }
}