package com.example.androidassignment


import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val url = "https://www.jsonkeeper.com/b/6HBE"

        CoroutineScope(Dispatchers.IO).launch {
            val result = postApiCall(url, getApiJsonPayload())
            withContext(Dispatchers.Main) {
                println(result)
            }
        }



        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max= 100
        progressBar.progress = 53





        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun getApiJsonPayload(): String {
        val jsonObject = JSONObject()
        jsonObject.put("id", "chatcmpl-A1uT4cghz9VyNLTveXeTAfOmicYBQ")
        jsonObject.put("object", "chat.completion")
        jsonObject.put("created", 1725018754)
        jsonObject.put("model", "gpt-3.5-turbo-16k-0613")

        val choicesArray = jsonObject.put("choices", JSONObject().apply {
            put("index", 0)
            put("message", JSONObject().apply {
                put("role", "assistant")
                put("content", "{\n  \"titles\": [\n    \"Master Android Development with Step-by-Step Tutorials\",\n    \"Unlock the Secrets of Android Development with Expert Guidance\"\n  ],\n  \"description\": \"Get ready to become an Android development pro...\"}")
            })
        })

        jsonObject.put("usage", JSONObject().apply {
            put("prompt_tokens", 145)
            put("completion_tokens", 89)
            put("total_tokens", 234)
        })

        return jsonObject.toString()
    }



    private fun postApiCall(apiUrl: String, jsonPayload: String): String {
        var result = ""
        val url = URL(apiUrl)
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            urlConnection.requestMethod = "POST"
            urlConnection.doOutput = true
            urlConnection.setRequestProperty("Content-Type", "application/json")
            urlConnection.setRequestProperty("Accept", "application/json")

            val outputStream: OutputStream = urlConnection.outputStream
            outputStream.write(jsonPayload.toByteArray())
            outputStream.flush()
            outputStream.close()

            val responseCode = urlConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = urlConnection.inputStream.bufferedReader()
                result = inputStream.readText()
                inputStream.close()
            } else {
                result = "Error: $responseCode"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            result = "Exception: ${e.message}"
        } finally {
            urlConnection.disconnect()
        }
        return result
    }


}