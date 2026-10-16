import SwiftUI

@main
struct StitchBookApp: App {
    @StateObject private var library = LibraryStore()

    var body: some Scene {
        WindowGroup {
            RootTabView()
                .environmentObject(library)
                .tint(Palette.coral)
        }
    }
}

struct RootTabView: View {
    var body: some View {
        TabView {
            NavigationStack { HomeView() }
                .tabItem { Label("Home", systemImage: "house.fill") }
            NavigationStack { LearnView() }
                .tabItem { Label("Learn", systemImage: "book.fill") }
            NavigationStack { PatternsView() }
                .tabItem { Label("Patterns", systemImage: "square.grid.2x2.fill") }
            NavigationStack { ProjectsView() }
                .tabItem { Label("Projects", systemImage: "bookmark.fill") }
            NavigationStack { ToolsView() }
                .tabItem { Label("Tools", systemImage: "wrench.and.screwdriver.fill") }
        }
    }
}
