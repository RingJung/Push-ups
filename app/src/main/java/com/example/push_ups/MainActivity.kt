package com.example.push_ups


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var listener : SensorListener? = null
    var manager: SensorManager? = null
    var playing = false

    var push_Count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        listener = SensorListener()

        Start_Button.setOnClickListener{ view ->
            if(playing == true){
                manager?.unregisterListener(listener)
                Start_Button.text = "계속"
                playing = !playing
            }
            else {
                var sensor = manager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)
                var chk = manager?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)

                if (chk == false) {
                    Push_Count.text = "."
                }
                Start_Button.text = "중지"
                playing = !playing
            }
        }
        Reset_Button.setOnClickListener{view->
            manager?.unregisterListener(listener)
            push_Count = 0;
            Push_Count.text = "${push_Count}"
            Start_Button.text = "시작"
        }
    }

    inner class SensorListener : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if(event?.sensor?.type == Sensor.TYPE_PROXIMITY){
                push_Count ++
                if(push_Count % 2 == 0) {
                    Push_Count.text = "${push_Count / 2}"
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {


        }
    }
}
