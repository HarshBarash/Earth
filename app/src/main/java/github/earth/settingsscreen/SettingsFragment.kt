package github.earth.settingsscreen

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import github.earth.MainActivity
import github.earth.R
import github.earth.R.*
import github.earth.services.ReminderService
import github.earth.utils.*
import java.util.*

class SettingsFragment : Fragment(), View.OnClickListener {

    private val itemLanguages = arrayOf("English", "Russian", "German")
    private val itemThemes = arrayOf("System", "Day", "Night")

    private lateinit var spinLanguages: Spinner
    private lateinit var spinThemes: Spinner
    private lateinit var fltSave: FloatingActionButton
    private lateinit var swtNtf: com.google.android.material.switchmaterial.SwitchMaterial

    private var sLanguage: String? = null
    private var sThemeAction: String? = null
    private var newLanguage: String? = null
    private var sIcon: String? = null
    private var sTheme: String? = null
    private var isNtf: Boolean? = null

    private lateinit var fltClose: FloatingActionButton

    //View themes
    private lateinit var vDefTheme      : View
    private lateinit var vPurpleTheme   : View
    private lateinit var vBeigeTheme    : View
    private lateinit var vGrayTheme     : View
    private lateinit var vLightPinkTheme: View
    private lateinit var vGreenTheme    : View
    private lateinit var vPinkTheme     : View
    private lateinit var vRedTheme      : View
    private lateinit var vYellowTheme   : View
    private lateinit var vOrangeTheme   : View
    private lateinit var vLightBlueTheme: View
    private lateinit var vBlueTheme     : View

    //View icons
    private lateinit var ivIcDef        : ImageView
    private lateinit var ivIcPurple     : ImageView
    private lateinit var ivIcBeige      : ImageView
    private lateinit var ivIcGray       : ImageView
    private lateinit var ivIcLightPink  : ImageView
    private lateinit var ivIcGreen      : ImageView
    private lateinit var ivIcPink       : ImageView
    private lateinit var ivIcRed        : ImageView
    private lateinit var ivIcYellow     : ImageView
    private lateinit var ivIcOrange     : ImageView
    private lateinit var ivIcLightBlue  : ImageView
    private lateinit var ivIcBlue       : ImageView

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

        vDefTheme = view.findViewById(R.id.vDefTheme)
        vPurpleTheme = view.findViewById(R.id.vPurpleTheme)
        vBeigeTheme = view.findViewById(R.id.vBeigeTheme)
        vGrayTheme = view.findViewById(R.id.vGrayTheme)
        vLightPinkTheme = view.findViewById(R.id.vLightPinkTheme)
        vGreenTheme = view.findViewById(R.id.vGreenTheme)
        vPinkTheme = view.findViewById(R.id.vPinkTheme)
        vRedTheme = view.findViewById(R.id.vRedTheme)
        vYellowTheme = view.findViewById(R.id.vYellowTheme)
        vOrangeTheme = view.findViewById(R.id.vOrangeTheme)
        vLightBlueTheme = view.findViewById(R.id.vLightBlueTheme)
        vBlueTheme       = view.findViewById(R.id.vBlueTheme)
        
        ivIcDef         = view.findViewById(R.id.ivIcDef        )
        ivIcPurple      = view.findViewById(R.id.ivIcPurple     )
        ivIcBeige       = view.findViewById(R.id.ivIcBeige      )
        ivIcGray        = view.findViewById(R.id.ivIcGray       )
        ivIcLightPink   = view.findViewById(R.id.ivIcLightPink  )
        ivIcGreen       = view.findViewById(R.id.ivIcGreen      )
        ivIcPink        = view.findViewById(R.id.ivIcPink       )
        ivIcRed         = view.findViewById(R.id.ivIcRed        )
        ivIcYellow      = view.findViewById(R.id.ivIcYellow     )
        ivIcOrange      = view.findViewById(R.id.ivIcOrange     )
        ivIcLightBlue   = view.findViewById(R.id.ivIcLightBlue  )
        //ivIcBlue         = view.findViewById(R.id.ivIcBlue       )

        spinLanguages = view.findViewById(R.id.spinLanguages)
        spinThemes = view.findViewById(R.id.spinTheme)
        fltSave = view.findViewById(R.id.fltSave)
        fltClose = view.findViewById(R.id.fltClose)
        swtNtf = view.findViewById(R.id.swtNtf)

        val adapterThemes = ArrayAdapter(requireContext(), layout.list_item, itemThemes)
        val adapterLang = ArrayAdapter(requireContext(), layout.list_item, itemLanguages)

        spinThemes.adapter = adapterThemes
        spinLanguages.adapter = adapterLang

        listenSetter()
        getUserData()

        fltClose.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_SettingsFragment_to_ProfileFragment)
        }

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
        isNtf = spConfig.getBoolean(SETTINGS_REMIND_SWITCH, true)
        sThemeAction = spConfig.getString(SETTINGS_THEME_ACTION, ACTION_SYSTEM)

        swtNtf.isChecked = isNtf as Boolean

        when (sLanguage) {
            ENGLISH -> spinLanguages.setSelection(0)
            RUSSIAN -> spinLanguages.setSelection(1)
            GERMAN  -> spinLanguages.setSelection(2)
        }

        when (sThemeAction) {
            ACTION_SYSTEM -> spinThemes.setSelection(0)
            ACTION_DAY -> spinThemes.setSelection(1)
            ACTION_NIGHT -> spinThemes.setSelection(2)
        }

        Log.v(LOG_SETTINGS_FRAGMENT, "Theme: $sTheme")


    }

    override fun onClick(v: View?) {
        when(v?.id) {

            R.id.vDefTheme      -> { dialog(TYPE_THEME, THEME_DEFAULT)  }
            R.id.vPurpleTheme   -> { dialog(TYPE_THEME, THEME_PURPLE)   }
            R.id.vBeigeTheme    -> { dialog(TYPE_THEME, THEME_BEIGE)    }
            R.id.vGrayTheme     -> { dialog(TYPE_THEME, THEME_GRAY)     }
            R.id.vLightPinkTheme-> { dialog(TYPE_THEME, THEME_LIGHT_PINK)}
            R.id.vGreenTheme    -> { dialog(TYPE_THEME, THEME_GREEN)    }
            R.id.vPinkTheme     -> { dialog(TYPE_THEME, THEME_PINK)     }
            R.id.vRedTheme      -> { dialog(TYPE_THEME, THEME_RED)      }
            R.id.vYellowTheme   -> { dialog(TYPE_THEME, THEME_YELLOW)   }
            R.id.vOrangeTheme   -> { dialog(TYPE_THEME, THEME_ORANGE)   }
            R.id.vLightBlueTheme-> { dialog(TYPE_THEME, THEME_LIGHT_BLUE)}
            R.id.vBlueTheme     -> { dialog(TYPE_THEME, THEME_BLUE)     }

            R.id.ivIcDef        -> { dialog(TYPE_ICON, IC_DEFAULT)      }
            R.id.ivIcPurple     -> { dialog(TYPE_ICON, IC_PURPLE)       }
            R.id.ivIcBeige      -> { dialog(TYPE_ICON, IC_BEIGE)        }
            R.id.ivIcGray       -> { dialog(TYPE_ICON, IC_GRAY)         }
            R.id.ivIcLightPink  -> { dialog(TYPE_ICON, IC_LIGHT_PINK)   }
            R.id.ivIcGreen      -> { dialog(TYPE_ICON, IC_GREEN)        }
            R.id.ivIcPink       -> { dialog(TYPE_ICON, IC_PINK)         }
            R.id.ivIcRed        -> { dialog(TYPE_ICON, IC_RED)          }
            R.id.ivIcYellow     -> { dialog(TYPE_ICON, IC_YELLOW)       }
            R.id.ivIcOrange     -> { dialog(TYPE_ICON, IC_ORANGE)       }
            //R.id.ivIcLightBlue  -> { dialog(TYPE_ICON, IC_LIGHT_BLUE) }
            R.id.ivIcBlue       -> { dialog(TYPE_ICON, IC_BLUE)         }
        }
    }

    private fun dialog(type: String, selected: String) {

        val message = when(type) {
            TYPE_THEME -> resources.getString(string.u_selected_theme)
            TYPE_ICON -> resources.getString(string.u_selected_icon)
            else -> "bruh"
        }

        Log.v(LOG_SETTINGS_FRAGMENT, "Dialog")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.сonfirmation))
            .setMessage(message)
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
                Log.v(LOG_SETTINGS_FRAGMENT, "NegativeButton pressed")
            }
            .setPositiveButton(resources.getString(R.string.proceed)) { dialog, which ->
                // Respond to positive button press
                if (type == TYPE_THEME) {

                    val sp = (activity as MainActivity).getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE)
                    val editor = sp.edit()
                    editor.putString(SETTINGS_THEME, selected)
                    editor.apply()
                    ReminderService.stopService(requireContext())
                    (activity as MainActivity?)?.finish()
                    startActivity(Intent(requireContext(), MainActivity::class.java))

                } else if (type == TYPE_ICON) {
                    (activity as MainActivity).changeIcon(selected)
                }
            }
            .show()
    }

    private fun listenSetter() {

        vDefTheme.setOnClickListener(this)
        vPurpleTheme.setOnClickListener(this)
        vBeigeTheme.setOnClickListener(this)
        vGrayTheme.setOnClickListener(this)
        vLightPinkTheme.setOnClickListener(this)
        vGreenTheme.setOnClickListener(this)
        vPinkTheme.setOnClickListener(this)
        vRedTheme.setOnClickListener(this)
        vYellowTheme.setOnClickListener(this)
        vOrangeTheme.setOnClickListener(this)
        vLightBlueTheme.setOnClickListener(this)
        vBlueTheme      .setOnClickListener(this)

        ivIcDef.setOnClickListener(this)
        ivIcPurple.setOnClickListener(this)
        ivIcBeige.setOnClickListener(this)
        ivIcGray.setOnClickListener(this)
        ivIcLightPink.setOnClickListener(this)
        ivIcGreen.setOnClickListener(this)
        ivIcPink.setOnClickListener(this)
        ivIcRed.setOnClickListener(this)
        ivIcYellow.setOnClickListener(this)
        ivIcOrange.setOnClickListener(this)
        ivIcLightBlue   .setOnClickListener(this)
        //ivIcBlue

        swtNtf.setOnCheckedChangeListener { buttonView, isChecked ->
            val spConfig = activity?.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE)
            val editor = spConfig?.edit()
            when(isChecked){
                true -> { editor?.putBoolean(SETTINGS_REMIND_SWITCH, true) }
                false -> { editor?.putBoolean(SETTINGS_REMIND_SWITCH, false) }
            }
            editor?.apply()
            (activity as MainActivity).updateService()
            (activity as MainActivity).updateWidgets()
        }

        spinThemes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.v(LOG_SETTINGS_FRAGMENT, "Spinner: " + itemThemes[position])

                val selectedAction =
                    when {
                        itemThemes[position] == itemThemes[0] -> ACTION_SYSTEM
                        itemThemes[position] == itemThemes[1] -> ACTION_DAY
                        itemThemes[position] == itemThemes[2] -> ACTION_NIGHT
                        else -> Log.v(LOG_SETTINGS_FRAGMENT, "Selected action is null!")
                    }

                if (selectedAction == sThemeAction)
                    return
                else {

                    val spConfig = activity?.getSharedPreferences(SETTINGS_FILE, Context.MODE_PRIVATE) ?: return
                    with(spConfig.edit()) {

                        Log.v(LOG_SETTINGS_FRAGMENT, "New Theme Action: $selectedAction")

                        putString(SETTINGS_THEME_ACTION, selectedAction.toString())

                        apply()
                    }
                    reloadActivity()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

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
                    reloadActivity()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }
    }

    private fun reloadActivity() {
        ReminderService.stopService(requireContext())
        (activity as MainActivity?)?.finish()
        startActivity(Intent(requireContext(),MainActivity::class.java))
    }
}