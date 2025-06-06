package dam.nathan.models.repositories

import dam.nathan.models.Post
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
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

class PostRepository {

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
    val urlPosts = "${serverLocation}${env["POSTS_ROUTE"]}"

    suspend fun getPosts(): MutableList<Post>? {
        try {
            val response: HttpResponse = client.get(urlPosts) {
                contentType(ContentType.Application.Json)
            }
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

    suspend fun createPost(post: Post, token: String): Post? {
        try {
            val response: HttpResponse = client.post(urlPosts) {
                contentType(ContentType.Application.Json)
                setBody(post)
                bearerAuth(token)
            }
            println(response.status)
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

    suspend fun updatePost(post: Post, token: String): String? {
        try {
            val response: HttpResponse = client.put("${urlPosts}${post.id}") {
                contentType(ContentType.Application.Json)
                setBody(post)
                bearerAuth(token)
            }
            println("UpdatePost result: ${response.status}")
            if (response.status == HttpStatusCode.NoContent) {
                return "ok"
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