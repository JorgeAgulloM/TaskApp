package com.softyorch.taskapp.utils

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.softyorch.taskapp.utils.SizeText.*

sealed class SizeText {
    data class MinimumSizeText(
        val size: TextUnit = 12.sp, val sizeText: String = "Minimum size"
    ) : SizeText()

    data class MiddleSizeText(
        val size: TextUnit = 14.sp, val sizeText: String = "Middle size"
    ) : SizeText()

    data class NormalSizeText(
        val size: TextUnit = 16.sp, val sizeText: String = "Normal size"
    ) : SizeText()

    data class HighSizeText(
        val size: TextUnit = 18.sp, val sizeText: String = "High size"
    ) : SizeText()

    data class HigherSizeText(
        val size: TextUnit = 20.sp, val sizeText: String = "Higher size"
    ) : SizeText()

    data class ExtraHighSizeText(
        val size: TextUnit = 22.sp, val sizeText: String = "Extra high size"
    ) : SizeText()
}

fun sizeTextUnits(selector: Int): TextUnit {
    var size = 0.sp
    when (selector) {
        0 -> size = MinimumSizeText().size
        1 -> size = MiddleSizeText().size
        2 -> size = NormalSizeText().size
        3 -> size = HighSizeText().size
        4 -> size = HigherSizeText().size
        5 -> size = ExtraHighSizeText().size
    }
    return size
}

fun sizeTextName(selector: Int): String {
    var name = ""
    when (selector) {
        0 -> name = MinimumSizeText().sizeText
        1 -> name = MiddleSizeText().sizeText
        2 -> name = NormalSizeText().sizeText
        3 -> name = HighSizeText().sizeText
        4 -> name = HigherSizeText().sizeText
        5 -> name = ExtraHighSizeText().sizeText
    }
    return name
}