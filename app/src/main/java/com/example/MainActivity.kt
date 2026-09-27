package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.PeymanTopBar
import com.example.ui.screens.*
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.PeymanViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Persian language RTL layout direction
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    PeymanApp()
                }
            }
        }
    }
}

@Composable
fun PeymanApp(viewModel: PeymanViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showSavedProjectsDialog by remember { mutableStateOf(false) }
    var showAppInfoDialog by remember { mutableStateOf(false) }

    val savedProjects by viewModel.savedProjects.collectAsState()

    Scaffold(
        topBar = {
            PeymanTopBar(
                onSavedProjectsClick = { showSavedProjectsDialog = true },
                onInfoClick = { showAppInfoDialog = true }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = NavyPrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = null) },
                    label = { Text("بخشنامه ۵۰۹۰", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyPrimary,
                        selectedTextColor = AmberAccent,
                        indicatorColor = AmberAccent,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.testTag("nav_tab_5090")
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = null) },
                    label = { Text("فهرست بها", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyPrimary,
                        selectedTextColor = AmberAccent,
                        indicatorColor = AmberAccent,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.testTag("nav_tab_fehrest")
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Percent, contentDescription = null) },
                    label = { Text("ضرایب پیمان", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyPrimary,
                        selectedTextColor = AmberAccent,
                        indicatorColor = AmberAccent,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.testTag("nav_tab_coefficients")
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.TrendingUp, contentDescription = null) },
                    label = { Text("تعدیل آحاد بها", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyPrimary,
                        selectedTextColor = AmberAccent,
                        indicatorColor = AmberAccent,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.testTag("nav_tab_adjustment")
                )
                NavigationBarItem(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    icon = { Icon(Icons.Default.Gavel, contentDescription = null) },
                    label = { Text("قوانین و نشریه", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NavyPrimary,
                        selectedTextColor = AmberAccent,
                        indicatorColor = AmberAccent,
                        unselectedIconColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                        unselectedTextColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.testTag("nav_tab_regulations")
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when (selectedTab) {
            0 -> DelayCalculatorScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            1 -> FehrestBahaScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            2 -> CoefficientsScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            3 -> AdjustmentScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            4 -> RegulationsScreen(
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    if (showSavedProjectsDialog) {
        SavedProjectsDialog(
            projects = savedProjects,
            onDelete = { viewModel.deleteSavedProject(it) },
            onDismiss = { showSavedProjectsDialog = false }
        )
    }

    if (showAppInfoDialog) {
        AppInfoDialog(onDismiss = { showAppInfoDialog = false })
    }
}
