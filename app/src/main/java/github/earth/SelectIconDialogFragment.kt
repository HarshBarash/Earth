package github.earth

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import github.earth.utils.*

class SelectIconDialogFragment: DialogFragment(), View.OnClickListener {

    private lateinit var ivIcDef        : ImageView
    private lateinit var ivIcPurple     : ImageView
    private lateinit var ivIcPink       : ImageView
    private lateinit var ivIcGreen      : ImageView
    private lateinit var ivIcRed        : ImageView
    private lateinit var ivIcBeige      : ImageView
    private lateinit var ivIcLightPink  : ImageView
    private lateinit var ivIcYellow     : ImageView
    private lateinit var ivIcBlue       : ImageView
    private lateinit var ivIcGray       : ImageView
    private lateinit var ivIcOrange     : ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView : View = inflater.inflate(R.layout.fragment_app_icon_dialog, container)

        ivIcDef         = rootView.findViewById(R.id.ivIcDef)
        ivIcPurple      = rootView.findViewById(R.id.ivIcPurple)
        ivIcPink        = rootView.findViewById(R.id.ivIcPink)
        ivIcGreen       = rootView.findViewById(R.id.ivIcGreen)
        ivIcBlue        = rootView.findViewById(R.id.ivIcBlue)
        ivIcBeige       = rootView.findViewById(R.id.ivIcBeige)
        ivIcLightPink   = rootView.findViewById(R.id.ivIcLightPink)
        ivIcRed         = rootView.findViewById(R.id.ivIcRed)
        ivIcYellow      = rootView.findViewById(R.id.ivIcYellow)
        ivIcGray        = rootView.findViewById(R.id.ivIcGray)
        ivIcOrange      = rootView.findViewById(R.id.ivIcOrange)

        listenSetter()

        return rootView
    }

    override fun onClick(v: View?) {
        //Toast.makeText(requireContext(), R.string.choice, Toast.LENGTH_SHORT).show()
        when (v?.id) {

            R.id.ivIcDef        -> (activity as MainActivity).changeIcon(IC_DEFAULT)
            R.id.ivIcPurple     -> (activity as MainActivity).changeIcon(IC_PURPLE)
            R.id.ivIcPink       -> (activity as MainActivity).changeIcon(IC_PINK)
            R.id.ivIcGreen      -> (activity as MainActivity).changeIcon(IC_GREEN)
            R.id.ivIcBlue       -> (activity as MainActivity).changeIcon(IC_BLUE)
            R.id.ivIcOrange     -> (activity as MainActivity).changeIcon(IC_ORANGE)
            R.id.ivIcGray       -> (activity as MainActivity).changeIcon(IC_GRAY)
            R.id.ivIcYellow     -> (activity as MainActivity).changeIcon(IC_YELLOW)
            R.id.ivIcRed        -> (activity as MainActivity).changeIcon(IC_RED)
            R.id.ivIcLightPink  -> (activity as MainActivity).changeIcon(IC_LIGHT_PINK)
            R.id.ivIcBeige      -> (activity as MainActivity).changeIcon(IC_BEIGE)

            else -> Toast.makeText(requireContext(), "Nothing selected", Toast.LENGTH_SHORT).show()
        }
        dismiss()
    }

    private fun listenSetter() {
        ivIcDef.setOnClickListener(this)
        ivIcPurple.setOnClickListener(this)
        ivIcPink.setOnClickListener(this)
        ivIcGreen.setOnClickListener(this)
        ivIcBlue.setOnClickListener(this)
        ivIcOrange.setOnClickListener(this)
        ivIcGray.setOnClickListener(this)
        ivIcYellow.setOnClickListener(this)
        ivIcRed.setOnClickListener(this)
        ivIcLightPink.setOnClickListener(this)
        ivIcBeige.setOnClickListener(this)

    }

}