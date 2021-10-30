package com.udacoding.pos

import android.content.Context
import android.content.SharedPreferences

class SessionManager (private val context: Context) {

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor

    private val PRIVATE_MODE = 0
    private val PREF_NAME = "UDAPOS"

    companion object {
        const val KEY_LOGIN = "is_login"
        const val ID_USER = "id_user"
        const val FULL_NAME = "full_name"
        const val EMAIL_USER = "email"
        const val PHONE_NUMBER = "phone_number"
    }

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun createLoginSession(){
        editor.apply{
            putBoolean(KEY_LOGIN, true)
            commit()
        }

    }

    var id_user: Int
        get() = pref.getInt(ID_USER, 0)
        set(value) {
            editor.apply {
                putInt(ID_USER, value)
                commit()
            }
        }

    var full_name: String
        get() = pref.getString(FULL_NAME, "") ?: ""
        set(value) {
            editor.apply {
                putString(FULL_NAME, value)
                commit()
            }
        }

    var email_user: String
        get() = pref.getString(EMAIL_USER, "") ?: ""
        set(value) {
            editor.apply {
                putString(EMAIL_USER, value)
                commit()
            }
        }

    var phone_number: String
        get() = pref.getString(PHONE_NUMBER, "") ?: ""
        set(value) {
            editor.apply {
                putString(PHONE_NUMBER, value)
                commit()
            }
        }

    val is_login: Boolean
    get() = pref.getBoolean(KEY_LOGIN, false)

    fun logout(){
        editor.apply {
            clear()
            commit()
        }
    }

}