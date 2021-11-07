package com.udacoding.pos.ui.profile.model

import com.google.gson.annotations.SerializedName

data class ResponseProfit(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: Any? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null
)

data class Data(

	@field:SerializedName("total_transaction")
	val totalTransaction: Int? = null,

	@field:SerializedName("total_income")
	val totalIncome: Int? = null
)
