package github.earth.settingsscreen

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.res.ResourcesCompat.*
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import github.earth.MainActivity
import github.earth.R
import github.earth.R.*
import github.earth.R.color.*
import github.earth.R.mipmap.*
import github.earth.SelectIconDialogFragment
import github.earth.SelectThemeDialogFragment
import github.earth.authscreen.LoginActivity
import github.earth.models.User
import github.earth.utils.*
import github.earth.views.PasswordDialog
import java.util.*

class SettingsFragment : Fragment(), View.OnClickListener {

    private val itemLanguages = arrayOf("English", "Russian", "German")

    private lateinit var spinLanguages: Spinner
    private lateinit var ivSelectedIcon: ImageView
    private lateinit var fltSave: FloatingActionButton
    private lateinit var clOtherColors: ConstraintLayout
    private lateinit var vSelectedTheme: View

    private var sLanguage: String? = null
    private var newLanguage: String? = null
    private var sIcon: String? = null
    private var sTheme: String? = null

    private lateinit var fltClose: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(LOG_SETTINGS_FRAGMENT, "onCreate called")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(LOG_SETTINGS_FRAGMENT, "onCreateView called")
        val view = inflater.inflate(layout.fragment_settings, container, false)

        ivSelectedIcon = view.findViewById(R.id.ivSelectedIcon)
        spinLanguages = view.findViewById(R.id.spinLanguages)
        fltSave = view.findViewById(R.id.fltSave)
        clOtherColors = view.findViewById(R.id.clOtherColors)
        vSelectedTheme = view.findViewById(R.id.vSelectedTheme)
        fltClose = view.findViewById(R.id.fltClose)

        val adapter = ArrayAdapter(requireContext(), layout.list_item, itemLanguages)
        spinLanguages.adapter = adapter

        listenSetter()
        getUserData()

        return view
    }

    override fun onStart() {
        super.onStart()
        Log.v(LOG_SETTINGS_FRAGMENT, "onStart called")

    }

    private fun getUserData() {

        val spConfig = activity?.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE) ?: return
        sLanguage = spConfig.getString(SETTINGS_LANGUAGE, Locale.getDefault().displayLanguage.toString())
        sIcon = spConfig.getString(SETTINGS_APP_ICON, IC_DEFAULT)
        sTheme = spConfig.getString(SETTINGS_THEME, THEME_DEFAULT)

        when (sLanguage) {
            ENGLISH -> spinLanguages.setSelection(0)
            RUSSIAN -> spinLanguages.setSelection(1)
            GERMAN  -> spinLanguages.setSelection(2)
        }

        Log.v(LOG_SETTINGS_FRAGMENT, "Theme: $sTheme")

        if (sTheme != THEME_DEFAULT)
            vSelectedTheme.background = getDrawable(resources, drawable.circle, null)

        when (sTheme) {
            THEME_DEFAULT       -> {vSelectedTheme.background = getDrawable(resources, drawable.ic_earth, null)
            }
            THEME_GREEN         -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_green))}
            THEME_PURPLE        -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_purple))}
            THEME_BEIGE         -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_beige))}
            THEME_ORANGE        -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_orange))}
            THEME_YELLOW        -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_yellow))}
            THEME_RED           -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_red))}
            THEME_GRAY          -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_gray))}
            THEME_PINK          -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_pink))}
            THEME_LIGHT_PINK    -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_light_pink))}
            THEME_BLUE          -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_blue))}
            THEME_LIGHT_BLUE    -> {vSelectedTheme.background.setTint(getColor(requireContext(), accent_light_blue))}
        }

        when(sIcon) {
            IC_DEFAULT      -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher,null))
            IC_PURPLE       -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher_purple,null))
            IC_BEIGE        -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher_beige,null))
            IC_GRAY         -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher_gray,null))
            IC_PINK         -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher_pink,null))
            IC_LIGHT_PINK   -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher_light_pink,null))
            IC_RED          -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher_red,null))
            IC_YELLOW       -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher_yellow,null))
            IC_ORANGE       -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher_orange,null))
            IC_GREEN        -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher_green,null))
            IC_BLUE         -> ivSelectedIcon.setImageDrawable(getDrawable(resources, ic_launcher_blue,null))
        }

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            /*R.id.fltSave ->         {
                saveUserSettings()
            }*/
            R.id.fltClose -> {
                (activity as MainActivity).supportFragmentManager.popBackStack()
            }
            R.id.ivSelectedIcon ->  {
                var dialog = SelectIconDialogFragment()
                dialog.show((activity as MainActivity).supportFragmentManager, "customDialog")

            }
            R.id.clOtherColors ->   {
                var dialog = SelectThemeDialogFragment()
                dialog.show((activity as MainActivity).supportFragmentManager, "customDialog")
                clOtherColors.startAnimation(
                    AnimationUtils.loadAnimation(
                    requireContext(), anim.colors_rotation))
            }
        }
    }

    private fun listenSetter() {

        fltClose.setOnClickListener(this)
        fltSave.setOnClickListener(this)
        ivSelectedIcon.setOnClickListener(this)
        clOtherColors.setOnClickListener(this)

        spinLanguages.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.v(LOG_SETTINGS_FRAGMENT, "Spinner: " + itemLanguages[position])

                val selectedLang =
                    when {
                        itemLanguages[position] == itemLanguages[0] -> ENGLISH
                        itemLanguages[position] == itemLanguages[1] -> RUSSIAN
                        itemLanguages[position] == itemLanguages[2] -> GERMAN
                        else -> Log.v(LOG_SETTINGS_FRAGMENT, "Selected Language is null!")
                    }

                Log.v(LOG_SETTINGS_FRAGMENT, "Selected Language: $selectedLang\n sLanguage: $sLanguage")

                if (selectedLang == sLanguage)
                    return
                else {
                    newLanguage = selectedLang.toString()

                    val spConfig = activity?.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE) ?: return
                    with(spConfig.edit()) {

                        Log.v(LOG_SETTINGS_FRAGMENT, "New Language: $newLanguage")

                        if (newLanguage != null)
                            putString(SETTINGS_LANGUAGE, newLanguage)

                        apply()
                    }

                    (activity as MainActivity?)?.finish()
                    startActivity(Intent(requireContext(),MainActivity::class.java))
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }
}