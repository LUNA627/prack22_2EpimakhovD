package com.example.poke

import android.content.Context
import com.google.gson.Gson

class UserPref(private val context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()


    fun saveUser(user: User) {
        val json = gson.toJson(user)
        prefs.edit().putString("saved_user", json).apply()
    }


    fun getSavedUser(): User? {
        val json = prefs.getString("saved_user", null) ?: return null
        return gson.fromJson(json, User::class.java)
    }


    fun isUserRegistered(): Boolean {
        return prefs.contains("saved_user")
    }
}