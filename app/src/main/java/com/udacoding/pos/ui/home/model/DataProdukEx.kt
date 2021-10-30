package com.udacoding.pos.ui.home.model

data class DataProduk(
    val produk: String?,
    val desc: String?,
    val stock: String,
    val price: String,
)

fun responseProduk(): MutableList<DataProduk> {

//    val data: MutableList<DataProduk>? = null
    val data = ArrayList<DataProduk>()

    data.add(
        DataProduk(
            "Skincare",
            "Skincare paling baru yang pernah ada di dunia ini",
            "98",
            "500"
        )
    )

    data.add(
        DataProduk(
            "Printer",
            "Printer paling baru yang pernah ada di dunia ini",
            "78",
            "350"
        )
    )

    data.add(
        DataProduk(
            "Komputer",
            "Komputer paling baru yang pernah ada di dunia ini",
            "48",
            "380"
        )
    )

    data.add(
        DataProduk(
            "Laptop",
            "Laptop paling baru yang pernah ada di dunia ini",
            "48",
            "380"
        )
    )

    data.add(
        DataProduk(
            "Printer",
            "Printer paling baru yang pernah ada di dunia ini",
            "78",
            "350"
        )
    )

    data.add(
        DataProduk(
            "Skincare",
            "Skincare paling baru yang pernah ada di dunia ini",
            "98",
            "500"
        )
    )

    data.add(
        DataProduk(
            "Skincare",
            "Skincare paling baru yang pernah ada di dunia ini",
            "98",
            "500"
        )
    )

    data.add(
        DataProduk(
            "Printer",
            "Printer paling baru yang pernah ada di dunia ini",
            "78",
            "350"
        )
    )

    data.add(
        DataProduk(
            "Komputer",
            "Komputer paling baru yang pernah ada di dunia ini",
            "48",
            "380"
        )
    )

    return data

}