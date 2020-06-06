package udit.programmer.co.weatherapp.Models

import android.app.ProgressDialog
import android.os.AsyncTask
import java.util.*

class OpenWeatherMap {
    var coord: Coord? = null
    var weatherItem: List<WeatherItem>? = null
    var base: String? = null
    var main: Main? = null
    var wind: Wind? = null
    var rain: Rain? = null
    var clouds: Clouds? = null
    var dt: Int = 0
    var sys: Sys? = null
    var id: Int = 0
    var name: String? = null
    var cod: Int = 0

    constructor()
    constructor(
        coord: Coord,
        weatherItem: List<WeatherItem>,
        base: String,
        main: Main,
        wind: Wind,
        rain: Rain,
        sys: Sys,
        id: Int,
        clouds: Clouds,
        dt: Int,
        name: String,
        cod: Int
    ) {
        this.coord = coord
        this.weatherItem = weatherItem
        this.base = base
        this.main = main
        this.wind = wind
        this.rain = rain
        this.sys = sys
        this.id = id
        this.clouds = clouds
        this.dt = dt
        this.name = name
        this.cod = cod
    }
}