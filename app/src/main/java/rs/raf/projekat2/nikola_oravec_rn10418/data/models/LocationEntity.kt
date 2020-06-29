package rs.raf.projekat2.nikola_oravec_rn10418.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val x:Double,
    val y: Double,
    val title :String,
    val content : String,
    val date: Date
)