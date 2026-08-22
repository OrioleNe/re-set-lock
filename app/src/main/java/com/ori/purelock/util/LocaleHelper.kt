package com.ori.purelock.util

import android.content.Context
import android.content.res.Configuration
import com.ori.purelock.data.AppLanguage
import java.util.Locale

/**
 * Wraps a [Context] so its resources resolve using [language] instead of the
 * device's locale, powering the in-app "Ngôn ngữ" (Language) setting.
 *
 * [AppLanguage.SYSTEM] returns [context] unchanged so the device locale (and
 * its own runtime locale changes) keeps being followed.
 */
object LocaleHelper {
    fun wrap(context: Context, language: AppLanguage): Context {
        val tag = language.tag ?: return context

        val locale = Locale.forLanguageTag(tag)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }
}
