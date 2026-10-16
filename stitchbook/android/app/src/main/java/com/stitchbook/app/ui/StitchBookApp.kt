package com.stitchbook.app.ui

import androidx.compose.foundation.layout.padding
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.stitchbook.app.ProjectsViewModel
import com.stitchbook.app.ui.screens.HomeScreen
import com.stitchbook.app.ui.screens.LearnScreen
import com.stitchbook.app.ui.screens.PatternDetailScreen
import com.stitchbook.app.ui.screens.PatternsScreen
import com.stitchbook.app.ui.screens.ProjectScreen
import com.stitchbook.app.ui.screens.ProjectsScreen
import com.stitchbook.app.ui.screens.ToolsScreen
import com.stitchbook.app.ui.screens.TutorialScreen

enum class Tab(val route: String, val label: String, val icon: ImageVector) {
    Home("home", "Home", Icons.Filled.Home),
    Learn("learn", "Learn", Icons.AutoMirrored.Filled.MenuBook),
    Patterns("patterns", "Patterns", Icons.Filled.GridView),
    Projects("projects", "Projects", Icons.Filled.Bookmark),
    Tools("tools", "Tools", Icons.Filled.Build),
}

@Composable
fun StitchBookApp(vm: ProjectsViewModel = viewModel()) {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route
    val content = vm.content

    Scaffold(
        bottomBar = {
            if (Tab.entries.any { it.route == route }) {
                NavigationBar {
                    Tab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = route == tab.route,
                            onClick = {
                                nav.navigate(tab.route) {
                                    popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavHost(nav, startDestination = Tab.Home.route, modifier = Modifier.padding(padding)) {
            composable(Tab.Home.route) {
                HomeScreen(
                    vm = vm,
                    onTutorial = { nav.navigate("tutorial/$it") },
                    onProject = { nav.navigate("project/$it") },
                    onTab = { nav.navigate(it) },
                )
            }
            composable(Tab.Learn.route) { LearnScreen(vm, onTutorial = { nav.navigate("tutorial/$it") }) }
            composable(Tab.Patterns.route) { PatternsScreen(vm, onPattern = { nav.navigate("pattern/$it") }) }
            composable(Tab.Projects.route) { ProjectsScreen(vm, onProject = { nav.navigate("project/$it") }) }
            composable(Tab.Tools.route) { ToolsScreen() }

            composable("tutorial/{id}") { back ->
                val id = back.arguments?.getString("id").orEmpty()
                TutorialScreen(content.tutorial(id), onBack = { nav.popBackStack() })
            }
            composable("pattern/{id}") { back ->
                val id = back.arguments?.getString("id").orEmpty()
                PatternDetailScreen(
                    vm = vm,
                    pattern = content.pattern(id),
                    onBack = { nav.popBackStack() },
                    onStarted = { projectId ->
                        nav.navigate("project/$projectId") { popUpTo("pattern/{id}") { inclusive = true } }
                    },
                )
            }
            composable("project/{id}") { back ->
                val id = back.arguments?.getString("id").orEmpty()
                ProjectScreen(vm, projectId = id, onBack = { nav.popBackStack() })
            }
        }
    }
}
