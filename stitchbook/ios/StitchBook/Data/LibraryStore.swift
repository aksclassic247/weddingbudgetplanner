import Foundation
import SwiftUI

enum AttachmentKind: String {
    case pdfs
    case photos
}

/// Holds the content and the person's projects. Projects are saved to UserDefaults as JSON.
@MainActor
final class LibraryStore: ObservableObject {
    let content: StitchContent
    @Published private(set) var projects: [Project]

    private let storageKey = "stitchbook.projects.v2"
    private let legacyKey = "stitchbook.projects.v1"

    init() {
        content = Self.loadContent()
        projects = Self.loadProjects(key: storageKey, legacyKey: legacyKey)
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

    func addRepeat(_ projectId: String, name: String, sourceCounterId: UUID, rowsPerRepeat: Int) {
        update(projectId) { project in
            project.repeats = project.repeatCounters + [RepeatCounter(name: name, sourceCounterId: sourceCounterId, rowsPerRepeat: rowsPerRepeat)]
        }
    }

    func removeRepeat(_ projectId: String, repeatId: UUID) {
        update(projectId) { project in
            project.repeats = project.repeatCounters.filter { $0.id != repeatId }
        }
    }

    /// Turns the daily reminder on at hour:minute, or off when hour is nil.
    func setReminder(_ projectId: String, hour: Int?, minute: Int?, patternTitle: String) {
        update(projectId) { project in
            project.reminderHour = hour
            project.reminderMinute = minute
        }
        if let hour, let minute {
            ReminderService.schedule(projectId: projectId, title: patternTitle, hour: hour, minute: minute)
        } else {
            ReminderService.cancel(projectId: projectId)
        }
    }

    /// Saves a PDF or photo into the project's folder.
    func addAttachment(_ projectId: String, kind: AttachmentKind, data: Data, extension ext: String) throws {
        let name = try ProjectFiles.write(data, projectId: projectId, kind: kind.rawValue, extension: ext)
        update(projectId) { project in
            switch kind {
            case .pdfs: project.pdfs = project.pdfFiles + [name]
            case .photos: project.photos = project.photoFiles + [name]
            }
        }
    }

    func removeAttachment(_ projectId: String, kind: AttachmentKind, name: String) {
        ProjectFiles.delete(projectId: projectId, kind: kind.rawValue, name: name)
        update(projectId) { project in
            switch kind {
            case .pdfs: project.pdfs = project.pdfFiles.filter { $0 != name }
            case .photos: project.photos = project.photoFiles.filter { $0 != name }
            }
        }
    }

    func remove(_ projectId: String) {
        ReminderService.cancel(projectId: projectId)
        ProjectFiles.deleteProject(projectId: projectId)
        projects.removeAll { $0.id == projectId }
        save()
    }

    /// Writes a backup file and returns its URL, so it can be shared. Photos and PDFs are not included.
    func backupFileURL() -> URL? {
        guard let data = try? JSONEncoder().encode(projects) else { return nil }
        let url = FileManager.default.temporaryDirectory.appendingPathComponent("stitchbook-backup.json")
        try? data.write(to: url)
        return url
    }

    /// Adds projects from a backup. Projects whose id already exists are skipped. Returns how many were added.
    func restore(from data: Data) -> Int {
        guard let incoming = try? JSONDecoder().decode([Project].self, from: data) else { return 0 }
        let existing = Set(projects.map(\.id))
        let added = incoming.filter { !existing.contains($0.id) }
        if !added.isEmpty {
            projects = added + projects
            save()
        }
        return added.count
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

    private static func loadProjects(key: String, legacyKey: String) -> [Project] {
        let data = UserDefaults.standard.data(forKey: key) ?? UserDefaults.standard.data(forKey: legacyKey)
        guard let data else { return [] }
        return (try? JSONDecoder().decode([Project].self, from: data)) ?? []
    }
}
