package com.udacoding.pos.ui.home.model

import com.google.gson.annotations.SerializedName

data class ResponseProduct(

	@field:SerializedName("data")
	val data: List<DataItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: Any? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class DataItem(

	@field:SerializedName("image")
	val image: Any? = null,

	@field:SerializedName("product")
	val product: String? = null,

	@field:SerializedName("is_active")
	val isActive: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("price")
	val price: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("stock")
	val stock: Int? = null
)
