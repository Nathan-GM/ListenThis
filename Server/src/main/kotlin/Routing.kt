package dam.nathan

import at.favre.lib.crypto.bcrypt.BCrypt
import dam.nathan.classes.Connection
import dam.nathan.classes.UserDatabase
import dam.nathan.classes.UserSerializable
import dam.nathan.repositories.UserRepository
import dam.nathan.responses.UserResponse
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId

fun Application.configureRouting() {

    /* TODO When the repository is ready & mongo is running, 
        add here the connection class and the userRepository.
    */

    val connection = Connection()
    val repositoryUser = UserRepository(connection)

    routing {
        //Endpoint that will return all users -> TMP function.
        get("/") {
            val users = repositoryUser.getAll()
            val responses = mutableListOf<UserResponse>()

            for (user in users) {
                val response = UserResponse(
                    id = user.id.toString(),
                    name = user.username
                )
                responses.add(response)
            }
            call.respond(HttpStatusCode.OK,responses)
        }

        post("/users") {
            try {
                val user = call.receive<UserSerializable>()
                val existing = repositoryUser.getAll()
                var found = false

                for (existingUser in existing) {
                    if (existingUser.username.equals(user.username)) {
                        call.respond(
                            HttpStatusCode.Conflict
                        )
                        found = true
                        break
                    }
                }

                if (!found) {
                    /* TODO Implement avatar converters here */
                    val cypherPassword = BCrypt.withDefaults().hashToString(12, user.password.toCharArray())
                    /* TODO Check to change the biography value at first to empty */
                    val userDataBase = UserDatabase(
                        id = ObjectId(),
                        username = user.username,
                        password = cypherPassword,
                        biography = "",
                        avatar = user.avatar
                    )

                    val result = repositoryUser.add(userDataBase)
                    if (result == null) {
                        call.respond(HttpStatusCode.InternalServerError)
                    } else {
                        call.respond(
                            HttpStatusCode.Created,
                            UserResponse(id = userDataBase.id.toString(), name = userDataBase.username)
                        )
                    }
                }
            } catch (e: IllegalStateException) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = mapOf("message" to e.localizedMessage)
                )
            } catch (e: JsonConvertException) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = mapOf("message" to e.localizedMessage)
                )
            } catch (e: Exception) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = mapOf("message" to e.localizedMessage)
                )
            }
        }

        // TODO do both put and delete endpoints

        put("/users/{id}") {

        }

        delete("/users/{id}") {

        }
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}
