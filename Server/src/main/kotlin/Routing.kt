package dam.nathan

import at.favre.lib.crypto.bcrypt.BCrypt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dam.nathan.classes.*
import dam.nathan.repositories.PostRepository
import dam.nathan.repositories.UserRepository
import dam.nathan.responses.UserResponse
import dam.nathan.services.loadAvatar
import dam.nathan.services.saveAvatar
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bson.types.ObjectId
import java.util.*

//TODO Check avatar service, its not working properly

fun Application.configureRouting() {

    val connection = Connection()
    val repositoryUser = UserRepository(connection)
    val repositoryPost = PostRepository(connection)

    val env = dotenv{
        directory = "./"
        filename = "information.env"
    }

    val myRealm = env["JWT_REALM"]
    val secret = env["JWT_SECRET"]
    val issuer = env["JWT_ISSUER"]
    val audience = env["JWT_AUDIENCE"]

    install(Authentication) {
        jwt("jwt-auth") {
            realm = myRealm

            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build())

            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has already expired.")
            }


        }
    }

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
             * Endpoint that will return the user information based on their username
             */
            get("/user/{username}") {
                val username = call.parameters["username"]
                if (username == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    val user = repositoryUser.getByUsername(username)
                    if (user == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        var avatar = ""
                        if (user.avatar != null) {
                            avatar = loadAvatar(user.avatar)
                        }
                        val userResponse = UserSerializable(
                            id = user.id.toString(),
                            username = user.username,
                            password = user.password,
                            avatar = avatar,
                            biography = user.biography,
                        )

                        if (userResponse == null) {
                            call.respond(HttpStatusCode.NotFound)
                        } else {
                            call.respond(
                                HttpStatusCode.OK,
                                userResponse
                            )
                        }
                    }
                }
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
                        var avatarBase64 = ""
                        var id =  ObjectId()
                        if (user.avatar != null && !user.avatar.equals("")) {
                            avatarBase64 = saveAvatar(user.avatar, id)
                        }

                        val cypherPassword = BCrypt.withDefaults().hashToString(12, user.password.toCharArray())
                        val userDataBase = UserDatabase(
                            id = id,
                            username = user.username,
                            password = cypherPassword,
                            biography = user.biography,
                            avatar = avatarBase64
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

            //TODO Add confirmation by token
            put("/{id}") {
                try {
                    val idParameter = call.parameters["id"]!!
                    val userParameter = call.receive<UserSerializable>()
                    if (userParameter == null || idParameter.equals("")) {
                        call.respond(HttpStatusCode.BadRequest)
                    }
                    val id = ObjectId(idParameter)
                    val user = repositoryUser.getById(id)
                    var avatarBase64 = userParameter.avatar

                    if (user == null) {
                        call.respond(HttpStatusCode.NotFound)
                    }

                    println(userParameter)
                    println(user)

                    if (user?.avatar != userParameter.avatar && (userParameter.avatar != null && !user?.avatar.equals(""))) {
                        println("Entra en avatar")
                        avatarBase64 = saveAvatar(userParameter.avatar, id)
                    }
                    println("espues de avatar")


                    val userDB = UserDatabase(
                        id = ObjectId(idParameter),
                        username = userParameter.username,
                        password = userParameter.password,
                        biography = userParameter.biography,
                        avatar = avatarBase64
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

            //TODO add the confirmation by token
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

        post("login") {
            try {
                val user = call.receive<UserSerializable>()
                val userDB = repositoryUser.getByUsername(user.username)
                if (userDB == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                } else {
                    val passBD = BCrypt.verifyer().verify(user.password.toCharArray(), userDB.password)
                    if (passBD.verified) {
                        val token = JWT.create()
                            .withAudience(audience)
                            .withIssuer(issuer)
                            .withClaim("username", user.username)
                            .withExpiresAt(Date(System.currentTimeMillis() + (1*24*60*1000)))
                            .sign(Algorithm.HMAC256(secret))
                        call.respond(hashMapOf("token" to token))
                    } else {
                        call.respond(HttpStatusCode.Unauthorized)
                    }
                }
            } catch (e: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("message" to e.localizedMessage))
            } catch (e: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("message" to e.localizedMessage))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("message" to e.localizedMessage))
            }
        }

        route("posts") {
            get("/") {
                val posts = repositoryPost.getAll()
                val response = mutableListOf<PostsSerializables>()

                for (post in posts) {
                    var media = ""
                    if (post.media != null && post.media.isNotBlank()) {
                        // TODO convert media to Base 64 right here
                        // media = imageToBase64(post.image)
                    }
                    if (media == "" && media.isBlank()) {
                        media = post.media ?: ""
                    }

                    var postComments = mutableListOf<CommentsSerializable>()
                    var postLikes = mutableListOf<LikesSerializable>()

                    for (comment in post.comments) {
                        val commentSerializable = CommentsSerializable(
                            authorId = comment._authorId.toString(),
                            comment = comment.comment,
                            timeOfPost = comment.timeOfPost,
                        )
                        postComments.add(commentSerializable)
                    }

                    for (like in post.likes) {
                        val likeSerializable = LikesSerializable(
                            userId = like._userId.toString()
                        )
                        postLikes.add(likeSerializable)
                    }

                    val postSerializable = PostsSerializables(
                        id = post._id.toString(),
                        author = post.author.toString(),
                        media = media,
                        content = post.content,
                        timestamp = post.timestamp,
                        genre = post.genre.toString(),
                        comments = postComments,
                        likes = postLikes
                    )

                    response.add(postSerializable)

                }
            }

            authenticate("jwt-auth") {
                post("/") {
                    try {
                        val post = call.receive<PostsSerializables>()
                        val principal = call.principal<JWTPrincipal>()

                        val username = principal!!.payload.getClaim("username").asString()
                        val expirationDate = principal.expiresAt?.time?.minus(System.currentTimeMillis().toInt())

                        val userId = repositoryUser.getByUsername(username)!!.id.toString()

                        if (expirationDate != null && expirationDate < 0) {
                            call.respond(HttpStatusCode.Unauthorized)
                        } else {
                            if (!userId.equals(post.author)) {
                                call.respond(HttpStatusCode.Unauthorized)
                            }
                            var mediaFile = ""
                            if (post.media != null && post.media.isNotBlank()) {
                                // TODO Store the media file on a server folder
                            }

                            val postDB = PostsDatabase(
                                _id = ObjectId(),
                                author = ObjectId(post.author),
                                media = mediaFile,
                                content = post.content,
                                timestamp = post.timestamp ?: System.currentTimeMillis(),
                                genre = ObjectId(post.genre),
                                comments = mutableListOf(),
                                likes = mutableListOf()
                            )

                            val result = repositoryPost.add(postDB)
                            if (result == null) {
                                call.respond(HttpStatusCode.InternalServerError)
                            } else {

                                var postComments = mutableListOf<CommentsSerializable>()
                                var postLikes = mutableListOf<LikesSerializable>()

                                for (comment in postDB.comments) {
                                    val commentSerializable = CommentsSerializable(
                                        authorId = comment._authorId.toString(),
                                        comment = comment.comment,
                                        timeOfPost = comment.timeOfPost,
                                    )
                                    postComments.add(commentSerializable)
                                }

                                for (like in postDB.likes) {
                                    val likeSerializable = LikesSerializable(
                                        userId = like._userId.toString()
                                    )
                                    postLikes.add(likeSerializable)
                                }

                                val answer = PostsSerializables(
                                    id = postDB._id.toString(),
                                    author = postDB.author.toString(),
                                    media = postDB.media,
                                    content = post.content,
                                    timestamp = post.timestamp,
                                    genre = postDB.genre.toString(),
                                    comments = postComments,
                                    likes = postLikes
                                )

                                call.respond(
                                    HttpStatusCode.Created,
                                    answer
                                )
                            }
                        }
                    } catch (e: Exception) {
                        call.respond(HttpStatusCode.BadRequest, mapOf("message" to e.localizedMessage))
                    }
                }
            }

            // Static plugin. Try to access `/static/index.html`
            staticResources("/static", "static")
        }
    }


}