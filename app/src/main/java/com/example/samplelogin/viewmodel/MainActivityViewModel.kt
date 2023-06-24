package com.example.samplelogin.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.content.Context
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile

class MainActivityViewModel : ViewModel(){

    val scheme = "demo"
    val domain = "dev-nv2avlcdioxnnsm5.us.auth0.com"
    val clientID = "9vpnuCTFZitS4pNltlya0iTqrwcQQ2js"
    var cachedCredentials: Credentials? = null
    var cachedUserProfile: UserProfile? = null
    var userIsAuthenticated by mutableStateOf(false)
    var emailData by mutableStateOf("")
    var nameData by mutableStateOf("")


    fun loginWithBrowser(context: Context, account: Auth0) {
        WebAuthProvider.login(account)
            .withScheme(scheme)
            .withScope("openid profile email read:current_user update:current_user_metadata")
            .withAudience("https://${domain}/api/v2/")

            .start(context, object : Callback<Credentials, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    showMessage(context,"Failure: ${exception.getCode()}")
                }

                override fun onSuccess(credentials: Credentials) {
                    cachedCredentials = credentials
                    showMessage(context,"Success Authentication")

                    userIsAuthenticated = true

                    showUserProfile(context, account)
                }
            })
    }

    fun logout(context: Context, account: Auth0) {
        WebAuthProvider.logout(account)
            .withScheme(scheme)
            .start(context, object : Callback<Void?, AuthenticationException> {
                override fun onSuccess(payload: Void?) {
                    // The user has been logged out!

                    showMessage(context, "Logout Success")
                    cachedCredentials = null
                    cachedUserProfile = null
                    userIsAuthenticated = false

                    nameData = ""
                    emailData = ""

                }

                override fun onFailure(exception: AuthenticationException) {

                    showMessage(context, "Failure: ${exception.getCode()}")
                }
            })
    }

    fun showUserProfile(context: Context, account: Auth0) {
        val client = AuthenticationAPIClient(account)

        client.userInfo(cachedCredentials!!.accessToken!!)
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    showMessage(context,"Failure: ${exception.getCode()}")
                }

                override fun onSuccess(profile: UserProfile) {
                    cachedUserProfile = profile

                    emailData = profile.email.toString()
                    nameData = profile.name.toString()
                }
            })
    }

    fun getUserMetadata(context: Context, account: Auth0) {
        val usersClient = UsersAPIClient(account, cachedCredentials!!.accessToken!!)

        usersClient.getProfile(cachedUserProfile!!.getId()!!)
            .start(object : Callback<UserProfile, ManagementException> {
                override fun onFailure(exception: ManagementException) {
                    showMessage(context,"Failure: ${exception.getCode()}")
                }

                override fun onSuccess(userProfile: UserProfile) {
                    cachedUserProfile = userProfile;

                    val country = userProfile.getUserMetadata()["country"] as String?
                }
            })
    }


    fun showMessage(context: Context, text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }
}