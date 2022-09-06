package com.shop_seller.interfaces

import com.shop_seller.model.OrderModel

interface UserOfOrder
{
    fun clickedUserOfOrder(orderModel: OrderModel)
}