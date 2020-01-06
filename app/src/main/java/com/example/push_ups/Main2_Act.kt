package com.example.push_ups

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

class Main2_Act : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        Realm.init(this)
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {

                val member = Member()
                member.id = 1
                member.name = "Tom"
                member.age = 25

                realm.copyToRealm(member)
            }
        }

        Realm.getDefaultInstance().use { realm ->

            realm.where(Member::class.java).findAll().forEach {
                Log.d("db", "${it.id}")
                Log.d("db", "${it.name}")
                Log.d("db", "${it.age}")
            }
        }
    }
}

public open class Member: RealmObject() {

    @PrimaryKey
    public open var id: Int = 0

    public open var name: String? = null
    public open var age: Int = 0
}