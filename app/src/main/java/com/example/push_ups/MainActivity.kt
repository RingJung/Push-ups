package com.example.push_ups


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {

    var listener: SensorListener? = null
    var manager: SensorManager? = null
    var playing = false

    var push_Count = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        listener = SensorListener()

        Start_Button.setOnClickListener { view ->
            if (playing == true) {
                manager?.unregisterListener(listener)
                Start_Button.text = "계속"
                playing = !playing
            } else {
                var sensor = manager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)
                var chk = manager?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI)

                if (chk == false) {
                    Push_Count.text = "."
                }
                Start_Button.text = "중지"
                playing = !playing
            }
        }
        Reset_Button.setOnClickListener { view ->
            manager?.unregisterListener(listener)
            push_Count = 0;
            Push_Count.text = "${push_Count}"
            Start_Button.text = "시작"
        }

        Push_Count.setOnClickListener { view ->
            if (playing == true) {
                listener!!.counting_button()
            }
        }
        Result_Button.setOnClickListener {
            startActivity<Main2_Act>()
        }
    }

    inner class SensorListener : SensorEventListener {

        var push_det = false
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
                //textView.text = "물체와의 거리: ${event?.values[0]} cm"

                push_det = !push_det
                if (push_det == true) {
                    push_Count++
                    Push_Count.text = "${push_Count}"
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {


        }

        fun counting_button() {
            push_Count++
            Push_Count.text = "${push_Count}"
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()

        val onlyDate: LocalDateTime = LocalDateTime.now()
        Realm.init(this)

        Log.d("date", onlyDate.toString())

        Realm.getDefaultInstance().use { realm ->

            realm.executeTransaction {

                val member = Member()
                member.id = onlyDate.toString()
                Log.d("data_realm",member.id)
                member.cnt = push_Count.toInt()
                Log.d("data_realm",member.id)

                realm.copyToRealm(member)
            }

        }
    }
}

public open class Member : RealmObject() {

    @PrimaryKey
    public open var id: String = ""
    public open var cnt: Int = 0
}
