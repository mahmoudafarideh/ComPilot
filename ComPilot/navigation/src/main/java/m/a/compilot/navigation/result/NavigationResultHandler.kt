package m.a.compilot.navigation.result

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle


class NavigationResultHandler(private val savedStateHandle: SavedStateHandle) {
    fun handleNavigationResult(key: String, action: ComposeResult.() -> Unit) {
        if (savedStateHandle.contains(key)) {
            savedStateHandle.get<Bundle>(key)?.let { action(ComposeResult(it)) }
            savedStateHandle.remove<Bundle>(key)
        }
    }
}