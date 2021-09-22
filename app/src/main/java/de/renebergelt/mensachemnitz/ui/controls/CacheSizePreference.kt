package de.renebergelt.mensachemnitz.ui.controls

import android.content.Context
import android.content.ContextWrapper
import android.provider.Settings
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import de.renebergelt.mensachemnitz.R
import de.renebergelt.mensachemnitz.databinding.CacheSizePreferenceBinding
import de.renebergelt.mensachemnitz.databinding.TabHeaderViewBinding
import de.renebergelt.mensachemnitz.viewmodels.SettingsViewModel
import kotlinx.android.synthetic.main.main_activity.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class CacheSizePreference : Preference {

    lateinit var viewModel : SettingsViewModel

    init {
        widgetLayoutResource = R.layout.placeholder_preference
        this.isIconSpaceReserved = false
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)

        val activity = getActivity()!!
        val contentView = holder?.findViewById(R.id.preference_content_placeholder) as ViewGroup

        viewModel = activity.viewModel<SettingsViewModel>().value
        val inflater = activity.layoutInflater
        val binding = DataBindingUtil.inflate<CacheSizePreferenceBinding>(inflater, R.layout.cache_size_preference, contentView, true);

        binding.viewModel = viewModel;
        binding.lifecycleOwner = activity;
    }

    private fun getActivity(): FragmentActivity? {
        var context: Context = context
        while (context is ContextWrapper) {
            if (context is FragmentActivity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }

}