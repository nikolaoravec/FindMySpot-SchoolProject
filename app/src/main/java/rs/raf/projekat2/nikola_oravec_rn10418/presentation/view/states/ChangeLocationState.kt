package rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.states

sealed class ChangeLocationState {
    object Success: ChangeLocationState()
    data class Error(val message: String): ChangeLocationState()
}