package com.lj.fatoldsun.platform.webview

import android.os.Bundle
import com.lj.fatoldsun.core.base.BaseLibActivity
import com.lj.fatoldsun.platform.R
import com.lj.fatoldsun.platform.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : BaseLibActivity() {
    private lateinit var binding: ActivityWebViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = intent.getStringExtra("url") ?: return
        val fragment = WebViewFragment.newInstance(url)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}