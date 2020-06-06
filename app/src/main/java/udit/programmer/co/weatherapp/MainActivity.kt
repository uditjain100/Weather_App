package udit.programmer.co.weatherapp

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.provider.Settings.Secure.isLocationProviderEnabled
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import udit.programmer.co.weatherapp.Common.Helper
import udit.programmer.co.weatherapp.Models.OpenWeatherMap
import java.security.Permission
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
    val locationManager by lazy {
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    val locationRequest by lazy {
        LocationRequest().setInterval(2000)
            .setFastestInterval(2000)
            .setSmallestDisplacement(1f)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
    }
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            for (location in result.locations) {
                latitude_layout.text = location.latitude.toString()
                longitude_layout.text = location.longitude.toString()
            }
        }
    }
    var openWeatherMap = OpenWeatherMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        c_toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.refresh -> {
                    Toast.makeText(this, "Refresh Clicked", Toast.LENGTH_LONG).show()
                    getLatestLocations()
                    true
                }
                else -> false
            }
        }

        stop_btn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(
                    this@MainActivity,
                    "Location Updates Stopped ... ",
                    Toast.LENGTH_LONG
                ).show()
                stopLocationUpdates()
                latitude_layout.text = "***"
                longitude_layout.text = "***"
            }
        })
    }


    override fun onStart() {
        super.onStart()
        requestLocation()
        if (isLocationGranted()) {
            if (IsLocationProviderEnabled()) {
                getLatestLocations()
            } else {
                showDialog()
            }
        } else {
            requestLocation()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
    }

    private inner class GetWeather : AsyncTask<String, Unit, String>() {

        internal var pd = ProgressDialog(this@MainActivity)

        override fun onPreExecute() {
            super.onPreExecute()
            pd.setTitle("Please Wait ... ")
            pd.show()
        }

        override fun doInBackground(vararg params: String?): String {
            return Helper().GetHttpDataHandler(params[0])
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            if (result.contains("Error : Not found city")) {
                pd.dismiss()
                return
            }
            val mType = object : TypeToken<OpenWeatherMap>() {}.type
            openWeatherMap = Gson().fromJson(result, mType)
            pd.dismiss()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            999 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (IsLocationProviderEnabled()) {
                    getLatestLocations()
                } else {
                    showDialog()
                }
            } else {
                Toast.makeText(this, "Permissions not Granted ... ", Toast.LENGTH_LONG).show()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLatestLocations() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun showDialog() {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setMessage("Location Services should be Enabled")
            .setTitle("Location Enabled")
            .setCancelable(false)
            .setPositiveButton("Enable Now", { dialogInterface: DialogInterface?, which: Int ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                dialogInterface?.dismiss()
            }).show()
    }

    private fun IsLocationProviderEnabled(): Boolean {
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
        )
            return true
        return false
    }

    private fun isLocationGranted(): Boolean {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
            return true
        return false
    }

    private fun requestLocation() {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), 999
        )
    }
}