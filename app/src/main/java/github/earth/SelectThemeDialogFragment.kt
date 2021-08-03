package github.earth

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import github.earth.utils.*

class SelectThemeDialogFragment: DialogFragment(), View.OnClickListener {

    private lateinit var themeDef        : View
    private lateinit var themePurple     : View
    private lateinit var themePink       : View
    private lateinit var themeGreen      : View
    private lateinit var themeRed        : View
    private lateinit var themeBeige      : View
    private lateinit var themeLightPink  : View
    private lateinit var themeYellow     : View
    private lateinit var themeBlue       : View
    private lateinit var themeLightBlue  : View
    private lateinit var themeGray       : View
    private lateinit var themeOrange     : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView : View = inflater.inflate(R.layout.fragment_theme_dialog, container)

        themeDef        = rootView.findViewById(R.id.themeDef)
        themePurple     = rootView.findViewById(R.id.themePurple)
        themePink       = rootView.findViewById(R.id.themePink)
        themeGreen      = rootView.findViewById(R.id.themeGreen)
        themeBlue       = rootView.findViewById(R.id.themeBlue)
        themeLightBlue  = rootView.findViewById(R.id.themeLightBlue)
        themeBeige      = rootView.findViewById(R.id.themeBeige)
        themeLightPink  = rootView.findViewById(R.id.themeLightPink)
        themeRed        = rootView.findViewById(R.id.themeRed)
        themeYellow     = rootView.findViewById(R.id.themeYellow)
        themeGray       = rootView.findViewById(R.id.themeGray)
        themeOrange     = rootView.findViewById(R.id.themeOrange)

        listenSetter()

        return rootView
    }

    override fun onClick(v: View?) {
        val sp = (activity as MainActivity).getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE)
        val editor = sp.edit()

        Toast.makeText(requireContext(), R.string.choice, Toast.LENGTH_SHORT).show()
        when (v?.id) {
            R.id.themeDef       -> { editor.putString(SETTINGS_THEME, THEME_DEFAULT) }
            R.id.themePurple    -> { editor.putString(SETTINGS_THEME, THEME_PURPLE) }
            R.id.themePink      -> { editor.putString(SETTINGS_THEME, THEME_PINK) }
            R.id.themeGreen     -> { editor.putString(SETTINGS_THEME, THEME_GREEN) }
            R.id.themeBlue      -> { editor.putString(SETTINGS_THEME, THEME_BLUE) }
            R.id.themeLightBlue -> { editor.putString(SETTINGS_THEME, THEME_LIGHT_BLUE) }
            R.id.themeOrange    -> { editor.putString(SETTINGS_THEME, THEME_ORANGE) }
            R.id.themeGray      -> { editor.putString(SETTINGS_THEME, THEME_GRAY) }
            R.id.themeYellow    -> { editor.putString(SETTINGS_THEME, THEME_YELLOW) }
            R.id.themeRed       -> { editor.putString(SETTINGS_THEME, THEME_RED) }
            R.id.themeLightPink -> { editor.putString(SETTINGS_THEME, THEME_LIGHT_PINK) }
            R.id.themeBeige     -> { editor.putString(SETTINGS_THEME, THEME_BEIGE) }
            else -> Toast.makeText(requireContext(), "Nothing selected", Toast.LENGTH_SHORT).show()
        }
        editor.apply()
        dismiss()
        (activity as MainActivity?)?.finish()
        startActivity(Intent(requireContext(),MainActivity::class.java))
    }

    private fun listenSetter() {
        themeDef.setOnClickListener(this)
        themePurple.setOnClickListener(this)
        themePink.setOnClickListener(this)
        themeGreen.setOnClickListener(this)
        themeBlue.setOnClickListener(this)
        themeOrange.setOnClickListener(this)
        themeGray.setOnClickListener(this)
        themeYellow.setOnClickListener(this)
        themeRed.setOnClickListener(this)
        themeLightPink.setOnClickListener(this)
        themeBeige.setOnClickListener(this)

    }

}