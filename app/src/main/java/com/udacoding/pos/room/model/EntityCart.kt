package com.udacoding.pos.room.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class EntityCart (
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "id_user")
    var id_user: Int? = null,

    @ColumnInfo(name = "id_customer")
    var id_customer: Int? = null,

    @ColumnInfo(name = "id_product")
    var id_product: Int? = null,

    @ColumnInfo(name = "name_product")
    var name_product: String? = null,

    @ColumnInfo(name = "price_product")
    var price_product: String? = null,

    @ColumnInfo(name = "image_product")
    var image_product: String? = null,

    @ColumnInfo(name = "qty")
    var qty: Int? = null,

    @ColumnInfo(name = "total_item_price")
    var total_item_price: Double? = null

)