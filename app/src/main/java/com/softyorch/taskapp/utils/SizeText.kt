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

/*    data class ExtraHighSizeText(
        val size: TextUnit = 22.sp, val sizeText: String = "Extra high size"
    ) : SizeText()*/
}

class StandardizedSizes(selector: Int) {
    val minimumSize = (sizeTextUnits(selector).value.toInt() - 4).sp
    val littleSize = (sizeTextUnits(selector).value.toInt() - 2).sp
    val normalSize = sizeTextUnits(selector)
    val largeSize = (sizeTextUnits(selector).value.toInt() + 2).sp
    val highSize = (sizeTextUnits(selector).value.toInt() + 4).sp
    val maximumSize = (sizeTextUnits(selector).value.toInt() + 6).sp
}

fun sizeTextUnits(selector: Int): TextUnit =
    when (selector) {
        0 -> MinimumSizeText().size
        1 -> MiddleSizeText().size
        2 -> NormalSizeText().size
        3 -> HighSizeText().size
        4 -> HigherSizeText().size
        //5 -> ExtraHighSizeText().size
        else -> { MiddleSizeText().size}
    }

fun sizeTextName(selector: Int): String =
    when (selector) {
        0 -> MinimumSizeText().sizeText
        1 -> MiddleSizeText().sizeText
        2 -> NormalSizeText().sizeText
        3 -> HighSizeText().sizeText
        4 -> HigherSizeText().sizeText
        //5 -> ExtraHighSizeText().sizeText
        else -> { MiddleSizeText().sizeText }
    }


