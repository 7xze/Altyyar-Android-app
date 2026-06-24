package com.altayyar.app.presentation.ui.feature.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.altayyar.app.presentation.ui.activity.BaseActivity
import com.altayyar.app.BuildConfig
import com.altayyar.app.presentation.ui.activity.MainActivity
import com.altayyar.app.R
import com.altayyar.app.databinding.ActivityLoginBinding
import com.altayyar.app.entity.Account
import com.altayyar.app.entity.AccountSource
import com.altayyar.app.entity.Status
import com.altayyar.app.util.setOnWindowInsetsChangeListener
import com.altayyar.app.util.viewBinding
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private var firebaseAuth: FirebaseAuth? = null

    private val binding by viewBinding(ActivityLoginBinding::inflate)

    private val googleSignInLauncher = registerForActivityResult(GoogleSignInActivityResultContract()) { result ->
        if (result != null) {
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result)
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e(TAG, "Google sign in failed", e)
                displayError(getString(R.string.error_google_sign_in_failed))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        firebaseAuth = try {
            FirebaseApp.initializeApp(this)
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Firebase initialization failed", e)
            null
        }

        if (firebaseAuth != null) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)
        }

        binding.loginScrollView.setOnWindowInsetsChangeListener { windowInsets ->
            val insets = windowInsets.getInsets(systemBars() or ime())
            binding.loginScrollView.updatePadding(bottom = insets.bottom)
        }

        if (BuildConfig.CUSTOM_LOGO_URL.isNotBlank()) {
            Glide.with(binding.loginLogo)
                .load(BuildConfig.CUSTOM_LOGO_URL)
                .placeholder(null)
                .into(binding.loginLogo)
        }

        binding.googleSignInButton.setOnClickListener { onGoogleSignInClick() }
        binding.emailLoginButton.setOnClickListener { onEmailLoginClick() }

        binding.devModeToggle.setOnClickListener {
            val isVisible = binding.devLoginLayout.visibility == View.VISIBLE
            binding.devLoginLayout.visibility = if (isVisible) View.GONE else View.VISIBLE
            binding.devModeToggle.setText(
                if (isVisible) R.string.login_dev_mode else R.string.login_dev_mode_hide
            )
        }

        binding.devLoginButton.setOnClickListener { onDevLoginClick() }

        if (isAdditionalLogin()) {
            binding.toolbar.visibility = View.VISIBLE
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    override fun requiresLogin(): Boolean {
        return false
    }

    private fun onGoogleSignInClick() {
        if (firebaseAuth == null) {
            displayError(getString(R.string.error_google_sign_in_failed))
            return
        }
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val auth = firebaseAuth
        if (auth == null) {
            displayError(getString(R.string.error_google_sign_in_failed))
            return
        }
        setLoading(true)
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        performFirebaseLogin(
                            uid = firebaseUser.uid,
                            email = firebaseUser.email ?: "",
                            displayName = firebaseUser.displayName ?: "",
                            avatar = firebaseUser.photoUrl?.toString() ?: ""
                        )
                    }
                } else {
                    setLoading(false)
                    displayError(getString(R.string.error_google_sign_in_failed))
                }
            }
    }

    private fun onEmailLoginClick() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        binding.emailTextInputLayout.error = null
        binding.passwordTextInputLayout.error = null

        if (email.isEmpty()) {
            binding.emailTextInputLayout.error = getString(R.string.error_empty)
            return
        }
        if (password.isEmpty()) {
            binding.passwordTextInputLayout.error = getString(R.string.error_empty)
            return
        }

        if (email == DEV_EMAIL && password == DEV_PASSWORD) {
            performDevLogin()
            return
        }

        val auth = firebaseAuth
        if (auth == null) {
            binding.emailTextInputLayout.error = getString(R.string.error_google_sign_in_failed)
            return
        }

        setLoading(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        performFirebaseLogin(
                            uid = firebaseUser.uid,
                            email = firebaseUser.email ?: "",
                            displayName = firebaseUser.displayName ?: email.substringBefore("@"),
                            avatar = firebaseUser.photoUrl?.toString() ?: ""
                        )
                    }
                } else {
                    setLoading(false)
                    if (task.exception?.message?.contains("no user record", ignoreCase = true) == true) {
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(this) { registerTask ->
                                if (registerTask.isSuccessful) {
                                    val newUser = auth.currentUser
                                    if (newUser != null) {
                                        performFirebaseLogin(
                                            uid = newUser.uid,
                                            email = newUser.email ?: "",
                                            displayName = newUser.displayName ?: email.substringBefore("@"),
                                            avatar = ""
                                        )
                                    }
                                } else {
                                    binding.emailTextInputLayout.error = getString(R.string.error_invalid_credentials)
                                }
                            }
                    } else {
                        binding.passwordTextInputLayout.error = getString(R.string.error_invalid_credentials)
                    }
                }
            }
    }

    private fun performFirebaseLogin(uid: String, email: String, displayName: String, avatar: String) {
        lifecycleScope.launch {
            try {
                val account = Account(
                    id = uid,
                    localUsername = email.substringBefore("@"),
                    username = email,
                    displayName = displayName,
                    createdAt = Date(),
                    note = "",
                    url = "",
                    avatar = avatar,
                    header = "",
                    locked = false,
                    followersCount = 0,
                    followingCount = 0,
                    statusesCount = 0,
                    source = AccountSource(
                        privacy = Status.Visibility.PUBLIC,
                        sensitive = false,
                        note = "",
                        language = "ar"
                    ),
                    bot = false,
                    emojis = emptyList()
                )

                accountManager.addAccount(
                    accessToken = uid,
                    domain = "altayyar.app",
                    clientId = "firebase",
                    clientSecret = "",
                    oauthScopes = "",
                    newAccount = account
                )

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                setLoading(false)
                Log.e(TAG, "Firebase login failed", e)
            }
        }
    }

    private fun onDevLoginClick() {
        val email = binding.devEmailEditText.text.toString().trim()
        val password = binding.devPasswordEditText.text.toString().trim()

        binding.devEmailTextInputLayout.error = null
        binding.devPasswordTextInputLayout.error = null

        if (email.isEmpty()) {
            binding.devEmailTextInputLayout.error = getString(R.string.error_empty)
            return
        }
        if (password.isEmpty()) {
            binding.devPasswordTextInputLayout.error = getString(R.string.error_empty)
            return
        }

        if (email == DEV_EMAIL && password == DEV_PASSWORD) {
            performDevLogin()
        } else {
            binding.devPasswordTextInputLayout.error = getString(R.string.error_invalid_credentials)
        }
    }

    private fun performDevLogin() {
        setLoading(true)

        lifecycleScope.launch {
            try {
                val devAccount = Account(
                    id = "dev_001",
                    localUsername = "joan",
                    username = "joan@dev.local",
                    displayName = "Joan (Dev)",
                    createdAt = Date(),
                    note = "مطور تيار",
                    url = "https://dev.local/@joan",
                    avatar = "",
                    header = "",
                    locked = false,
                    followersCount = 0,
                    followingCount = 0,
                    statusesCount = 0,
                    source = AccountSource(
                        privacy = Status.Visibility.PUBLIC,
                        sensitive = false,
                        note = "مطور تيار",
                        language = "ar"
                    ),
                    bot = false,
                    emojis = emptyList()
                )

                accountManager.addAccount(
                    accessToken = "dev_access_token",
                    domain = "dev.local",
                    clientId = "dev_client",
                    clientSecret = "dev_secret",
                    oauthScopes = "read write follow push",
                    newAccount = devAccount
                )

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } catch (e: Exception) {
                setLoading(false)
                binding.devPasswordTextInputLayout.error = getString(R.string.error_generic)
                Log.e(TAG, "Dev login failed", e)
            }
        }
    }

    private fun setLoading(loadingState: Boolean) {
        if (loadingState) {
            binding.loginLoadingLayout.visibility = View.VISIBLE
            binding.loginInputLayout.visibility = View.GONE
        } else {
            binding.loginLoadingLayout.visibility = View.GONE
            binding.loginInputLayout.visibility = View.VISIBLE
        }
    }

    private fun displayError(error: String?) {
        setLoading(false)
        if (error != null) {
            Log.e(TAG, error)
        }
    }

    private fun isAdditionalLogin(): Boolean {
        return intent.getIntExtra(LOGIN_MODE, MODE_DEFAULT) == MODE_ADDITIONAL_LOGIN
    }

    companion object {
        private const val TAG = "LoginActivity"

        private const val DEV_EMAIL = "joan@joan.com"
        private const val DEV_PASSWORD = "7Xroot_2025"

        const val MODE_DEFAULT = 0
        const val MODE_ADDITIONAL_LOGIN = 1

        @JvmStatic
        fun getIntent(context: Context, mode: Int): Intent {
            val loginIntent = Intent(context, LoginActivity::class.java)
            loginIntent.putExtra(LOGIN_MODE, mode)
            return loginIntent
        }

        private const val LOGIN_MODE = "LOGIN_MODE"
    }
}
