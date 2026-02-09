package com.rudy.mindfulscroll.domain.model

data class ThresholdConfig(
    val id: Int = 0,
    val presetName: String?,
    val threshold1Min: Int,
    val threshold2Min: Int,
    val threshold3Min: Int,
    val isGlobal: Boolean = true,
    val appPackageName: String? = null,
) {
    val threshold1Seconds: Int get() = threshold1Min * 60
    val threshold2Seconds: Int get() = threshold2Min * 60
    val threshold3Seconds: Int get() = threshold3Min * 60
    val throbTriggerSeconds: Int get() = threshold3Seconds + (5 * 60)

    companion object {
        val LIGHT = ThresholdConfig(
            presetName = "Light",
            threshold1Min = 5,
            threshold2Min = 10,
            threshold3Min = 15,
        )
        val MODERATE = ThresholdConfig(
            presetName = "Moderate",
            threshold1Min = 10,
            threshold2Min = 20,
            threshold3Min = 30,
        )
        val STRICT = ThresholdConfig(
            presetName = "Strict",
            threshold1Min = 5,
            threshold2Min = 10,
            threshold3Min = 20,
        )
        val DEFAULT = MODERATE
    }
}
