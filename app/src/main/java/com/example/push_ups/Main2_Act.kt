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

    val realm = Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setBarChart()
        Realm.init(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun setBarChart() {
        var num = 0
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()
        var barDataSet: BarDataSet


        realm.where(Member::class.java).findAll().forEach {

            entries.add(BarEntry(it.cnt.toFloat(), num++))


            labels.add(it.id)
        }
        barDataSet = BarDataSet(entries, "푸쉬업 횟수")
        val data = BarData(labels, barDataSet)
        barChart.data = data // set the data and list of lables into chart

        barChart.setDescription("시간별 푸시업 수")  // set the description

        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = resources.getColor(R.color.colorAccent)

        barChart.animateY(5000)


    }


}

