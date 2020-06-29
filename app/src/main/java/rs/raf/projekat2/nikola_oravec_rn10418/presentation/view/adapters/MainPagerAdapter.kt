package rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.fragments.ListFragment
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.fragments.SavedLocationsFragment


class MainPagerAdapter(
    fragmentManager: FragmentManager,
    private val context: Context
) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private const val ITEM_COUNT = 2
        const val FRAGMENT_1 = 0
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            FRAGMENT_1 -> SavedLocationsFragment()
            else -> ListFragment()
        }
    }

    override fun getCount(): Int {
        return ITEM_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            FRAGMENT_1 -> "Mapa"
            else -> "Lista"
        }
    }

}