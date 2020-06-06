package udit.programmer.co.weatherapp.Common

import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class Helper {
    companion object {
        var stream: String = ""
    }

    fun GetHttpDataHandler(urlString: String?): String {
        val url = URL(urlString)
        val urlConnection = url.openConnection() as HttpURLConnection
        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
            val r =
                BufferedReader(InputStreamReader(BufferedInputStream(urlConnection.inputStream)))
            val sb = StringBuilder()
            var line = r.readLine()
            while (line != null) {
                sb.append(line)
                line = r.readLine()
            }
            stream = sb.toString()
            urlConnection.disconnect()
        }
        return stream
    }

}