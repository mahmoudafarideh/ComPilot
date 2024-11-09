package m.a.compilot.navigation

import android.os.Build
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import m.a.compilot.navigation.result.NavigationResultHandler
import java.io.Serializable

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

inline fun <reified T : Serializable> Bundle.getSafeOptionalSerializable(key: String): T? =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        when {
            this.containsKey(key) -> this.getSerializable(key, T::class.java)
            else -> null
        }
    } else {
        @Suppress("DEPRECATION")
        this.getSerializable(key) as T?
    }

@Composable
fun NavBackStackEntry.NavigationResultHandler(
    action: NavigationResultHandler.() -> Unit
) {
    LaunchedEffect(Unit) {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            action(NavigationResultHandler(this@NavigationResultHandler.savedStateHandle))
        }
    }
}
