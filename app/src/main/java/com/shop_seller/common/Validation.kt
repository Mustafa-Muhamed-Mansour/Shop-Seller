package com.shop_seller.common

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

class Validation
{

    fun checkImage(context: Context, image: String): String
    {
        if (image == null)
        {
            Toast.makeText(context, "Please enter your image", Toast.LENGTH_SHORT).show()
        }

        return image
    }

    fun checkEmail(context: Context, email: String): String
    {
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
        }

        return email
    }

    fun checkName(context: Context, name: String): String
    {
        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
        }

        return name
    }

    fun checkPassword(context: Context, password: String): String
    {
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(context, "Please enter your password", Toast.LENGTH_SHORT).show()
        }

        return password
    }

    fun checkPhone(context: Context, phone: String): String
    {
        if (TextUtils.isEmpty(phone))
        {
            Toast.makeText(context, "Please enter your phone", Toast.LENGTH_SHORT).show()
        }

        return phone
    }

    fun checkAddress(context: Context, address: String): String
    {
        if (TextUtils.isEmpty(address))
        {
            Toast.makeText(context, "Please enter your shopping address", Toast.LENGTH_SHORT).show()
        }

        return address
    }

    fun checkTitleProduct(context: Context, titleProduct: String): String
    {
        if (TextUtils.isEmpty(titleProduct))
        {
            Toast.makeText(context, "Please enter your title of product", Toast.LENGTH_SHORT).show()
        }

        return titleProduct
    }

    fun checkPriceProduct(context: Context, priceProduct: String): String
    {
        if (TextUtils.isEmpty(priceProduct))
        {
            Toast.makeText(context, "Please enter your price of product", Toast.LENGTH_SHORT).show()
        }

        return priceProduct
    }
}