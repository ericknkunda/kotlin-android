package com.example.test_resource_files


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import java.io.File

class MainActivity : AppCompatActivity() {
//    var myButton:Button=Button(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_resource)
//        myButton =(Button)
    var framelayout =findViewById<FrameLayout>(R.id.fragment_frame)


    }

    fun openActivitryTwo(view: View){
        val intent =Intent(this,activity2::class.java)
        startActivity(intent)
    }

    fun openFirstFragment(view: View){
        val fragmentInstance =eacMessage()
        val layout =R.id.fragment_frame
        openFragmentOne(fragmentInstance, layout)
    }

    fun openSecondFragment(view: View){
        val fragment =ClickMeFragment()
        val layoutId =R.id.fragment_frame

        loadFragmentTwo(fragment, layoutId)
    }
    fun openFragmentOne(fragment:Fragment, layoutId:Int){
        var manager: FragmentManager =supportFragmentManager
        var transaction: FragmentTransaction =manager.beginTransaction()
        transaction.replace(layoutId, fragment)
        transaction.commit()

    }

    fun loadFragmentTwo(fragment: Fragment, layoutId: Int){
      var  manager:androidx.fragment.app.FragmentManager = supportFragmentManager
        var transaction:androidx.fragment.app.FragmentTransaction =manager.beginTransaction()
        transaction.replace(layoutId, fragment)
        transaction.commit()
    }


}