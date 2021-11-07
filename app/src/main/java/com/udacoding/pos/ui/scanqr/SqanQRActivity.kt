package com.udacoding.pos.ui.scanqr

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.udacoding.pos.MainActivity
import com.udacoding.pos.databinding.ActivitySqanQrBinding

private const val CAMERA_REQUEST_CODE = 1

class SqanQRActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySqanQrBinding

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySqanQrBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupPermission()
        initView()

        startScan()

    }

    private fun startScan() {
        codeScanner = CodeScanner(this, binding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                val intent = Intent(this@SqanQRActivity, MainActivity::class.java)
                intent.putExtra("result_scan", it.text)

                runOnUiThread {
                    setResult(Activity.RESULT_OK, intent)
                    finish()
//                    Toast.makeText(this@SqanQRActivity, it.text, Toast.LENGTH_SHORT).show()
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.e("startScan:", "Camera init error: ${it.message}")
                }
            }
        }
    }

    private fun setupPermission() {
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    private fun initView() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"You need camera permission!", Toast.LENGTH_SHORT).show()
                } else {
                    //success
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onStop() {
        super.onStop()
        codeScanner.stopPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }
}