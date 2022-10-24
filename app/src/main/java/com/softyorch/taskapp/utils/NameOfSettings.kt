package com.softyorch.taskapp.utils

enum class NameOfSettings {
    Id,
    Name,
    Email,
    Pass,
    Picture,
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
            Id.name -> "id"
            Name.name -> "name"
            Email.name -> "email"
            Pass.name -> "pass"
            Picture.name -> "picture"
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