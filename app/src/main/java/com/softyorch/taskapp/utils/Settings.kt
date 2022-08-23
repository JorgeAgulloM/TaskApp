package com.softyorch.taskapp.utils

enum class Settings {
    Name,
    Pass,
    Activate,
    LastLoginDate,
    RememberMe,
    LightDarkAutomaticTheme,
    LightOrDarkTheme,
    AutomaticLanguage,
    AutomaticColors,
    TimeLimitAutoLoading,
    TextSize;

    companion object {
        fun name(name: String): String = when (name) {
            Name.name -> "name"
            Pass.name -> "pass"
            Activate.name -> "activate"
            LastLoginDate.name -> "last_login_date"
            RememberMe.name -> "remember_me"
            LightDarkAutomaticTheme.name -> "light_dark_automatic_theme"
            LightOrDarkTheme.name -> "light_or_dark_theme"
            AutomaticLanguage.name -> "automatic_language"
            AutomaticColors.name -> "automatic_colors"
            TimeLimitAutoLoading.name -> "time_limit_auto_loading"
            TextSize.name -> "text_size"
            else -> {
                ""
            }
        }
    }
}