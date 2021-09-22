package de.renebergelt.mensachemnitz.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import org.koin.android.viewmodel.ext.android.viewModel
import de.renebergelt.mensachemnitz.R
import de.renebergelt.mensachemnitz.databinding.AboutActivityBinding
import de.renebergelt.mensachemnitz.viewmodels.AboutViewModel

class AboutActivity : AppCompatActivity() {

    val viewModel: AboutViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : AboutActivityBinding = DataBindingUtil.setContentView(this,
            R.layout.about_activity
        );

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val toolbar: Toolbar = findViewById<View>(R.id.settings_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
