package dam.nathan.repositories.common

/*Interface that provides basic functions for the repositories. */
interface IRepository<T,K> {
    //Get functions
    suspend fun getAll() : List<T>
    suspend fun getById(id:K) : T?

    suspend fun update(item:T)
    suspend fun updatebyId(item:T,id:K)

    suspend fun remove(item:T)
    suspend fun removeById(id:K)

    suspend fun add(item:T)
    
}