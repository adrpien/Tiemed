package com.adrpien.tiemed.presentation.feature_users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adrpien.tiemed.databinding.FragmentLoginBinding
import com.adrpien.tiemed.core.base_fragment.BaseFragment
import com.adrpien.tiemed.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragment() {

    // ViewBinding
    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    // Instance of Firebase Authetication
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val LOGIN_DEBUG = "LOGIN_DEBUG"

    init{
        setHasOptionsMenu(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            // Execute login action
            loginButtonClickListener()
        }
    }

    private fun loginButtonClickListener() {

        val mail = binding.emailEditText.text?.trim().toString()
        val password = binding.passwordEditText.text?.trim().toString()
        firebaseAuth.signInWithEmailAndPassword(mail, password)
            // Show SnackBar if not logged succesfully
            .addOnFailureListener { exc ->
                Snackbar.make(requireView(), "Podano błędny e-mail lub hasło", Snackbar.LENGTH_SHORT).show()
                Log.d(LOGIN_DEBUG, exc.message.toString())
            }
            // Go to main ativity if logged succesfully
            .addOnSuccessListener {
                if(it.user != null) startApp()
            }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun startApp(){
        val intent = Intent(requireContext(), MainActivity::class.java).apply {
            flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }

}