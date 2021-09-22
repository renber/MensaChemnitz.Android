package de.renebergelt.mensachemnitz.viewmodels

import android.R.string
import androidx.annotation.UiThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.renebergelt.mensachemnitz.commands.ICommand
import de.renebergelt.mensachemnitz.commands.RelayCommand
import de.renebergelt.mensachemnitz.services.IAppSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SettingsViewModel : ViewModel {

    val settings : IAppSettings

    private val _isLoading = MutableLiveData<Boolean>()

    val isLoading : LiveData<Boolean> = _isLoading

    var usedSpaceBytes : Long = 0

    private val _usedSpaceText = MutableLiveData<String>()

    val usedSpaceText : LiveData<String> = _usedSpaceText

    val clearCacheCommand : ICommand = RelayCommand( ::clearCache, ::canClearCache );

    constructor(settings : IAppSettings)
            : super() {
        this.settings = settings
    }

    fun canClearCache() : Boolean {
        return usedSpaceBytes > 0 && !(isLoading.value ?: false)
    }

    fun clearCache() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                clearCacheCommand.updateCanExecute()

                clearCacheAsync()
            } finally {
                updateCacheSize()
            }
        }
    }

    fun updateCacheSize() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                clearCacheCommand.updateCanExecute()
                val size = calculateCacheSize()
                usedSpaceBytes = size
                _usedSpaceText.value = formatBytes(size)
            } finally {
                _isLoading.value = false
                clearCacheCommand.updateCanExecute()
            }
        }
    }

    @UiThread
    suspend fun calculateCacheSize() : Long = withContext(Dispatchers.Default)  {
        settings.calculateCacheSize()
    }

    @UiThread
    suspend fun clearCacheAsync() = withContext(Dispatchers.Default)  {
        settings.clearCache()
    }

    fun formatBytes(byteSize : Long) : String {
        val sizes = arrayOf("B", "KB", "MB", "GB", "TB")
        var len = byteSize.toDouble()
        var order = 0
        while (len >= 1024 && order < sizes.size - 1) {
            order++
            len = len / 1024
        }

        return String.format("%.2f %s", len, sizes[order])
    }
}