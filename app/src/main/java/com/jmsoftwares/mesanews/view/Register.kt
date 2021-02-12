package com.jmsoftwares.mesanews.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.VolleyError
import com.jmsoftwares.mesanews.R
import com.jmsoftwares.mesanews.api.AuthenticatonRequests
import com.jmsoftwares.mesanews.util.Constants
import com.jmsoftwares.mesanews.util.Navigation
import org.json.JSONObject

class Register : AppCompatActivity() {

    private var editTextRegister_name: EditText? = null
    private var editTextRegister_email: EditText? = null
    private var editTextRegister_password: EditText? = null
    private var editTextRegister_repassword: EditText? = null
    private var buttonRegister_singup: Button? = null
    var progressbarRegister: ProgressBar? = null
    var destroy: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        setupComponents()
        listnerComponents()
    }

    private fun setupComponents() {

        val actionbar: ActionBar = supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeButtonEnabled(true)
        editTextRegister_name = findViewById(R.id.register_name)
        editTextRegister_email = findViewById(R.id.register_email)
        editTextRegister_password = findViewById(R.id.register_password)
        editTextRegister_repassword = findViewById(R.id.register_repassword)
        buttonRegister_singup = findViewById(R.id.register_signin)
    }

    private fun listnerComponents() {
        buttonRegister_singup?.setOnClickListener {
            if (editTextRegister_password?.text.toString() == editTextRegister_repassword?.text.toString()
                && !editTextRegister_name?.equals("")!!
                && !editTextRegister_email?.equals("")!!
            ) {
                singup()
            }else{
                Toast.makeText(this, "Complete os campos corretamente!", Toast.LENGTH_LONG ).show()
            }
        }
    }

    private fun singup() {

        buttonRegister_singup!!.isEnabled = false
        progressbarRegister?.visibility = View.VISIBLE

        val responseOK: Response.Listener<JSONObject> =
            Response.Listener { response: JSONObject ->

                val token = response["token"]
                val bundle = Bundle()
                bundle.putString("token", token.toString())
                val navigation = Navigation()
                navigation.goToActivityCleaningBackParams(this, MainActivity::class.java, bundle)

                if(!this.isFinishing) {
                    buttonRegister_singup!!.isEnabled = true
                    progressbarRegister?.visibility = View.INVISIBLE
                }
            }

        val responseError = Response.ErrorListener { error: VolleyError ->
            println("request fail =" + error.message)

            if(!this.isFinishing) {
                buttonRegister_singup!!.isEnabled = true
                progressbarRegister?.visibility = View.INVISIBLE
            }
        }

        val request = AuthenticatonRequests()
        request.signupRequest(
            baseContext,
            Constants.requestConstants.signupUrl,
            responseOK ,
            responseError,
            editTextRegister_name?.text.toString(),
            editTextRegister_email?.text.toString(),
            editTextRegister_password?.text.toString()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}