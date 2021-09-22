package de.renebergelt.mensachemnitz.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import de.renebergelt.mensachemnitz.R
import de.renebergelt.mensachemnitz.databinding.MainActivityBinding
import de.renebergelt.mensachemnitz.ui.fragments.MainFragment
import de.renebergelt.mensachemnitz.viewmodels.MainViewModel
import org.koin.android.ext.android.get
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    // use get() to avoid recreating the MainViewModel
    val viewModel: MainViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : MainActivityBinding = DataBindingUtil.setContentView(this,
            R.layout.main_activity
        );

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.adjustSelectedDay()
    }

}
