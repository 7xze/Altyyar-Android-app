package com.altayyar.app.presentation.ui.activity

import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.updatePadding
import com.altayyar.app.R
import com.altayyar.app.databinding.ActivityLicenseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LicenseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.includedToolbar.toolbar)
        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        setTitle(R.string.about_title_activity)

        ViewCompat.setOnApplyWindowInsetsListener(binding.scrollView) { scrollView, insets ->
            val systemBarInsets = insets.getInsets(systemBars())
            scrollView.updatePadding(bottom = systemBarInsets.bottom)
            insets.inset(0, 0, 0, systemBarInsets.bottom)
        }

        binding.licenseApacheTextView.text = getString(R.string.license_apache_2)
    }
}
