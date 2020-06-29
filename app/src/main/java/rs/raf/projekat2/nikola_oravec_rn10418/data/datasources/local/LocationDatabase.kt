package rs.raf.projekat2.nikola_oravec_rn10418.data.datasources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import rs.raf.projekat2.nikola_oravec_rn10418.data.datasources.local.converters.DateConverter
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.LocationEntity


@Database(
    entities = [LocationEntity::class],
    version = 1,
    exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class LocationDatabase : RoomDatabase(){
    abstract fun getLocationDao() : LocationDao
}