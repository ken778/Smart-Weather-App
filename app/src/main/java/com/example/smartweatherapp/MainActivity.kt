package com.example.smartweatherapp

import android.Manifest
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.*

var CITY: String = "pretoria"
var API: String ="d5762ba56acf2d29530dba0d45471ed9"
val long: String = ""
var latitu : String = ""




class MainActivity : AppCompatActivity() {



    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //location function
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

        //calling api
        apiCall()

    }

    private fun fetchLocation(){
        val task = fusedLocationProviderClient.lastLocation
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
          != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
        task.addOnSuccessListener {
            if(it != null){
                latitu = it.latitude.toString()
                findViewById<TextView>(R.id.address).text = long

                Toast.makeText(applicationContext,"${it.latitude} ${it.longitude}",Toast.LENGTH_SHORT).show()
            }
        }

    }





   private fun apiCall(){
       //request queue
       val queue = Volley.newRequestQueue(this)
       val url = "https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API"

        //request response
       val jsonObjectRequest = JsonObjectRequest(
           Request.Method.GET, url, null,
           Response.Listener{ response->
               //objects
             val main = response.getJSONObject("main")
               val sys = response.getJSONObject("sys")
               val wind = response.getJSONObject("wind")
              // val updatedAt:Long = response.getLong("td")
               val weather = response.getJSONArray("weather").getJSONObject(0)
               val address = response.getString("name")+", "+sys.getString("country")
                   //populating variables
               val humidity = main.getString("humidity")
               val pressure = main.getString("pressure")
               val temp = main.getString("temp")+"°C"
               val tempMin = "Min Temp: "+main.getString("temp_min")+"°C"
               val tempMax = "Max Temp: "+main.getString("temp_max")+"°C"
               val sunrise:Long = sys.getLong("sunrise")
               val sunset:Long = sys.getLong("sunset")
               val windSpeed = wind.getString("speed")
               val weatherDescription = weather.getString("description")

             Log.d("MainActivity", response.toString())


               Log.d("MainActivity",humidity)

               //displaying data


               findViewById<TextView>(R.id.address).text = address
               findViewById<TextView>(R.id.humidity).text = humidity
               findViewById<TextView>(R.id.pressure).text = pressure
               findViewById<TextView>(R.id.temp).text = temp
               findViewById<TextView>(R.id.temp_min).text = tempMin
               findViewById<TextView>(R.id.temp_max).text = tempMax
               findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
               findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
               findViewById<TextView>(R.id.wind).text = windSpeed
               findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
           },
           {
               Log.d("MainActivity", "something went wrong")
           }
       )
       //add the request to the RequestQueue
       queue.add(jsonObjectRequest)




   }




}


