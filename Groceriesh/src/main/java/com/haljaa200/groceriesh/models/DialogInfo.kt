package com.haljaa200.groceriesh.models

import java.io.Serializable

data class DialogInfo(
    val title: Int,
    val text: Int,
    val okTextColor: Int = 0,
): Serializable