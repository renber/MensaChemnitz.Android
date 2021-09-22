package de.renebergelt.mensachemnitz.ui.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import de.renebergelt.mensachemnitz.R
import de.renebergelt.mensachemnitz.viewmodels.SettingsViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class SettingsActivity : AppCompatActivity() {

    val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()

        val toolbar: Toolbar = findViewById<View>(R.id.settings_toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()

        viewModel.updateCacheSize()
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}