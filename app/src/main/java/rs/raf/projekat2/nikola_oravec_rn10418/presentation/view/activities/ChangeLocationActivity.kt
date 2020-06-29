package rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.activities

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_change_location.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import rs.raf.projekat2.nikola_oravec_rn10418.R
import rs.raf.projekat2.nikola_oravec_rn10418.data.models.Location
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.contract.Contract
import rs.raf.projekat2.nikola_oravec_rn10418.presentation.view.states.LocationsState

class ChangeLocationActivity: AppCompatActivity(R.layout.activity_change_location) {

    private val viewModel: Contract.ViewModel by viewModel<rs.raf.projekat2.nikola_oravec_rn10418.presentation.viewmodel.ViewModel>()
    private var location : Location? = null

    companion object{
        const val CHANGE_KEY = "editKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init(){
        parseIntent()
        initListeners()
        initObserver()
    }

    private fun initObserver() {
        viewModel.locationsState.observe(this, Observer {
            when(it) {
                is LocationsState.Update -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }else -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun initListeners(){

        odustaniBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        sacuvajBtn.setOnClickListener {
            if (check()){
                val title = et_title_change.text.toString()
                val content = et_content_change.text.toString()
                viewModel.updateTitleAndContentById(location!!.id, title, content)
            }
        }
    }

    private fun parseIntent() {
        intent?.let {
            location = it.getParcelableExtra(CHANGE_KEY)

            val title : String? = location?.title
            val content : String? = location?.content

            et_title_change.setText(title)
            et_content_change.setText(content)
        }
    }



    private fun check():Boolean{
        if (et_content_change.text.isEmpty() || et_title_change.text.isEmpty()) {
            Toast.makeText(this, "Fields must not be empty!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}