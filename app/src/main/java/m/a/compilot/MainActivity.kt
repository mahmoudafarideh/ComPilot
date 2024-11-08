package m.a.compilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import m.a.compilot.navigation.LocalNavController
import m.a.compilot.navigation.comPilotNavController
import m.a.compilot.routes.dialog
import m.a.compilot.routes.navigator
import m.a.compilot.routes.screen
import m.a.compilot.ui.theme.ComPilotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComPilotTheme {
                val navigation = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navigation) {
                    NavHost(
                        navController = navigation,
                        startDestination = FullScreenRoute("Tesla").navigator()
                    ) {
                        FullScreenRoute.screen(this) {
                            val navController = LocalNavController.comPilotNavController
                            LaunchedEffect(Unit) {
                                delay(2_000)
                                navController.navigate(DialogRoute(1).navigator)
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Red)
                            )
                        }
                        DialogRoute.dialog(this) {
                            val navController = LocalNavController.comPilotNavController
                            LaunchedEffect(Unit) {
                                delay(2_000)
                                navController.safePopBackStack()
                                navController.safePopBackStack()
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(Color.Blue)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComPilotTheme {
        Greeting("Android")
    }
}