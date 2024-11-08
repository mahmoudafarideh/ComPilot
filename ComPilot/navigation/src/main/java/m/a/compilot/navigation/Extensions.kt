package m.a.compilot.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController

val LocalNavController = compositionLocalOf<NavController> { error("No NavController found!") }

val ProvidableCompositionLocal<NavController>.comPilotNavController: ComPilotNavController
    @Composable
    get() {
        val navigation = this.current
        val safeNavController = remember {
            ComPilotNavControllerImp(navigation, navigation.currentDestination?.id)
        }
        return safeNavController
    }

