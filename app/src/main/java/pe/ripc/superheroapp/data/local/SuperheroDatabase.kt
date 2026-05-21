package pe.ripc.superheroapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SuperheroEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SuperheroDatabase : RoomDatabase() {
    abstract fun superheroDao(): SuperheroDao
}
