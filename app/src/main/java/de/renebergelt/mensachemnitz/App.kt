package de.renebergelt.mensachemnitz

import android.app.Application
import de.renebergelt.mensachemnitz.services.*
import de.renebergelt.mensachemnitz.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val appModule = module {

            single<IHolidayService> { SaxonyHolidayService() }
            single<IAppSettings> { SharedPreferencesAppSettings( get() ) }
            single<IMensaMenuProvider> { CachingMenuProvider(get(), get()) }
            single<IImageProvider> { CachingPlateImageCache(get(), get()) }
            single<IViewModelFactory> { DefaultViewModelFactory()}

            // we only want one single MainViewModel instance for the whole lifetime of the app
            single {MainViewModel(get(), get(), get())}

            viewModel { AboutViewModel(get()) }
            viewModel { SettingsViewModel(get()) }
        }

        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }

}