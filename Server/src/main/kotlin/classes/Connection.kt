package dam.nathan.classes

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.github.cdimascio.dotenv.dotenv

/**
 * Class that will contain the basic functions to connect to the database
 *
 * @author Nathan Gonzalez Mercado
 */
class Connection() {
    private var client : MongoClient? = null
    val env = dotenv{
        directory = "./"
        filename = "information.env"
    }

    /**
     * Function that will create a connection to the database
     */
    fun connect() {
        if (client == null) {
            client = MongoClient.create(env["CONNECTION"])
        }
    }

    /**
     * Function that will get the database
     *
     * @param name database that we will be looking to get.
     * @return Will return the database if found or null if the database doesn't exist.
     */
    fun getDataBase(name:String) : MongoDatabase? {
        if (client != null) {
            return client!!.getDatabase(name)
        } else {
            return null
        }
    }

    /**
     * Checks if the client is open,
     *
     * @return  If the client is open it will return true, else will return false
     */
    fun isOpen() : Boolean {
        if (client == null) return false else return true
    }

    /**
     * Function that will close the client.
     */
    fun close() {
        client?.close()
    }
}