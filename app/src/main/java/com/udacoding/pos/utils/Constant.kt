package com.udacoding.pos.utils


fun emailPattern() : String {
    return "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
}

fun domain() : String {
    return "http://192.168.11.215/"
}

fun codeCustomer() : Int {
    return 2
}

fun baseURL() : String {
    return "${domain()}udapos_backend/public/api/"
}

fun URLImageCustomer() : String {
    return "${domain()}udapos_backend/public/image/customer/"
}

fun URLImageProduct() : String {
    return "${domain()}udapos_backend/public/image/product/"
}
