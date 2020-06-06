package udit.programmer.co.weatherapp.Common

import android.media.Image
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object Common {
    val API_KEY = "9e85de56e43bc0220e766c8203d183ed"
    val API_LINK = "http://api.openweathermap.org/data/2.5/weather"
    fun apiRequest(lat: String, lan: String): String {
        return StringBuilder(API_LINK).append("?lat=${lat}&lon=${lan}&appid=${API_KEY}&units=metric")
            .toString()
    }

    fun unixTimeStampToDateTime(unixTimeStamp: Double): String {
        val date = Date()
        date.time = unixTimeStamp.toLong() * 1000
        return SimpleDateFormat("HH:mm").format(date)
    }

    fun getImage(icon: String): String {
        return "http://openweathermap.org/img/w/$icon.png"
    }

    val dateNow: String
        get() {
            return SimpleDateFormat("dd MM yyyy HH:mm").format(Date())
        }
}