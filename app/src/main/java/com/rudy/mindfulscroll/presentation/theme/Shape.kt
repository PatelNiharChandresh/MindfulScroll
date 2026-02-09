package com.rudy.mindfulscroll.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// UIUXspec Section 3.4
val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),   // Chips
    medium = RoundedCornerShape(12.dp), // Cards
    large = RoundedCornerShape(20.dp),  // Buttons
    extraLarge = RoundedCornerShape(28.dp), // Dialogs, search bar
)
