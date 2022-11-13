package com.example.stockapp.profilecard

import com.example.stockapp.stockcard.StockData

object SampleProfileData {
    val items: List<ProfileData> = listOf(
        ProfileData(
            profileName = "Default",
            profileValue = 2500.0
        ),
        ProfileData(
            profileName = "Crypto",
            profileValue = 50000.0
        )
    )
}