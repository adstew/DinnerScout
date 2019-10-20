package test.company.dev.DinnerScout.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

import test.company.dev.DinnerScout.R

class SignupFragment : Fragment(),View.OnClickListener {
    companion object {
        val FRAGMENT_TAG = "Singup_Fragment"
        fun newInstance() = SignupFragment()
    }

    private var mListener: OnFragmentInteractionListener? = null
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword:EditText
    private lateinit var signupButton: Button
    internal var progressBar: ProgressBar? = null


    private var mAuth: FirebaseAuth? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup, container, false)

        // Instantiate Firebase Auth object
        mAuth = FirebaseAuth.getInstance()

        // Assign view objects to their respective view ids
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword) as EditText
        signupButton = view.findViewById(R.id.signupButton)

        // Allow signup button to be clickable
        signupButton.setOnClickListener(this)
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    /**
     * Registers users, provides validation, and stores in firestore database.
     */
    private fun registerUser() {
        val email = editTextEmail.text.toString().trim { it <= ' ' }
        val password = editTextPassword.getText().toString().trim({ it <= ' ' })

        if (email.isEmpty()) {
            editTextEmail.error = "Email is required"
            editTextEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required")
            editTextPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            editTextPassword.setError("Minimum length of password should be 6")
            editTextPassword.requestFocus()
            return
        }



        mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "User Registered Successfully", Toast.LENGTH_SHORT).show()

//                MainMenu.uid = mAuth!!.currentUser!!.uid

                val manager = activity?.getSupportFragmentManager()
                val trans = manager?.beginTransaction()
                trans?.remove(manager.findFragmentByTag(FRAGMENT_TAG)!!)
                trans?.commit()
            } else {
                if (task.exception is FirebaseAuthUserCollisionException) {
                    Toast.makeText(context, "User Already Registered", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
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


    override fun onClick(view: View) {

        //Auto Minimizes on screen keyboard
        val inputManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            activity?.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )

        when (view.id) {
            R.id.signupButton -> registerUser()
        }
    }
}
