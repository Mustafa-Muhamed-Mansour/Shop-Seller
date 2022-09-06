package com.shop_seller.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shop_seller.databinding.ItemUserOfOrderBinding
import com.shop_seller.interfaces.DeleteUserOfOrder
import com.shop_seller.interfaces.UserOfOrder
import com.shop_seller.model.OrderModel

class UserOfOrderAdapter(private var orderModel: ArrayList<OrderModel>, private var userOfOrder: UserOfOrder, private var deleteUserOfOrder: DeleteUserOfOrder): RecyclerView.Adapter<UserOfOrderAdapter.UserOfOrderViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserOfOrderViewHolder
    {
        return UserOfOrderViewHolder(ItemUserOfOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserOfOrderViewHolder, position: Int)
    {
        val model: OrderModel = orderModel[position]
        holder.bind.txtNameItemUserOfOrder.text = model.name
        holder.bind.txtQuantityItemUserOfOrder.text = "Quantity: " + model.quantityOfProduct
        holder.bind.txtPriceItemUserOfOrder.text = "Price: " + model.priceProduct
        holder.bind.txtPhoneItemUserOfOrder.text = model.phone

        holder
            .itemView
            .setOnClickListener {

                userOfOrder.clickedUserOfOrder(model)

            }

        holder
            .bind
            .imgDeleteItemUserOfOrder
            .setOnClickListener {

                deleteUserOfOrder.deleteUserOfOrder(model)
            }
    }

    override fun getItemCount(): Int
    {
        return orderModel.size
    }


    class UserOfOrderViewHolder(binding: ItemUserOfOrderBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val bind = binding
    }
}