package m.a.compilot

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import kotlinx.coroutines.delay
import m.a.compilot.navigation.LocalNavController
import m.a.compilot.navigation.NavigationResultHandler
import m.a.compilot.navigation.comPilotNavController
import m.a.compilot.routes.bottomSheet
import m.a.compilot.routes.dialog
import m.a.compilot.routes.navigator
import m.a.compilot.routes.screen
import m.a.compilot.ui.theme.ComPilotTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComPilotTheme {
                val navigation = rememberNavController()
                val bottomSheetNavigator = rememberBottomSheetNavigator()

                CompositionLocalProvider(LocalNavController provides navigation) {
                    navigation.navigatorProvider += bottomSheetNavigator
                    ModalBottomSheetLayout(
                        sheetContentColor = Color.Unspecified,
                        modifier = Modifier,
                        bottomSheetNavigator = bottomSheetNavigator
                    ) {
                        NavHost(
                            navController = navigation,
                            startDestination = FullScreenRoute("Tesla").navigator()
                        ) {
                            FullScreenRoute.screen(this) {
                                val navController = LocalNavController.comPilotNavController
                                LaunchedEffect(Unit) {
                                    if (it.argument.title == "Tesla") {
                                        delay(2_000)
                                        navController.navigate(BottomSheetRoute("Label").navigator)
                                    }
                                }
                                val context = LocalContext.current
                                it.navBackStackEntry.NavigationResultHandler {
                                    this.handleNavigationResult("DialogResult") {
                                        Toast.makeText(
                                            context,
                                            "The arg is ${this.getInt("DialogId")}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate(
                                            FullScreenWithNestedArgRoute(
                                                nested = FullScreenWithNestedArgRoute.NestedData(
                                                    1,
                                                    "Name"
                                                ),
                                                child = FullScreenWithNestedArgRoute.Child.Child1
                                            ).navigator
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Red)
                                )
                            }
                            FullScreenWithNestedArgRoute.screen(this) {
                                val navController = LocalNavController.comPilotNavController
                                LaunchedEffect(Unit) {
                                    delay(2_000)
                                    navController
                                        .clearBackStack()
                                        .navigate(FullScreenRoute("Another Title").navigator)
                                }
                                val context = LocalContext.current
                                LaunchedEffect(Unit) {
                                    Toast.makeText(
                                        context,
                                        "The arg is ${it.argument}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Cyan)
                                )
                            }
                            DialogRoute.dialog(this) {
                                val navController = LocalNavController.comPilotNavController
                                LaunchedEffect(Unit) {
                                    delay(2_000)
                                    navController
                                        .setResult("DialogResult") {
                                            this.setInt("DialogId", it.argument.id)
                                        }
                                        .safePopBackStack()
                                    navController.safePopBackStack()
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .background(Color.Blue)
                                )
                            }
                            BottomSheetRoute.bottomSheet(this) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .background(Color.Green)
                                )
                            }
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