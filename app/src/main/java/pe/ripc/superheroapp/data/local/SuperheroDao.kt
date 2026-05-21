package pe.ripc.superheroapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SuperheroDao {

    @Query(
        """
        SELECT * FROM superheroes
        WHERE name LIKE '%' || :query || '%'
            OR fullName LIKE '%' || :query || '%'
            OR alterEgos LIKE '%' || :query || '%'
            OR firstAppearance LIKE '%' || :query || '%'
            OR publisher LIKE '%' || :query || '%'
            OR alignment LIKE '%' || :query || '%'
        ORDER BY name
        """
    )
    suspend fun searchSuperheroes(query: String): List<SuperheroEntity>

    @Query("SELECT * FROM superheroes WHERE id = :id LIMIT 1")
    suspend fun getSuperheroById(id: String): SuperheroEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSuperheroes(superheroes: List<SuperheroEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSuperhero(superhero: SuperheroEntity)
}
