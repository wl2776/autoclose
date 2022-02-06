package com.wl2776.autoclose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val b_ok: Button = findViewById<Button>(R.id.button_login_ok)
        b_ok.setOnClickListener {
            login()
        }

        val b_cancel: Button = findViewById<Button>(R.id.button_login_cancel)
        b_cancel.setOnClickListener {
            finish()
        }
    }

    private fun login() {
        var log_act = Login()

        val callback = object: Callback<SLAPIResponse> {
            override fun onResponse(call: Call<SLAPIResponse>?, response: Response<SLAPIResponse>?) {
                println("response: body: ${response?.body()} error body ${response?.errorBody()}")
                println(response?.body())

                val rb: SLAPIResponse? = response?.body()
                println("state: " + rb?.state)

                val erb: ResponseBody? = response?.errorBody()
                println("state: " + rb?.state)
            }

            override fun onFailure(call: Call<SLAPIResponse>, t: Throwable) {
                println("call failed")
                println(t)
            }

        }
        log_act.login(callback)
    }
}