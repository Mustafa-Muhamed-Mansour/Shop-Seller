package com.shop_seller.model

class ProductModel
{

    lateinit var id: String
    lateinit var randomKey: String
    lateinit var image: String
    lateinit var title: String
    lateinit var price: String

    constructor()
    {
    }

    constructor(id: String, randomKey: String, image: String, title: String, price: String)
    {
        this.id = id
        this.randomKey = randomKey
        this.image = image
        this.title = title
        this.price = price
    }


}