package com.altayyar.app.presentation.ui.activity

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.core.view.updatePadding
import androidx.lifecycle.lifecycleScope
import com.altayyar.app.BuildConfig
import com.altayyar.app.R
import com.altayyar.app.data.repository.InstanceInfoRepository
import com.altayyar.app.databinding.ActivityAboutBinding
import com.altayyar.app.util.copyToClipboard
import com.altayyar.app.util.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AboutActivity : BottomSheetActivity() {
    @Inject
    lateinit var instanceInfoRepository: InstanceInfoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAboutBinding.inflate(layoutInflater)
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

        binding.versionTextView.text = getString(R.string.about_app_version, getString(R.string.app_name), BuildConfig.VERSION_NAME)

        binding.deviceInfo.text = getString(
            R.string.about_device_info,
            Build.MANUFACTURER,
            Build.MODEL,
            Build.VERSION.RELEASE,
            Build.VERSION.SDK_INT
        )

        lifecycleScope.launch {
            accountManager.activeAccount?.let { account ->
                val instanceInfo = instanceInfoRepository.getUpdatedInstanceInfoOrFallback()
                binding.accountInfo.text = getString(
                    R.string.about_account_info,
                    account.username,
                    account.domain,
                    instanceInfo.version
                )
                binding.accountInfoTitle.show()
                binding.accountInfo.show()
            }
        }

        binding.copyDeviceInfo.setOnClickListener {
            copyToClipboard(
                "${binding.versionTextView.text}\n\nDevice:\n\n${binding.deviceInfo.text}\n\nAccount:\n\n${binding.accountInfo.text}",
                getString(R.string.about_copied),
                "Altayyar version information",
            )
        }
    }
}
