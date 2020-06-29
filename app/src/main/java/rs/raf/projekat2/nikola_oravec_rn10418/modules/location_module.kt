package rs.raf.projekat2.nikola_oravec_rn10418.modules

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import rs.raf.projekat2.nikola_oravec_rn10418.data.datasources.local.LocationDatabase
import rs.raf.projekat2.nikola_oravec_rn10418.data.repositories.LocationRepository
import rs.raf.projekat2.nikola_oravec_rn10418.data.repositories.LocationRepositoryImpl
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.viewmodel.ViewModel


val locationModule = module {

    viewModel { ViewModel(get()) }

    single<LocationRepository> { LocationRepositoryImpl(get()) }

    single { get<LocationDatabase>().getLocationDao() }

}