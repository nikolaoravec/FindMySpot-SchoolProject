package rs.raf.projekat2.nikola_oravec_rn10418.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.LocationFilter

interface LocationRepository {
    fun getAll(): Observable<List<Location>>
    fun insert(location: Location): Completable
    fun updateTitleAndContentById(id:Long,title:String,content:String):Completable
    fun getAllByFilter(locationFilter : LocationFilter): Observable<List<Location>>
    fun delete(id:Long): Completable
}

