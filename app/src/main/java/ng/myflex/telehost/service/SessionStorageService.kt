package ng.myflex.telehost.service

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ng.myflex.telehost.config.Constant
import java.lang.reflect.Type
import javax.inject.Inject

class SessionStorageService @Inject constructor(
    context: Context,
    private val gson: Gson
) {

    private var preferences: SharedPreferences =
        context.getSharedPreferences(Constant.sessionStorageKey, Context.MODE_PRIVATE)

    fun getInt(key: String): Int = preferences.getInt(key, 0)

    fun getString(key: String): String? = preferences.getString(key, null)

    fun getMap(key: String): Map<String, Any>? {
        val data = preferences.getString(key, null) ?: return null

        val type: Type = object : TypeToken<Map<String, Any>>() {}.type

        return gson.fromJson(data, type)
    }

    fun save(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun saveInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun saveLong(key: String, value: Long) {}

    fun saveBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun saveObject(key: String, value: Any) {
        val content = gson.toJson(value)

        preferences.edit().putString(key, content).apply()
    }

    fun remove(key: String) = preferences.edit().remove(key).apply()
}