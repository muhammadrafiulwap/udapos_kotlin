 package com.udacoding.pos.ui.pdfresult

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.udacoding.pos.MainActivity
import com.udacoding.pos.R
import com.udacoding.pos.databinding.ActivityResultFileBinding
import com.udacoding.pos.utils.moveActivity
import java.io.File

class ResultFileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultFileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultFileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val path = intent.getStringExtra("path")
        val phone = intent.getStringExtra("phone")

        Log.d("onCreate: Phone", phone.toString())
        val uri_file = File(path ?: "")

        binding.buttonHome.setOnClickListener {
            moveActivity(MainActivity::class.java)
        }

        binding.buttonView.setOnClickListener {
            viewFile(uri_file)
        }

        binding.buttonSend.setOnClickListener {
            sendWaWithFile(uri_file, phone ?: "")
        }


    }

    private fun viewFile(uri_file: File) {
        var uri: Uri? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                uri = FileProvider.getUriForFile(
                    this,
                    applicationContext.packageName + ".provider",
                    uri_file
                )
            } else {
                uri = Uri.fromFile(uri_file)
            }
        } catch (e: IllegalStateException) {
            AlertDialog.Builder(this).apply {
                setTitle("Exception")
                setMessage(e.message)
                setPositiveButton("OK") { _, _ -> }.show()
            }
        }

        val intent = Intent(Intent.ACTION_VIEW)
        if (uri.toString().contains(".pdf")) {
            intent.setDataAndType(uri, "application/pdf")
        } else if (uri.toString().contains(".xls")) {
            intent.setDataAndType(uri, "application/vnd.ms-excel")
        } else if (uri.toString().contains(".xlsx")) {
            intent.setDataAndType(
                uri,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            )
        } else if (uri.toString().contains(".doc")) {
            intent.setDataAndType(uri, "application/msword")
        } else if (uri.toString().contains(".docx")) {
            intent.setDataAndType(
                uri,
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            )
        }

        try {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        } catch (e: ActivityNotFoundException) {
//            showToast(this, "Aplikasi untuk membuka file tidak ada di perangkat ini")
            AlertDialog.Builder(this).apply {
                setTitle("Info")
                setMessage(getString(R.string.application_not_found))
                setPositiveButton("OK"){dialog, which ->
                    dialog.dismiss()
                }
            }.show()
        }
    }

    private fun sendWaWithFile(uri_file: File, phone: String) {
        var uri: Uri? = null

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                uri = FileProvider.getUriForFile(
                    this,
                    applicationContext.packageName + ".provider",
                    uri_file
                )
            } else {
                uri = Uri.fromFile(uri_file)
            }
        } catch (e: IllegalStateException) {
            AlertDialog.Builder(this).apply {
                setTitle("Exception")
                setMessage(e.message)
                setPositiveButton("OK") { _, _ -> }.show()
            }
        }

        val i = Intent(Intent.ACTION_SEND)
        i.putExtra(Intent.EXTRA_STREAM, uri)
        i.putExtra("jid","$phone@s.whatsapp.net")
        i.setPackage("com.whatsapp")
        i.setType("application/pdf")

        try {
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)

        } catch (e: ActivityNotFoundException) {
            AlertDialog.Builder(this).apply {
                setTitle("Info")
                setMessage(getString(R.string.application_not_found))
                setPositiveButton("OK"){dialog, which ->
                    dialog.dismiss()
                }
            }.show()
        }
    }
}