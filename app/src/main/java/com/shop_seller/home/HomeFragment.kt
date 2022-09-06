package com.shop_seller.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shop_seller.R
import com.shop_seller.adapter.ProductAdapter
import com.shop_seller.databinding.FragmentHomeBinding
import com.shop_seller.interfaces.DeleteProduct
import com.shop_seller.model.ProductModel

class HomeFragment : Fragment(), DeleteProduct
{

    private lateinit var binding: FragmentHomeBinding
    private lateinit var productModelList: ArrayList<ProductModel>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var retProduct: DatabaseReference


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)



        initViews()
        initDatabase()
        clickedViews()

    }

    private fun initViews()
    {
        productModelList = ArrayList()
        productAdapter = ProductAdapter(productModelList, this)
    }

    private fun initDatabase()
    {
        auth = FirebaseAuth.getInstance()
        retProduct = FirebaseDatabase.getInstance().reference

        binding.loadingProducts.visibility = View.VISIBLE
        retProduct
            .child("Sellers")
            .child(auth.uid!!)
            .child("Products")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot)
                {
                    productModelList.clear()

                    binding.loadingProducts.visibility = View.GONE
                    if (snapshot.exists())
                    {
                        for (snap in snapshot.children)
                        {
                            val productModel = snap.getValue(ProductModel::class.java)
                            productModelList.add(productModel!!)
                        }

                        binding.rV.adapter = productAdapter
                        binding.rV.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        binding.rV.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
                    }

                    else
                    {
                        binding.loadingProducts.visibility = View.GONE
                        Toast.makeText(requireContext(), "مفيش حاجة علشان أعرضها", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError)
                {
                    binding.loadingProducts.visibility = View.GONE
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })

        retProduct
            .child("Orders")
            .child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    if (snapshot.exists())
                    {
                        binding.fabOrder.setImageResource(R.drawable.ic_numbers_order)
                    }

                    else
                    {
                        binding.fabOrder.setImageResource(R.drawable.ic_order)
                    }
                }

                override fun onCancelled(error: DatabaseError)
                {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun clickedViews()
    {
        binding
            .fabOrder
            .setOnClickListener {

                Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_orderFragment)

            }
    }

    override fun deleteOfProduct(productModel: ProductModel)
    {
        val popupMenu = PopupMenu(requireContext(), requireView())
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {

            when (it.itemId)
            {
                R.id.delete ->
                    retProduct
                        .child("Sellers")
                        .child(auth.uid!!)
                        .child("Products")
                        .child(productModel.randomKey)
                        .removeValue()
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful)
                            {
                                Toast.makeText(requireContext(), "done is deleted", Toast.LENGTH_SHORT).show()
                            }

                            else
                            {
                                Toast.makeText(requireContext(), task.exception!!.message, Toast.LENGTH_SHORT).show()
                            }
                        }

                else -> Toast.makeText(requireContext(), "Coming Soon", Toast.LENGTH_SHORT).show()
            }

            true
        }

        popupMenu.show()
    }
}