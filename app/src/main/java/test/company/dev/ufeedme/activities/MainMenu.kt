package test.company.dev.ufeedme.activities

import LoginFragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import test.company.dev.ufeedme.R

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    private fun inflateFirebaseLogin() {
        val manager = getSupportFragmentManager()
        val trans = manager.beginTransaction()
        val fragment = LoginFragment()
        trans.add(R.id.frame, fragment, LoginFragment.FRAGMENT_TAG).addToBackStack("tag")
        trans.commit()
    }
}

