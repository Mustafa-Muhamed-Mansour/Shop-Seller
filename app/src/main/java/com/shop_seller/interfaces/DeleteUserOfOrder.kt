package com.shop_seller.interfaces

import com.shop_seller.model.OrderModel

interface DeleteUserOfOrder
{
    fun deleteUserOfOrder(orderModel: OrderModel)
}