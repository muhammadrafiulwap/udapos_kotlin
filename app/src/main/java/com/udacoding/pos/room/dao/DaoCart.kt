package com.udacoding.pos.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.udacoding.pos.room.model.EntityCart

@Dao
interface DaoCart {

    @Insert
    fun insertCart(entityCart: EntityCart)

    @Query("SELECT * FROM cart")
    fun getCart(): List<EntityCart>

    @Query("SELECT SUM(total_item_price) AS total FROM cart ")
    fun getTotalPay(): Double

    @Query("SELECT * FROM cart WHERE id_product = :id_produk")
    fun getCartByIdProduk(id_produk: Int): List<EntityCart>

    @Query("UPDATE cart SET qty = :qty, total_item_price = :total WHERE id_product = :id_produk")
    fun updateQtyCart(id_produk: Int, qty: Int, total: Double)

    @Query("DELETE FROM cart WHERE id_product = :id_produk")
    fun deleteCart(id_produk: Int)

    @Query("DELETE FROM cart")
    fun deleteCartAll()

}