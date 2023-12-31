package com.example.speechrectificationapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*
import android.text.TextUtils
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import com.example.speechrecificationapp.LoginActivity
import com.example.speechrecificationapp.R
import com.example.speechrecificationapp.Speech_Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity()
{
    val SPLASH_SCREEN = 5000

    private lateinit var topAnimation : Animation
    private lateinit var bottomAnimation : Animation

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    //lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //hide status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val actionBar = supportActionBar
        actionBar!!.hide()

        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animations)
        bottomAnimation = AnimationUtils.loadAnimation(this,R.anim.bottom_animations)

        val logo_id = findViewById<ImageView>(R.id.logo)
        val entername_id = findViewById<EditText>(R.id.entername)
        val enteremail_id = findViewById<EditText>(R.id.enteremail)


        val enterpassword_id = findViewById<EditText>(R.id.enterpassword)
        val enterconfirmpassword_id = findViewById<EditText>(R.id.enterconfirmpassword)
        val btnSignUp_id = findViewById<Button>(R.id.btnSignUp)
        val btnLogin_id = findViewById<Button>(R.id.btnLogin)

        logo_id.animation = topAnimation
        entername_id.animation = topAnimation
        enteremail_id.animation = topAnimation
        enterpassword_id.animation = bottomAnimation
        enterconfirmpassword_id.animation = bottomAnimation
        btnSignUp_id.animation = bottomAnimation
        btnLogin_id.animation = bottomAnimation

//        //Animation on ChattingApp Icon
//        chat_animated_view_id.setOnClickListener{
//            chat_animated_view_id.animate().apply {
//                duration=1000
//                rotationYBy(360f)
//            }.withEndAction{
//                chat_animated_view_id.animate().apply {
//                    duration=1000
//                    rotationYBy(360f)
//                }.start()
//            }
//        }
//        auth = FirebaseAuth.getInstance()

        btnSignUp_id.setOnClickListener{
            val entername_id=findViewById<EditText>(R.id.entername)
            val enteremail_id=findViewById<EditText>(R.id.enteremail)
            val enterpassword_id=findViewById<EditText>(R.id.enterpassword)
            val enterconfirmpassword_id=findViewById<EditText>(R.id.enterconfirmpassword)

            val userName = entername_id.text.toString()
            val email = enteremail_id.text.toString()
            val password = enterpassword_id.text.toString()
            val confirmPassword = enterconfirmpassword_id.text.toString()

            if (TextUtils.isEmpty(userName))
            {
                Toast.makeText(applicationContext,"username is required",Toast.LENGTH_SHORT).show()
            }
            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(applicationContext,"email is required",Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(password))
            {
                Toast.makeText(applicationContext,"password is required",Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(confirmPassword))
            {
                Toast.makeText(applicationContext,"confirm password is required",Toast.LENGTH_SHORT).show()
            }

            if (password!=confirmPassword)
            {
                Toast.makeText(applicationContext,"password not match",Toast.LENGTH_SHORT).show()
            }
            registerUser(userName,email,password)

        }
        //when clickon login button, it will move to login activity
        val btnlogin_id=findViewById<Button>(R.id.btnLogin)
        btnlogin_id.setOnClickListener {
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    //add users
    private fun registerUser(userName:String,email:String,password:String)
    {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful)
                {
                    val user: FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName",userName)
                    hashMap.put("profileImage","")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if (it.isSuccessful)
                        {
                            /* open home activity */
                            val intent = Intent(this@MainActivity, Speech_Activity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }
}