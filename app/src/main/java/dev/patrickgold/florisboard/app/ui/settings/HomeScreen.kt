/*
 * Copyright (C) 2021 Patrick Goldinger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.patrickgold.florisboard.app.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.patrickgold.florisboard.BuildConfig
import dev.patrickgold.florisboard.R
import dev.patrickgold.florisboard.app.LocalNavController
import dev.patrickgold.florisboard.app.res.stringRes
import dev.patrickgold.florisboard.app.ui.Routes
import dev.patrickgold.florisboard.app.ui.components.FlorisErrorCard
import dev.patrickgold.florisboard.app.ui.components.FlorisScreen
import dev.patrickgold.florisboard.app.ui.components.FlorisWarningCard
import dev.patrickgold.florisboard.common.InputMethodUtils
import dev.patrickgold.florisboard.common.android.launchUrl
import dev.patrickgold.jetpref.datastore.model.observeAsState
import dev.patrickgold.jetpref.ui.compose.Preference

@Composable
fun HomeScreen() = FlorisScreen {
    title = stringRes(R.string.settings__home__title)
    backArrowVisible = false

    val navController = LocalNavController.current
    val context = LocalContext.current

    content {
        val isCollapsed by prefs.internal.homeIsBetaToolboxCollapsed.observeAsState()

        val isFlorisBoardEnabled by InputMethodUtils.observeIsFlorisboardEnabled(foregroundOnly = true)
        val isFlorisBoardSelected by InputMethodUtils.observeIsFlorisboardSelected(foregroundOnly = true)
        if (!isFlorisBoardEnabled) {
            FlorisErrorCard(
                modifier = Modifier.padding(8.dp),
                showIcon = false,
                text = stringRes(R.string.settings__home__ime_not_enabled),
                onClick = { InputMethodUtils.showImeEnablerActivity(context) },
            )
        } else if (!isFlorisBoardSelected) {
            FlorisWarningCard(
                modifier = Modifier.padding(8.dp),
                showIcon = false,
                text = stringRes(R.string.settings__home__ime_not_selected),
                onClick = { InputMethodUtils.showImePicker(context) },
            )
        }

        Card(modifier = Modifier.padding(8.dp)) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Beta-access to new Settings UI",
                        style = MaterialTheme.typography.subtitle1,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.weight(1.0f))
                    IconButton(onClick = { this@content.prefs.internal.homeIsBetaToolboxCollapsed.set(!isCollapsed) }) {
                        Icon(
                            painter = painterResource(if (isCollapsed) {
                                R.drawable.ic_keyboard_arrow_down
                            } else {
                                R.drawable.ic_keyboard_arrow_up
                            }),
                            contentDescription = null,
                        )
                    }
                }
                if (!isCollapsed) {
                    Text("You are currently testing out the new Settings of FlorisBoard.\n")
                    Text("This beta release contains a completely rewritten keyboard logic and UI backend, thus some features are still missing. These will get re-added in later beta versions (see below).\n")
                    Text("If you want to give feedback on the development of the new prefs and keyboard logic, please do so in below linked feedback thread:\n")
                    Button(onClick = {
                        context.launchUrl("https://github.com/florisboard/florisboard/discussions/1235")
                    }) {
                        Text("Open Feedback Thread")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Current version: ${BuildConfig.VERSION_NAME}\n")
                    Text("List of unavailable features (and when they will get re-implemented):\n")
                    Text(" - Smartbar (beta07)")
                    Text(" - Password autofill on Android11+ (beta07)")
                    Text(" - Clipboard manager / clipboard row (beta07)")
                    Text(" - Theme customization (new theme engine and look) (beta08)")
                    Text(" - Glide typing (beta09)")
                    Text(" - Emoji view (beta09 or beta10)")
                    Text(" - Landscape fullscreen input (beta09 or beta10)")
                    Text(" - Word suggestions (beta10+, new suggestion algorithm 0.3.15/16)\n")
                    Text("Please do not file issues that these features do not work while the current version is below the intended re-implementation version. Thank you!\n")
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        Preference(
            iconId = R.drawable.ic_language,
            title = stringRes(R.string.settings__localization__title),
            onClick = { navController.navigate(Routes.Settings.Localization) },
        )
        Preference(
            iconId = R.drawable.ic_palette,
            title = stringRes(R.string.settings__theme__title),
            onClick = { navController.navigate(Routes.Settings.Theme) },
        )
        Preference(
            iconId = R.drawable.ic_keyboard,
            title = stringRes(R.string.settings__keyboard__title),
            onClick = { navController.navigate(Routes.Settings.Keyboard) },
        )
        Preference(
            iconId = null,
            title = stringRes(R.string.settings__smartbar__title),
            onClick = { navController.navigate(Routes.Settings.Smartbar) },
        )
        Preference(
            iconId = R.drawable.ic_settings_suggest,
            title = stringRes(R.string.settings__typing__title),
            onClick = { navController.navigate(Routes.Settings.Typing) },
        )
        Preference(
            iconId = R.drawable.ic_spellcheck,
            title = stringRes(R.string.settings__spelling__title),
            onClick = { navController.navigate(Routes.Settings.Spelling) },
        )
        Preference(
            iconId = R.drawable.ic_library_books,
            title = stringRes(R.string.settings__dictionary__title),
            onClick = { navController.navigate(Routes.Settings.Dictionary) },
        )
        Preference(
            iconId = R.drawable.ic_gesture,
            title = stringRes(R.string.settings__gestures__title),
            onClick = { navController.navigate(Routes.Settings.Gestures) },
        )
        Preference(
            iconId = R.drawable.ic_assignment,
            title = stringRes(R.string.settings__clipboard__title),
            onClick = { navController.navigate(Routes.Settings.Clipboard) },
        )
        Preference(
            iconId = R.drawable.ic_adb,
            title = stringRes(R.string.devtools__title),
            onClick = { navController.navigate(Routes.Devtools.Home) },
        )
        Preference(
            iconId = R.drawable.ic_build,
            title = stringRes(R.string.settings__advanced__title),
            onClick = { navController.navigate(Routes.Settings.Advanced) },
        )
        Preference(
            iconId = R.drawable.ic_info,
            title = stringRes(R.string.about__title),
            onClick = { navController.navigate(Routes.Settings.About) },
        )
    }
}
