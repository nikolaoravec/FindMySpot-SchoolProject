package rs.raf.projekat2.nikola_oravec_rn10418.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.LocationFilter
import rs.raf.projekat2.nikola_oravec_rn10418.data.repositories.LocationRepository
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.contract.Contract
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.states.ChangeLocationState
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.states.LocationsState
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ViewModel(private val locationRepository: LocationRepository) : ViewModel(), Contract.ViewModel {

    override val changeDone: MutableLiveData<ChangeLocationState> = MutableLiveData()
    override val locationsState: MutableLiveData<LocationsState> = MutableLiveData()
    private val subscriptions = CompositeDisposable()
    private val publishSubject: PublishSubject<LocationFilter> = PublishSubject.create()

    init {
        val subscription = publishSubject
            .debounce(200, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .switchMap {
                locationRepository
                    .getAllByFilter(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnError {
                        Timber.e("Error in publish subject")
                        Timber.e(it)
                    }
            }
            .subscribe(
                {
                    locationsState.value = LocationsState.Success(it)
                },
                {
                    locationsState.value = LocationsState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAll() {
        val subscription = locationRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    locationsState.value = LocationsState.Success(it)
                },
                {
                    locationsState.value = LocationsState.Error("Error happened while fetching data from db")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun addLocation(l: Location) {
        val subscription = locationRepository
            .insert(l)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    locationsState.value = LocationsState.Add
                },
                {
                    locationsState.value = LocationsState.Error("Error happened while adding location")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun getAllByFilter(locationFilter: LocationFilter) {
        publishSubject.onNext(locationFilter)
    }

    override fun updateTitleAndContentById(id: Long, title: String, content: String) {
        val subscription = locationRepository
            .updateTitleAndContentById(id, title, content)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    locationsState.value = LocationsState.Update
                },
                {
                    locationsState.value = LocationsState.Error("Error happened while updating location title/content")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }

    override fun delete(id: Long) {
        val subscription = locationRepository
            .delete(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    locationsState.value = LocationsState.Delete
                },
                {
                    locationsState.value = LocationsState.Error("Error happened while deleting location")
                    Timber.e(it)
                }
            )
        subscriptions.add(subscription)
    }
}