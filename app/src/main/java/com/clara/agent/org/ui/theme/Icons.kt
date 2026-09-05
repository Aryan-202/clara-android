package com.clara.agent.org.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.clara.agent.org.R

/**
 * Central place for custom vector icons used throughout the app.
 */
object ClaraIcons {

    /**
     * The Google logo icon.
     *
     * This icon is loaded as a vector resource and can only be accessed from a
     * composable context.
     */
    val Google: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.google_icon_logo)
}