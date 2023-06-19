package com.example.test_resource_files

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
//    var myButton:Button=Button(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_resource)
//        myButton =(Button)
    }

    fun openActivitryTwo(view: View){
        val intent =Intent(this,activity2::class.java)
        startActivity(intent)
    }
}