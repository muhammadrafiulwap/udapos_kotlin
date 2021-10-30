package com.udacoding.pos.ui.customer.model

data class DataCustomer(
    val name: String?,
    val phone: String?,
)

fun responseCustomer(): MutableList<DataCustomer> {

//    val data: MutableList<DataProduk>? = null
    val data = ArrayList<DataCustomer>()


    for (i in 0..10){

        data.add(
            DataCustomer(
                "Skincare",
                "6285217721558"
            )
        )

    }

    return data

}