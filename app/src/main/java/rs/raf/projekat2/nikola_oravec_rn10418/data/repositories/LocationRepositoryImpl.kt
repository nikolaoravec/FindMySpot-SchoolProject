package rs.raf.projekat2.nikola_oravec_rn10418.data.repositories

import io.reactivex.Completable
import io.reactivex.Observable
import rs.raf.projekat2.nikola_oravec_rn10418.data.datasources.local.LocationDao
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.LocationEntity
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.LocationFilter

class LocationRepositoryImpl(private val localDataSource: LocationDao) : LocationRepository {

    override fun getAll(): Observable<List<Location>> {
        return localDataSource
            .getAll()
            .map {
                it.map {
                    Location(it.id,it.x, it.y, it.title,it.content,it.date)
                }
            }
    }

    override fun insert(location: Location): Completable {
        val locationEntity = LocationEntity(location.id,location.x, location.y, location.title,location.content,location.date)
        return localDataSource.insert(locationEntity)
    }

    override fun updateTitleAndContentById(id: Long, title: String, content: String): Completable {
        return localDataSource.updateTitleAndContentById(id, title, content)
    }

    override fun getAllByFilter(locationFilter: LocationFilter): Observable<List<Location>> {
        if(locationFilter.isDateDesc == true){
            return localDataSource
                .getByFilterDesc(locationFilter.titleContent)
                .map {
                    it.map {
                        Location(it.id,it.x, it.y, it.title,it.content,it.date)
                    }
                }
        }else{
            return localDataSource
                .getByFilterAsc(locationFilter.titleContent)
                .map {
                    it.map {
                        Location(it.id,it.x, it.y, it.title,it.content,it.date)
                    }
                }
        }
    }

    override fun delete(id: Long): Completable {
        return localDataSource.delete(id)
    }
}
