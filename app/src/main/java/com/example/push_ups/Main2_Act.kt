package com.example.push_ups

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main2.*
import java.util.ArrayList


class Main2_Act : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setBarChart()
        Realm.init(this)
    }

    private fun setBarChart() {
        var num = 0
        Realm.getDefaultInstance().use { realm ->
            realm.where(Member::class.java).findAll().forEach {
                val entries = ArrayList<BarEntry>()
                entries.add(BarEntry(it.cnt.toFloat(), num++))

                val barDataSet = BarDataSet(entries, "Cells")

                val labels = ArrayList<String>()
                labels.add(it.id)

                val data = BarData(labels, barDataSet)
                barChart.data = data // set the data and list of lables into chart

                barChart.setDescription("Set Bar Chart Description")  // set the description

                //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                barDataSet.color = resources.getColor(R.color.colorAccent)

                barChart.animateY(5000)

            }
        }
    }
}

