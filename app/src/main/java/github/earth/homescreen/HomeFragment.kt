package github.earth.homescreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import github.earth.R
import github.earth.authscreen.LoginActivity
import github.earth.utils.LOG_HOME_FRAGMENT
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController

    private lateinit var btnPlusContent: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_HOME_FRAGMENT, "onCreate called")

        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser == null) {
            val intent_toLogin = Intent (activity, LoginActivity::class.java)
            activity?.startActivity(intent_toLogin)
            activity?.finish()
            //Кто-нибудь, скажите Антону что это здесь не нужно
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(LOG_HOME_FRAGMENT, "onCreate called")
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        btnPlusContent = rootView.findViewById(R.id.btnPlusContent)
        btnPlusContent.setOnClickListener(this)
        // Многоуважаемый Антон, из за синтетки здесь была ошибка
        // Почему? Не знаю, мне насрать, делаю по своему

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v(LOG_HOME_FRAGMENT, "onDestroyView called")

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnPlusContent -> {
                navController.navigate(R.id.action_HomeFragment_to_SharePhotiScreen)
            }
        }
    }

}
