import Foundation
import SwiftUI

/// Holds the content and the person's projects. Projects are saved to UserDefaults as JSON.
@MainActor
final class LibraryStore: ObservableObject {
    let content: StitchContent
    @Published private(set) var projects: [Project]

    private let storageKey = "stitchbook.projects.v1"

    init() {
        content = Self.loadContent()
        projects = Self.loadProjects(key: storageKey)
    }

    func project(id: String) -> Project? {
        projects.first { $0.id == id }
    }

    /// Starts a project from a pattern and returns its id, or nil when the pattern is unknown.
    func start(patternId: String) -> String? {
        guard let pattern = content.pattern(id: patternId) else { return nil }
        let project = Project(
            id: "\(patternId)-\(Int(Date().timeIntervalSince1970 * 1000))",
            patternId: patternId,
            startedAt: Date(),
            done: Array(repeating: false, count: pattern.steps.count),
            notes: "",
            counters: []
        )
        projects.insert(project, at: 0)
        save()
        return project.id
    }

    func update(_ projectId: String, _ change: (inout Project) -> Void) {
        guard let index = projects.firstIndex(where: { $0.id == projectId }) else { return }
        change(&projects[index])
        save()
    }

    func toggleStep(_ projectId: String, index: Int) {
        update(projectId) { project in
            guard project.done.indices.contains(index) else { return }
            project.done[index].toggle()
        }
    }

    func remove(_ projectId: String) {
        projects.removeAll { $0.id == projectId }
        save()
    }

    private func save() {
        guard let data = try? JSONEncoder().encode(projects) else { return }
        UserDefaults.standard.set(data, forKey: storageKey)
    }

    private static func loadContent() -> StitchContent {
        guard
            let url = Bundle.main.url(forResource: "content", withExtension: "json"),
            let data = try? Data(contentsOf: url),
            let content = try? JSONDecoder().decode(StitchContent.self, from: data)
        else {
            // The content file is part of the app. If it is missing, the build is broken.
            fatalError("content.json is missing from the app bundle or does not match the model.")
        }
        return content
    }

    private static func loadProjects(key: String) -> [Project] {
        guard let data = UserDefaults.standard.data(forKey: key) else { return [] }
        return (try? JSONDecoder().decode([Project].self, from: data)) ?? []
    }
}
