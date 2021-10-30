package com.udacoding.pos.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

fun Context.showAlert(title: String, message: String, cancel: Boolean) {
    AlertDialog.Builder(this).apply {
        setTitle(title)
        setMessage(message)
        setCancelable(cancel)
        setPositiveButton("OK") { _, _ ->

        }.show()
    }
}

fun View.showSnackbar(message: String, duration: Int) {
    Snackbar.make(this, message, duration).show()
}

fun Activity.moveActivity(destination: Class<out Activity>) {
    startActivity(Intent(this, destination))
    finish()
}

fun Activity.openActivity(destination: Class<out Activity>) {
    startActivity(Intent(this, destination))
}

fun Activity.initPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.ACCESS_NETWORK_STATE
            ), 1
        )
    }
}

fun Activity.initPermissionLocation() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), 2
        )
    }
}

fun Activity.initPermissionBluetooth() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN
            ), 3
        )
    }
}

fun RelativeLayout.show() {
    visibility = View.VISIBLE
}

fun RelativeLayout.hide() {
    visibility = View.INVISIBLE
}

fun ProgressBar.show() {
    visibility = View.VISIBLE
}

fun ProgressBar.hide() {
    visibility = View.GONE
}

fun LinearLayout.show() {
    visibility = View.VISIBLE
}

fun LinearLayout.hide() {
    visibility = View.GONE
}

fun Button.show() {
    visibility = View.VISIBLE
}

fun Button.hide() {
    visibility = View.GONE
}

fun CardView.show() {
    visibility = View.VISIBLE
}

fun CardView.hide() {
    visibility = View.GONE
}

fun ImageView.show() {
    visibility = View.VISIBLE
}

fun ImageView.hide() {
    visibility = View.INVISIBLE
}

fun CheckBox.show() {
    visibility = View.VISIBLE
}

fun CheckBox.hide() {
    visibility = View.INVISIBLE
}

fun RecyclerView.show() {
    visibility = View.VISIBLE
}

fun RecyclerView.hide() {
    visibility = View.INVISIBLE
}

