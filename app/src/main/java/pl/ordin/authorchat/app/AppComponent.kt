package pl.ordin.authorchat.app

import dagger.Component
import pl.ordin.authorchat.main.MainActivity
import pl.ordin.data.network.http.HttpModule
import javax.inject.Singleton

@Singleton
@Component(modules = [HttpModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
}