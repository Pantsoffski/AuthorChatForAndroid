package pl.ordin.authorchat.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import pl.ordin.authorchat.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //findNavController(this, R.id.splashFragment).navigate(R.id.loginFragment)
    }

    override fun onSupportNavigateUp() = findNavController(this, R.id.splashFragment).navigateUp()
}
