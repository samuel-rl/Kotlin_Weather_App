@file:Suppress("DEPRECATION")

package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor


class MainActivity : AppCompatActivity() {

    val CITY: String = "Your city"
    val API: String = "your openWeather token"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val date = Date()
        val currentDate = SimpleDateFormat("EEEE, d MMM yyyy", Locale.FRENCH);
        val dateTextView: TextView = findViewById(R.id.date)
        dateTextView.text = currentDate.format(date)

        val localisation: TextView = findViewById(R.id.localisation)
        localisation.text = "Lyon"
        val execute = WeatherTask().execute();
    }

    inner class WeatherTask() : AsyncTask<String, Any, String>()
    {

        override fun onPreExecute() {
            super.onPreExecute()
        }


        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val jsonObj = JSONObject(result)
            val main = jsonObj.getJSONObject("main")
            val sys = jsonObj.getJSONObject("sys")
            val wind = jsonObj.getJSONObject("wind")
            val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

            val temperatureTextView: TextView = findViewById(R.id.temperatureTextView)
            temperatureTextView.text = "température : " + floor(main.getDouble("temp")).toString() + "°C"

            val humiditeTextView: TextView = findViewById(R.id.humiditeTextView)
            humiditeTextView.text = "Humidité : " + main.getString("humidity") + "%"

            val ventTextView: TextView = findViewById(R.id.ventTextView)
            ventTextView.text = "Vent : " + floor(wind.getDouble("speed")) + "m/s"

            val bg: RelativeLayout = findViewById(R.id.bg)
            val meteoTextView: TextView = findViewById(R.id.meteoTextView)
            val image:ImageView = findViewById(R.id.imageView)

            val w:String = weather.getString("main")
            if(w == "Rain"){
                bg.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.rain))
                meteoTextView.text ="Rain  "
                image.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.rain))
            }else if(w == "Clouds" || w == "Mist"){
                bg.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.cloudy))
                meteoTextView.text = "Cloudy"
                image.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.cloudy))
            }else{
                bg.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.sunny))
                meteoTextView.text = "Sunny "
                image.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.sunny))
            }
        }
    }

}