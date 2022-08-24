package com.softyorch.taskapp.utils

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

sealed class SizeText {
    data class MinimumSizeText(
        val size: TextUnit = 12.sp,
        val sizeText: String = "Minimum size"
    ) : SizeText()
    data class MiddleSizeText(
        val size: TextUnit = 14.sp,
        val sizeText: String = "Middle size"
    ) : SizeText()
    data class NormalSizeText(
        val size: TextUnit = 16.sp,
        val sizeText: String = "Normal size"
    ) : SizeText()
    data class HighSizeText(
        val size: TextUnit = 18.sp,
        val sizeText: String = "High size"
    ) : SizeText()
    data class HigherSizeText(
        val size: TextUnit = 20.sp,
        val sizeText: String = "Higher size"
    ) : SizeText()
    data class ExtraHighSizeText(
        val size: TextUnit = 22.sp,
        val sizeText: String = "Extra high size"
    ) : SizeText()
}
