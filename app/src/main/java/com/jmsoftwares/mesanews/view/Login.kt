package com.jmsoftwares.mesanews.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.VolleyError
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.jmsoftwares.mesanews.R
import com.jmsoftwares.mesanews.api.AuthenticatonRequests
import com.jmsoftwares.mesanews.util.Constants
import com.jmsoftwares.mesanews.util.Navigation
import org.json.JSONObject
import java.util.*

class Login : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var callbackManager: CallbackManager? = null
    private var editTextLogin_email: EditText? = null
    private var editTextLogin_password: EditText? = null
    private var textViewlogin_singup: TextView? = null
    private var buttonlogin_signin: Button? = null
    var imageButtonFacebook: com.facebook.login.widget.LoginButton? = null
    var progressbarLogin: ProgressBar? = null
    var navigation: Navigation? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        setupComponents()
        listnerComponents()

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    mAuth?.signOut()
                    LoginManager.getInstance().logOut()
                }

                override fun onError(exception: FacebookException) {
                    mAuth?.signOut()
                    LoginManager.getInstance().logOut()
                }
            })
    }

    public override fun onStart() {
        super.onStart()
        //singup(mAuth?.currentUser!!)
    }

    private fun setupComponents() {

        mAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        imageButtonFacebook = findViewById(R.id.login_button_facebook)
        editTextLogin_email = findViewById(R.id.login_email)
        editTextLogin_password = findViewById(R.id.login_password)
        textViewlogin_singup = findViewById(R.id.login_signup)
        buttonlogin_signin = findViewById(R.id.login_signin)
        progressbarLogin = findViewById(R.id.login_progressbar)
        navigation = Navigation()
    }

    private fun listnerComponents() {

        imageButtonFacebook?.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this, Arrays.asList("email", "public_profile")
            )
        }

        buttonlogin_signin?.setOnClickListener {
            if (editTextLogin_email?.length()!! > 0 && editTextLogin_password?.length()!! > 0) {
                signin()
            } else {
                Toast.makeText(this, R.string.toast_all_params, Toast.LENGTH_LONG).show()
            }
        }

        textViewlogin_singup?.setOnClickListener{ navigation?.goToActivity(
            baseContext,
            Register::class.java
        )}
    }

private fun signin(){

    progressbarLogin?.visibility = View.VISIBLE
    buttonlogin_signin!!.isEnabled = false

    val responseOK: Response.Listener<JSONObject> =
        Response.Listener { response: JSONObject ->

           val token =  response["token"]
            val bundle = Bundle()
            bundle.putString("token", token.toString())
            navigation?.goToActivityCleaningBackParams(this, MainActivity::class.java, bundle)

            if(!this.isFinishing) {
                progressbarLogin?.visibility = View.INVISIBLE
                buttonlogin_signin!!.isEnabled = true
            }
        }

    val responseError = Response.ErrorListener { error: VolleyError ->
        Toast.makeText(this, R.string.toast_login_fail, Toast.LENGTH_LONG).show()

        if(!this.isFinishing) {
            progressbarLogin?.visibility = View.INVISIBLE
            buttonlogin_signin!!.isEnabled = true
        }
    }

    val request = AuthenticatonRequests()
    request.signinRequest(
        baseContext,
        Constants.requestConstants.signinUrl,
        responseOK,
        responseError,
        editTextLogin_email?.text.toString(),
        editTextLogin_password?.text.toString()
    )
}

    private fun singup(user: FirebaseUser) {

        val responseOK: Response.Listener<JSONObject> =
            Response.Listener { response: JSONObject ->

                val token = response["token"]
                val bundle = Bundle()
                bundle.putString("token", token.toString())
                val navigation = Navigation()
                navigation.goToActivityCleaningBackParams(this, MainActivity::class.java, bundle)
            }

        val responseError = Response.ErrorListener { error: VolleyError ->
            mAuth?.signOut()
        }

        val request = AuthenticatonRequests()
        request.signupRequest(
            baseContext,
            Constants.requestConstants.signupUrl,
            responseOK ,
            responseError,
            user.displayName.toString(),
            user.email.toString(),
            user.uid
        )
    }


    private fun handleFacebookAccessToken(token: AccessToken) {
        println("handleFacebookAccessToken:$token")
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth!!.signInWithCredential(credential)
            .addOnCompleteListener(
                this
            ) { task: Task<AuthResult?> ->
                if (task.isSuccessful) {
                    println("signInWithCredential:success")
                    //singup(mAuth?.currentUser!!)

                } else {
                    println("signInWithCredential:failure " + task.exception)
                    Toast.makeText(
                        this, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

}