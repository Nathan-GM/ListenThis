package dam.nathan.repositories.common

/**
 * Interface that will provide the basic functions for the repositories.
 *
 * @property T Represents the item that will be using
 * @property K Represents the second property of the item, this usually will be an ID
 *
 */
interface IRepository<T,K> {

    /**
     * Function that will get all elements T from the database.
     * @return List of the elements of T
     */
    suspend fun getAll() : List<T>

    /**
     * Function that will get the element T from the database by the id
     *
     * @param id ID that belongs to an item T
     * @return This function can return a T object or null if the id doesn't get a match.
     */
    suspend fun getById(id:K) : T?

    /**
     * Function that will update the item T on the database
     *
     * @param item Item that will be updated on the database.
     */
    suspend fun update(item:T)

    /**
     * Function that will update an item based on the ID
     *
     * @param item Item T that will be getting the update.
     * @param id ID of the item that will be getting the update.
     */
    suspend fun updatebyId(item:T,id:K)

    /**
     * Function that will remove an item T from the database
     * @param item Item that will be removed from the database.
     */
    suspend fun remove(item:T)

    /**
     * Function that will remove an item T from the database based on their ID.
     *
     * @param id ID from the item that will be removed from the database.
     */
    suspend fun removeById(id:K)

    /**
     * Function that will add a new user T at the database
     * @param item Item T that will be added to the database.
     */
    suspend fun add(item:T) : String?
    
}