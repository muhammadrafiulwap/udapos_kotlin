package com.udacoding.pos.utils

import android.content.Context
import android.os.Environment
import com.udacoding.pos.R
import java.io.File

object Common {

    fun getAppPath(context: Context): String {
        val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), context.resources.getString(
            R.string.app_name))
        if (!dir.exists())
            dir.mkdir()
        return dir.path+File.separator
    }

    fun getStorageDirectory(fileName: String): File {
        if (Environment.getExternalStorageState() == null) {
            val f = File(Environment.getDataDirectory().absolutePath + "/FILENYA/"+fileName)
            if (!f.exists())
                f.mkdirs()
            return f
        } else {
            val f = File(Environment.getExternalStorageDirectory().absolutePath + "/FILENYA/"+fileName)
            if (!f.exists())
                f.mkdirs()
            return f
        }
    }

}