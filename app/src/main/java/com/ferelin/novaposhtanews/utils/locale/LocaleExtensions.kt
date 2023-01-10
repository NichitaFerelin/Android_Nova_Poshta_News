package com.ferelin.novaposhtanews.utils.locale

import java.util.*

const val ROMANIAN_DISPLAY_LANGUAGE = "română"

fun Locale.isRomanian(): Boolean = this.displayLanguage == ROMANIAN_DISPLAY_LANGUAGE
