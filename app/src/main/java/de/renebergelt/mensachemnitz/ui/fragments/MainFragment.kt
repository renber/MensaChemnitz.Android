package de.renebergelt.mensachemnitz.ui.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomappbar.BottomAppBar
import de.renebergelt.mensachemnitz.R
import de.renebergelt.mensachemnitz.databinding.MainFragmentBinding
import de.renebergelt.mensachemnitz.ui.activities.AboutActivity
import de.renebergelt.mensachemnitz.ui.activities.SettingsActivity
import de.renebergelt.mensachemnitz.viewmodels.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding : MainFragmentBinding = DataBindingUtil.inflate<MainFragmentBinding>(inflater, R.layout.main_fragment, container, false);
        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java);
        binding.viewModel = viewModel;
        binding.lifecycleOwner = activity;

        // ensure that the commands canExecute values are up-to-date
        viewModel.updateCommands()

        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // init app bar
        val activity = (requireActivity() as AppCompatActivity)
        val bottomAppBar = activity.findViewById<BottomAppBar>(R.id.bottom_app_bar);
        activity.setSupportActionBar(bottomAppBar)
        bottomAppBar.replaceMenu(R.menu.bottomappbar_menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.bottomappbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item?.let {
          when (it.itemId) {
              R.id.app_bar_change_location -> viewModel.switchLocationCommand.execute()
              R.id.app_bar_refresh -> viewModel.refreshCommand.execute()
              R.id.app_bar_settings -> startActivity(Intent(context, SettingsActivity::class.java))
              R.id.app_bar_about -> startActivity(Intent(context, AboutActivity::class.java))
          }
        }

        return true
    }
}
