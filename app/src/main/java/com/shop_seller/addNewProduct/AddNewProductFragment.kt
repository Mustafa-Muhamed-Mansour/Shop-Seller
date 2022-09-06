package com.shop_seller.addNewProduct

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.shop_seller.R
import com.shop_seller.common.Validation
import com.shop_seller.databinding.FragmentAddNewProductBinding
import com.shop_seller.model.ProductModel

class AddNewProductFragment : Fragment()
{

    private lateinit var binding: FragmentAddNewProductBinding
    private lateinit var randomKey: String
    private lateinit var auth: FirebaseAuth
    private lateinit var productReference: DatabaseReference
    private lateinit var productRef: StorageReference
    private lateinit var validation: Validation
    private lateinit var someLauncher: ActivityResultLauncher<Intent>
    private lateinit var image: String
    private lateinit var resultURI: Uri


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentAddNewProductBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)


        initView()
        initDatabase()
        clickedView()

    }

    private fun initView()
    {
        validation = Validation()
        someLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        {

            val dataIntent: Intent = it.data!!

            if (it.resultCode == Activity.RESULT_OK && dataIntent.data != null)
            {
                resultURI = dataIntent.data!!
                image = resultURI.toString()
                binding.imgProduct.setImageURI(resultURI)
            }

            else
            {
                Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
            }



        }
    }

    private fun initDatabase()
    {
        randomKey = FirebaseDatabase.getInstance().reference.push().key.toString()
        auth = FirebaseAuth.getInstance()
        productReference = FirebaseDatabase.getInstance().reference
        productRef = FirebaseStorage.getInstance().reference.child("Images").child("Images-Sellers").child("Images-Products").child(randomKey)
    }

    private fun clickedView()
    {
        binding
            .imgProduct
            .setOnClickListener {

                openGallery()

            }

        binding
            .btnAdd
            .setOnClickListener {

                val title = binding.editTitle.text.toString()
                val price = binding.editPrice.text.toString()

                if (TextUtils.isEmpty(title))
                {
                    validation.checkTitleProduct(requireContext(), title)
                    binding.editTitle.requestFocus()
                    return@setOnClickListener
                }

                if (TextUtils.isEmpty(price))
                {
                    validation.checkPriceProduct(requireContext(), price)
                    binding.editPrice.requestFocus()
                    return@setOnClickListener
                }

                else
                {
                    addProduct(image, title, price)
                }
            }

    }

    private fun addProduct(image: String, title: String, price: String)
    {
        binding.loadingAddNewProduct.visibility = View.VISIBLE
        productRef
            .putFile(Uri.parse(image))
            .addOnCompleteListener{

                if (it.isSuccessful)
                {
                    productRef
                        .downloadUrl
                        .addOnSuccessListener { imageUri ->

                            binding.loadingAddNewProduct.visibility = View.GONE

                            val productModel =  ProductModel(auth.uid!!, randomKey, imageUri.toString(), title, price)

                            productReference
                                .child("Sellers")
                                .child(auth.uid!!)
                                .child("Products")
                                .child(randomKey)
                                .setValue(productModel)

                            Toast.makeText(requireContext(), "Done is add new product", Toast.LENGTH_SHORT).show()

                        }.addOnFailureListener{ ex ->

                            binding.loadingAddNewProduct.visibility = View.GONE
                            Toast.makeText(requireContext(), ex.message, Toast.LENGTH_SHORT).show()
                        }
                }

                else
                {
                    binding.loadingAddNewProduct.visibility = View.GONE
                    Toast.makeText(requireContext(), it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun openGallery()
    {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        someLauncher.launch(intent)
    }


}