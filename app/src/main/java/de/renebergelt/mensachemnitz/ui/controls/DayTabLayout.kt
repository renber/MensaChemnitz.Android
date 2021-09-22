package de.renebergelt.mensachemnitz.ui.controls

import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import de.renebergelt.mensachemnitz.R
import de.renebergelt.mensachemnitz.databinding.TabHeaderViewBinding
import de.renebergelt.mensachemnitz.viewmodels.DayViewModel
import de.renebergelt.mensachemnitz.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.main_activity.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class DayTabLayout : TabLayout {

    lateinit var mainViewModel : MainViewModel

    constructor (context : Context)
        : this(context, null, 0) {
        // --
        initViewModel()
    }

    constructor (context : Context, attrs : AttributeSet)
            : this(context, attrs, attrs.styleAttribute) {
        // --
        initViewModel()
    }

    constructor (context : Context, attrs : AttributeSet?, defStyleAttr : Int)
        : super(context, attrs, defStyleAttr)
    {
        initViewModel()

        // --
        this.addOnTabSelectedListener(object : BaseOnTabSelectedListener<Tab> {
            override fun onTabReselected(tab: Tab?) {
            }

            override fun onTabUnselected(tab: Tab?) {
            }

            override fun onTabSelected(tab: Tab?) {

                if (tab == null)
                    mainViewModel.selectedDay.value = null
                else {
                    mainViewModel.selectedDay.value = mainViewModel.days[tab.position]
                }
            }
        })
    }

    fun initViewModel() {
        val activity = getActivity()!!
        mainViewModel = activity.viewModel<MainViewModel>().value
    }

    override fun addTab(tab: Tab, position: Int, setSelected: Boolean) {
        super.addTab(tab, position, setSelected)

        // get our binding context + viewmodel (dirty)

        val activity = getActivity()!!
        val inflater = activity.layoutInflater
        val binding = DataBindingUtil.inflate<TabHeaderViewBinding>(inflater, R.layout.tab_header_view, container, false)

        val dayViewModel = mainViewModel.days[position]

        binding.viewModel = dayViewModel
        binding.lifecycleOwner = activity

        // remove padding of header
        val layout_params = TableLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layout_params.setMargins(0, 0, 0, 10)
        binding.root.setLayoutParams(layout_params)

        tab.setCustomView(binding.root)

        mainViewModel.selectedDay.observe(activity, Observer<DayViewModel?> { d ->
            if (d != null) {
                val idx = mainViewModel.days.indexOf(d)
                if (idx >= 0 && idx != selectedTabPosition) {
                    getTabAt(idx)?.select()
                }
            }
        } )
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