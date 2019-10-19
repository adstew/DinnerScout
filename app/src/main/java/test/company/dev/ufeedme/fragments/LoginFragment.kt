
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import test.company.dev.ufeedme.R
import com.google.firebase.auth.FirebaseAuth

import test.company.dev.ufeedme.fragments.SignupFragment




/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 */
class LoginFragment : Fragment(), View.OnClickListener {
    lateinit var mAuth : FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var signUpButton: Button
    private lateinit var loginButton: Button
    private var mListener: OnFragmentInteractionListener? = null
    companion object {
        const val FRAGMENT_TAG = "Login_Fragment"
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        mAuth = FirebaseAuth.getInstance()
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        signUpButton = view.findViewById(R.id.signUpButton)
        loginButton = view.findViewById(R.id.loginButton)
        signUpButton.setOnClickListener(this)
        loginButton.setOnClickListener(this)

        return view
    }

    private fun userLogin() {
        val email = editTextEmail.text.toString().trim { it <= ' ' }
        val password = editTextPassword.text.toString().trim { it <= ' ' }

        if (email.isEmpty()) {
            editTextEmail.error = "Email is required"
            editTextEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Password is required"
            editTextPassword.requestFocus()
            return
        }




        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    // Save Firebase UID for later usage
                    //                        MainMenu.uid = mAuth.getCurrentUser().getUid()
                    //Log.d("intentsvc", "1: " + MainMenu.uid);

                    val manager = activity?.supportFragmentManager
                    val trans = manager?.beginTransaction()
                    trans?.remove(manager?.findFragmentByTag(LoginFragment.FRAGMENT_TAG)!!)
                    trans?.commit()
                } else {
                    Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onClick(view: View) {

        //Auto Minimizes on screen keyboard
        val inputManager = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            activity!!.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

        when (view.id) {
            test.company.dev.ufeedme.R.id.loginButton -> userLogin()
            R.id.signUpButton -> {
                val manager = activity!!.supportFragmentManager
                val trans = manager.beginTransaction()
                val fragment = SignupFragment()
                trans.replace(R.id.frame, fragment, SignupFragment.FRAGMENT_TAG).addToBackStack("tag").commit()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }


}// Required empty public constructor
