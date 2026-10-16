package com.stitchbook.app.support

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/** Contact details and the email helper. Change SUPPORT_EMAIL before you publish. */
object Support {
    const val SUPPORT_EMAIL = "aksclassic247@gmail.com"

    /** The About text shown on Home. It says what the app does not do, so nobody is misled. */
    const val ABOUT_TEXT =
        "StitchBook does not turn photos into patterns, and it has no AI chat. " +
            "Sample patterns were drafted with AI help and are not yet test-made, so check them before you start. " +
            "Everything is free, with no ads and no subscription."

    fun openMail(context: Context, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(SUPPORT_EMAIL))
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        try {
            context.startActivity(intent)
        } catch (error: ActivityNotFoundException) {
            Toast.makeText(context, "No email app found. Write to $SUPPORT_EMAIL.", Toast.LENGTH_LONG).show()
        }
    }
}
