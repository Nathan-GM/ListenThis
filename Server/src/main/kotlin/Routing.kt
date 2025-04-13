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

    val connection = Connection()
    val repositoryUser = UserRepository(connection)

    routing {
        //Endpoint that will return all users -> TMP function.

        /**
         * Users route, everything related to the users will be placed here.
         */
        route("users") {

            /**
             * Endpoint that will return all the users available on the database
             */
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

            /**
             * Endpoint that will create a new user. If the username's already taken, it will return a conflict error.
             */
            post("/") {
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
                        val userDataBase = UserDatabase(
                            id = ObjectId(),
                            username = user.username,
                            password = cypherPassword,
                            biography = user.biography,
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


            /**
             * Endpoint that will update the date related to a user.
             */
            put("/{id}") {
                try {
                    val idParameter = call.parameters["id"]!!
                    val userParameter = call.receive<UserSerializable>()
                    if (userParameter == null || idParameter.equals("")) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    val id = ObjectId(idParameter)
                    val userDB = UserDatabase(
                        id = ObjectId(idParameter),
                        username = userParameter.username,
                        password = userParameter.password,
                        biography = userParameter.biography,
                        avatar = userParameter.avatar
                    )

                    repositoryUser.updatebyId(userDB, id)
                    call.respond(HttpStatusCode.NoContent)
                }
                catch (e: IllegalStateException) {
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

            /**
             * Endpoint that will delete a user based on the ID.
             */
            delete("/{id}") {
                try {
                    val idParameter = call.parameters["id"]!!
                    if (idParameter == null || idParameter.equals("")) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    val id = ObjectId(idParameter)
                    repositoryUser.removeById(id)
                    call.respond(HttpStatusCode.NoContent)
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

        }
        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}
