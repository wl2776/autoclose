package com.wl2776.autoclose

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

data class CarComponent(var state: Boolean,
                        val view_id: Int,
                        val state_string: String,
                        val state_string_true: String, val state_string_false: String)
{
    override fun toString(): String {
        if (state) {
            return state_string.format(state_string_true)
        }
        return state_string.format(state_string_false)
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var engine: CarComponent
    private lateinit var door: CarComponent
    private lateinit var handbrake: CarComponent
    private lateinit var trunk: CarComponent
    private lateinit var ignitionkey: CarComponent
    private lateinit var alarm: CarComponent

    private var app_code: String = ""
    private var app_token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        engine = CarComponent(false,
            R.id.engine_state,
            getString(R.string.engine),
            getString(R.string.engine_started), getString(R.string.engine_stopped));

        door = CarComponent(false,
            R.id.door_state,
            getString(R.string.doors),
            getString(R.string.doors_open), getString(R.string.doors_closed))

        handbrake = CarComponent(true,
            R.id.handbrake_state,
            getString(R.string.hand_brake),
            getString(R.string.hand_brake_up), getString(R.string.hand_brake_down))

        trunk = CarComponent(false,
            R.id.trunk_state,
            getString(R.string.trunk),
            getString(R.string.trunk_open), getString(R.string.trunk_closed))

        ignitionkey = CarComponent(false,
            R.id.key_state,
            getString(R.string.key),
            getString(R.string.key_inserted), getString(R.string.key_pulled_out))

        alarm = CarComponent(true,
            R.id.alarm_state,
            getString(R.string.alarmed),
            getString(R.string.alarmed_on), getString(R.string.alarmed_off))

        setContentView(R.layout.activity_main)

        updateStatus()

        val b: Button = findViewById<Button>(R.id.cancel_button)
        b.setOnClickListener {
            ignitionkey.state = !ignitionkey.state
            updateStatus()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> {
                var loginResult = Intent(this, LoginActivity::class.java)
                startActivity(loginResult)
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun getStatus() {
        /* get user data
        login, slnet_token -> get user info ("https://developer.starline.ru/json/v3/user/{}/data".format(user_id) ) ->
        * */
    }

    private fun updateStatus() {
        val components: List<CarComponent> = listOf(engine, door, handbrake, trunk, ignitionkey, alarm)
        for (c in components) {
            val tv = findViewById<TextView>(c.view_id)
            tv.text = c.toString()
        }
    }
}