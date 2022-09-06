package com.shop_seller.login

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shop_seller.R
import com.shop_seller.common.Validation
import com.shop_seller.databinding.FragmentLoginBinding

class LoginFragment : Fragment()
{

    private lateinit var binding: FragmentLoginBinding
    private lateinit var validation: Validation
    private lateinit var auth: FirebaseAuth
    private lateinit var sellerRef: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        initView()
        initDatabase()
        clickedViews()


    }

    private fun initView()
    {
        validation = Validation()
    }

    private fun initDatabase()
    {
        auth = FirebaseAuth.getInstance()
        sellerRef = FirebaseDatabase.getInstance().reference
    }

    private fun clickedViews()
    {
        binding
            .txtRegisterSeller
            .setOnClickListener {

                Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_registerFragment)
            }

        binding
            .btnLogin
            .setOnClickListener {

                val email = binding.editEmail.text.toString()
                val password = binding.editPassword.text.toString()

                if (TextUtils.isEmpty(email))
                {
                    validation.checkEmail(requireContext(), email)
                    binding.editEmail.requestFocus()
                    return@setOnClickListener
                }

                if (TextUtils.isEmpty(password))
                {
                    validation.checkPassword(requireContext(), password)
                    binding.editPassword.requestFocus()
                    return@setOnClickListener
                }

                else
                {
                    loginSeller(email, password)
                }
            }
    }

    private fun loginSeller(email: String, password: String)
    {
        auth
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful)
                {
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
                }

                else
                {
                    Toast.makeText(requireContext(), it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

}