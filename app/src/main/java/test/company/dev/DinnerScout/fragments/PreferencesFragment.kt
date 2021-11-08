package test.company.dev.DinnerScout.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import test.company.dev.DinnerScout.R
import test.company.dev.DinnerScout.SimpleAdapter
import test.company.dev.DinnerScout.SwipeToDeleteCallback


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
val PREFS_FILENAME = "test.company.dev.DinnerScout.prefs"
var prefs: SharedPreferences? = null

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PreferencesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PreferencesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreferencesFragment : Fragment(), View.OnClickListener {

    private val simpleAdapter = SimpleAdapter(arrayListOf())


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.price1Btn -> Cost = "$"
            R.id.price2Btn -> Cost = "$$"
            R.id.price3Btn -> Cost = "$$$"
            R.id.addCategoryBtn -> simpleAdapter.addItem(categoriesACTV.text.toString())
            R.id.submitButton -> WriteToSharedPrefs()
        }
    }

    private fun WriteToSharedPrefs() {
        var Distance = distanceET.text.toString()
        var Rating = ratingBar.rating.toString()
        var Category = simpleAdapter.getItems().joinToString()


        prefs = activity!!.getSharedPreferences(PREFS_FILENAME, 0)
        val editor = prefs!!.edit()
        editor.putString("Cost", Cost)
        editor.putString("Distance", Distance)
        editor.putString("Rating", Rating)
        editor.putString("Category", Category)
        editor.apply()

    }

    // TODO: Rename and change types of parameters
    private var lat: String? = null
    private var long: String? = null
    private var param1: String? = null
    private var param2: String? = null


    // SharedPreference data
    var Cost = ""


    private lateinit var distanceET: EditText
    private lateinit var price1Btn: Button
    private lateinit var price2Btn: Button
    private lateinit var price3Btn: Button
    private lateinit var submitButton: Button
    private lateinit var addCategoryButton: Button
    private lateinit var ratingBar: RatingBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriesACTV: AutoCompleteTextView

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lat = it.getString("latitude")
            long = it.getString("longitude")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_preferences, container, false)

        price1Btn = view.findViewById(R.id.price1Btn)
        price2Btn = view.findViewById(R.id.price2Btn)
        price3Btn = view.findViewById(R.id.price3Btn)
        submitButton = view.findViewById(R.id.submitButton)
        submitButton.setOnClickListener(this)
        price1Btn.setOnClickListener(this)
        price2Btn.setOnClickListener(this)
        price3Btn.setOnClickListener(this)
        ratingBar = view.findViewById(R.id.ratingBar)
        distanceET = view.findViewById(R.id.distanceEt)
        categoriesACTV = view.findViewById(R.id.categoriesACTV)
        val categories = resources.getStringArray(R.array.categories)
        // Create the adapter and set it to the AutoCompleteTextView
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, categories)
        categoriesACTV.setAdapter(adapter)


        recyclerView = view.findViewById(R.id.categoriesRv)
        recyclerView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = simpleAdapter

        val swipeHandler = object : SwipeToDeleteCallback(activity!!.applicationContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as SimpleAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)


        addCategoryButton = view.findViewById(R.id.addCategoryBtn)
        addCategoryButton.setOnClickListener(this)
        return view
    }


    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PreferencesFragment.
         */
        const val FRAGMENT_TAG = "Preferences_Fragment"

        @JvmStatic
        fun newInstance(lat: String, long: String) :
                PreferencesFragment {
            val args = Bundle()
            args.putString("latitude", lat)
            args.putString("longitude", long)
            val fragment = PreferencesFragment()
            fragment.arguments = args
            return fragment
        }
    }
}

