package com.shop_seller.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shop_seller.R
import com.shop_seller.databinding.ItemProductBinding
import com.shop_seller.interfaces.DeleteProduct
import com.shop_seller.model.ProductModel

class ProductAdapter(private var productModel: ArrayList<ProductModel>, private var deleteProduct: DeleteProduct): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder
    {
        return ProductViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int)
    {
        val model: ProductModel = productModel[position]

        Glide
            .with(holder.itemView.context)
            .load(model.image)
            .placeholder(R.drawable.ic_new_product)
            .error(R.drawable.ic_new_product)
            .into(holder.bind.imgItemProduct)
        holder.bind.txtTitleItemProduct.text = model.title
        holder.bind.txtPriceItemProduct.text = model.price + " EGP"

        holder.itemView.setOnClickListener {

            deleteProduct.deleteOfProduct(model)
        }
    }

    override fun getItemCount(): Int
    {
        return productModel.size
    }

    class ProductViewHolder(binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val bind = binding
    }
}