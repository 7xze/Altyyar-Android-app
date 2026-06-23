package com.altayyar.app.components.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.core.content.edit
import androidx.core.net.toUri
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import at.connyduck.calladapter.networkresult.fold
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.altayyar.app.BaseActivity
import com.altayyar.app.BuildConfig
import com.altayyar.app.MainActivity
import com.altayyar.app.R
import com.altayyar.app.databinding.ActivityLoginBinding
import com.altayyar.app.entity.AccessToken
import com.altayyar.app.entity.Account
import com.altayyar.app.entity.AccountSource
import com.altayyar.app.entity.Status
import com.altayyar.app.network.MastodonApi
import com.altayyar.app.util.getNonNullString
import com.altayyar.app.util.openLinkInCustomTab
import com.altayyar.app.util.rickRoll
import com.altayyar.app.util.setOnWindowInsetsChangeListener
import com.altayyar.app.util.shouldRickRoll
import com.altayyar.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.launch
import okhttp3.HttpUrl

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    @Inject
    lateinit var mastodonApi: MastodonApi

    private lateinit var googleSignInClient: GoogleSignInClient
    private var firebaseAuth: FirebaseAuth? = null

    private val binding by viewBinding(ActivityLoginBinding::inflate)

    private val oauthRedirectUri: String
        get() {
            val scheme = getString(R.string.oauth_scheme)
            val host = BuildConfig.APPLICATION_ID
            return "$scheme://$host/"
        }

    private val doWebViewAuth = registerForActivityResult(OauthLogin()) { result ->
        when (result) {
            is LoginResult.Ok -> fetchOauthToken(result.code)
            is LoginResult.Err -> displayError(result.errorMessage)
            is LoginResult.Cancel -> setLoading(false)
        }
    }

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

        binding.mastodonLoginButton.setOnClickListener { onLoginClick(true) }

        binding.browserLoginButton.setOnClickListener { onLoginClick(false) }

        binding.whatsAnInstanceTextView.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(this)
                .setMessage(R.string.dialog_whats_an_instance)
                .setPositiveButton(R.string.action_close, null)
                .show()
            val textView = dialog.findViewById<TextView>(android.R.id.message)
            textView?.movementMethod = LinkMovementMethod.getInstance()
        }

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(R.string.action_browser_login)?.apply {
            setOnMenuItemClickListener {
                onLoginClick(false)
                true
            }
        }

        return super.onCreateOptionsMenu(menu)
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

                val accessToken = AccessToken(
                    accessToken = "dev_access_token"
                )

                accountManager.addAccount(
                    accessToken = accessToken.accessToken,
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

    private fun onLoginClick(openInWebView: Boolean) {
        binding.loginButton.isEnabled = false
        binding.domainTextInputLayout.error = null

        val domain = canonicalizeDomain(binding.domainEditText.text.toString())

        try {
            HttpUrl.Builder().host(domain).scheme("https").build()
        } catch (_: IllegalArgumentException) {
            setLoading(false)
            binding.domainTextInputLayout.error = getString(R.string.error_invalid_domain)
            return
        }

        if (shouldRickRoll(this, domain)) {
            rickRoll(this)
            return
        }

        setLoading(true)

        lifecycleScope.launch {
            mastodonApi.authenticateApp(
                domain,
                getString(R.string.app_name),
                oauthRedirectUri,
                OAUTH_SCOPES,
                getString(R.string.tayyar_website)
            ).fold(
                { credentials ->
                    preferences.edit {
                        putString(DOMAIN, domain)
                        putString(CLIENT_ID, credentials.clientId)
                        putString(CLIENT_SECRET, credentials.clientSecret)
                    }

                    redirectUserToAuthorizeAndLogin(domain, credentials.clientId, openInWebView)
                },
                { e ->
                    binding.loginButton.isEnabled = true
                    binding.domainTextInputLayout.error =
                        getString(R.string.error_failed_app_registration)
                    setLoading(false)
                    Log.e(TAG, Log.getStackTraceString(e))
                    return@launch
                }
            )
        }
    }

    private fun redirectUserToAuthorizeAndLogin(
        domain: String,
        clientId: String,
        openInWebView: Boolean
    ) {
        val uri = Uri.Builder()
            .scheme("https")
            .authority(domain)
            .path(MastodonApi.ENDPOINT_AUTHORIZE)
            .appendQueryParameter("client_id", clientId)
            .appendQueryParameter("redirect_uri", oauthRedirectUri)
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("scope", OAUTH_SCOPES)
            .build()

        if (openInWebView) {
            doWebViewAuth.launch(LoginData(domain, uri, oauthRedirectUri.toUri()))
        } else {
            openLinkInCustomTab(uri, this)
        }
    }

    override fun onStart() {
        super.onStart()

        val uri = intent.data

        if (uri?.toString()?.startsWith(oauthRedirectUri) == true) {
            val code = uri.getQueryParameter("code")
            val error = uri.getQueryParameter("error")

            if (code != null) {
                fetchOauthToken(code)
            } else {
                displayError(error)
            }
        } else {
            setLoading(false)
        }
    }

    private fun displayError(error: String?) {
        setLoading(false)

        binding.domainTextInputLayout.error = if (error == null) {
            getString(R.string.error_authorization_unknown)
        } else {
            Log.e(TAG, getString(R.string.error_authorization_denied) + " " + error)
            error.ifBlank { getString(R.string.error_authorization_denied) }
        }
    }

    private fun fetchOauthToken(code: String) {
        setLoading(true)

        val domain = preferences.getNonNullString(DOMAIN, "")
        val clientId = preferences.getNonNullString(CLIENT_ID, "")
        val clientSecret = preferences.getNonNullString(CLIENT_SECRET, "")

        lifecycleScope.launch {
            mastodonApi.fetchOAuthToken(
                domain,
                clientId,
                clientSecret,
                oauthRedirectUri,
                code,
                "authorization_code"
            ).fold(
                { accessToken ->
                    fetchAccountDetails(accessToken, domain, clientId, clientSecret)
                },
                { e ->
                    setLoading(false)
                    binding.domainTextInputLayout.error =
                        getString(R.string.error_retrieving_oauth_token)
                    Log.e(TAG, getString(R.string.error_retrieving_oauth_token), e)
                }
            )
        }
    }

    private suspend fun fetchAccountDetails(
        accessToken: AccessToken,
        domain: String,
        clientId: String,
        clientSecret: String
    ) {
        mastodonApi.accountVerifyCredentials(
            domain = domain,
            auth = "Bearer ${accessToken.accessToken}"
        ).fold({ newAccount ->
            accountManager.addAccount(
                accessToken = accessToken.accessToken,
                domain = domain,
                clientId = clientId,
                clientSecret = clientSecret,
                oauthScopes = OAUTH_SCOPES,
                newAccount = newAccount
            )
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }, { e ->
            setLoading(false)
            binding.domainTextInputLayout.error =
                getString(R.string.error_loading_account_details)
            Log.e(TAG, getString(R.string.error_loading_account_details), e)
        })
    }

    private fun setLoading(loadingState: Boolean) {
        if (loadingState) {
            binding.loginLoadingLayout.visibility = View.VISIBLE
            binding.loginInputLayout.visibility = View.GONE
        } else {
            binding.loginLoadingLayout.visibility = View.GONE
            binding.loginInputLayout.visibility = View.VISIBLE
            binding.loginButton.isEnabled = true
        }
    }

    private fun isAdditionalLogin(): Boolean {
        return intent.getIntExtra(LOGIN_MODE, MODE_DEFAULT) == MODE_ADDITIONAL_LOGIN
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val OAUTH_SCOPES = "read write follow push"
        private const val LOGIN_MODE = "LOGIN_MODE"
        private const val DOMAIN = "domain"
        private const val CLIENT_ID = "clientId"
        private const val CLIENT_SECRET = "clientSecret"

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

        private fun canonicalizeDomain(domain: String): String {
            var s = domain.replaceFirst("http://", "")
            s = s.replaceFirst("https://", "")
            val at = s.lastIndexOf('@')
            if (at != -1) {
                s = s.substring(at + 1)
            }
            return s.trim { it <= ' ' }
        }
    }
}
