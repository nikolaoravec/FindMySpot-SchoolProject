package rs.raf.projekat2.nikola_oravec_rn10418.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Location(
    val id : Long,
    val x : Double,
    val y: Double,
    val title :String,
    val content : String,
    val date : Date
) : Parcelable