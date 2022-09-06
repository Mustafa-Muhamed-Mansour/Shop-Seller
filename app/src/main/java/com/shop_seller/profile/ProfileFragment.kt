package com.shop_seller.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shop_seller.R
import com.shop_seller.databinding.FragmentLoginBinding
import com.shop_seller.databinding.FragmentProfileBinding
import com.shop_seller.model.SellerModel

class ProfileFragment : Fragment()
{

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var retRef: DatabaseReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        initDatabase()
        clickedView()

    }

    private fun initDatabase()
    {
        auth = FirebaseAuth.getInstance()
        retRef = FirebaseDatabase.getInstance().reference
        retRef
            .child("Sellers")
            .child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    if (snapshot.exists())
                    {
                        val model = snapshot.getValue(SellerModel::class.java)

                        Glide
                            .with(requireContext())
                            .load(model!!.image)
                            .placeholder(R.drawable.ic_profile)
                            .error(R.drawable.ic_profile)
                            .into(binding.imgProfile)
                        binding.txtName.text = model.name
                        binding.txtPhone.text = model.phone
                        binding.txtAddress.text = model.address
                    }

                    else
                    {
                        Toast.makeText(requireContext(), "معلش مش عارفين نجيب البيانات بتاعتك حاول مرة آخري", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError)
                {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun clickedView()
    {
        binding
            .btnEdit
            .setOnClickListener {

                Toast.makeText(requireContext(), "Soon", Toast.LENGTH_SHORT).show()
            }

        binding
            .imgBtnLogout
            .setOnClickListener {

                Navigation.findNavController(it).navigate(R.id.action_profileFragment_to_loginFragment)

            }
    }
}