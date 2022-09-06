package com.shop_seller.order

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.shop_seller.R
import com.shop_seller.adapter.UserOfOrderAdapter
import com.shop_seller.databinding.BottomSheetOrderBinding
import com.shop_seller.databinding.FragmentOrderBinding
import com.shop_seller.interfaces.DeleteUserOfOrder
import com.shop_seller.interfaces.UserOfOrder
import com.shop_seller.model.OrderModel

class OrderFragment : Fragment(), UserOfOrder, DeleteUserOfOrder
{


    private lateinit var binding: FragmentOrderBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var orderReference: DatabaseReference
    private lateinit var userOfOrderAdapter: UserOfOrderAdapter
    private lateinit var orderModel: ArrayList<OrderModel>
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetOrderBinding: BottomSheetOrderBinding
    private var result: Int?= null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        binding = FragmentOrderBinding.inflate(inflater, container, false)
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
        orderModel = ArrayList()
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetOrderBinding = DataBindingUtil.inflate(LayoutInflater.from(requireContext()), R.layout.bottom_sheet_order, requireActivity().findViewById(R.id.relative_order), false)
        bottomSheetDialog.setContentView(bottomSheetOrderBinding.root)
    }

    private fun initDatabase()
    {
        auth = FirebaseAuth.getInstance()
        orderReference = FirebaseDatabase.getInstance().reference
        binding.loadingOrder.visibility = View.VISIBLE
        orderReference
            .child("Orders")
            .child(auth.uid!!)
            .addValueEventListener(object : ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot)
                {
                    binding.loadingOrder.visibility = View.GONE

                    orderModel.clear()

                    if (snapshot.exists())
                    {
                        for (snap in snapshot.children)
                        {
                            val model = snap.getValue(OrderModel::class.java)
                            orderModel.add(model!!)
                        }

                        userOfOrderAdapter = UserOfOrderAdapter(orderModel, this@OrderFragment, this@OrderFragment)
                        binding.rVOrder.adapter = userOfOrderAdapter
                        binding.rVOrder.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        binding.rVOrder.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
                    }

                    else
                    {
                        binding.loadingOrder.visibility = View.GONE
                        Toast.makeText(requireContext(), "مفيش حاجة علشان أعرضها", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError)
                {
                    binding.loadingOrder.visibility = View.GONE
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun clickedView()
    {
        bottomSheetOrderBinding
            .imgClose
            .setOnClickListener {

                bottomSheetDialog.dismiss()
            }
    }

    @SuppressLint("SetTextI18n")
    override fun clickedUserOfOrder(orderModel: OrderModel)
    {
        Glide
            .with(requireContext())
            .load(orderModel.imageProduct)
            .placeholder(R.drawable.ic_new_product)
            .error(R.drawable.ic_new_product)
            .into(bottomSheetOrderBinding.imgProductOfOrder)
        result = (orderModel.priceProduct.toInt()) * (orderModel.quantityOfProduct.toInt())
        bottomSheetOrderBinding.txtResultProductOfOrder.text = "Total: " + result.toString() + " EGP"

        bottomSheetDialog.show()
    }

    override fun deleteUserOfOrder(orderModel: OrderModel)
    {
        orderReference
            .child("Orders")
            .child(auth.uid!!)
            .child(orderModel.randomKey)
            .removeValue()

        Toast.makeText(requireContext(), "done is deleted", Toast.LENGTH_SHORT).show()

        Navigation.findNavController(requireView()).navigate(R.id.action_orderFragment_to_homeFragment)

    }
}