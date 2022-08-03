package com.eratart.tools.customtab

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.eratart.baristashandbook.tools.customtabs.ICustomTabTool
import com.eratart.core.ext.printError

class CustomTabTool(private val context: Context) : ICustomTabTool {

    override fun openCustomTab(url: String) {
        if (url.isEmpty()) return
        val uri = Uri.parse(url)
        val customTabsIntent = CustomTabsIntent.Builder().build().apply {
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            customTabsIntent.launchUrl(context, uri)
        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
            e.printError()
        }
    }
}