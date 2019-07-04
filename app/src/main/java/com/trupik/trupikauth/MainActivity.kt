package com.trupik.trupikauth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
lateinit var providers : List<AuthUI.IdpConfig>
     val MY_REQUEST_CODE:Int = 7117


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init
        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )
        showSignINOptions()
        btn_sign_out.setOnClickListener{
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener {
                    btn_sign_out.isEnabled=false
                    showSignINOptions()
                }
                .addOnFailureListener {
                    e->Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==MY_REQUEST_CODE){
            val response =IdpResponse.fromResultIntent(data)
            if(resultCode==Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this,""+user!!.email,Toast.LENGTH_LONG).show()
                btn_sign_out.isEnabled=true
            }
            else{
                Toast.makeText(this,""+response!!.error!!.message,Toast.LENGTH_LONG).show()

            }
        }
    }
    public fun showSignINOptions(){
       startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()

           .setAvailableProviders(providers)
           .setTheme(R.style.MyTheme)
           .build(),MY_REQUEST_CODE
       )

    }
}
