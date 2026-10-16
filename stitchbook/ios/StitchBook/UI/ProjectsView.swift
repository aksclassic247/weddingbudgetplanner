import SwiftUI
import UniformTypeIdentifiers

struct ProjectsView: View {
    @EnvironmentObject private var library: LibraryStore
    @State private var showImporter = false
    @State private var restoreMessage: String?

    var body: some View {
        Group {
            if library.projects.isEmpty {
                VStack(spacing: 16) {
                    backupButtons
                    VStack(spacing: 8) {
                        Text("No projects yet").font(.headline).foregroundStyle(Palette.ink)
                        Text("Pick a pattern and start a project. Your progress is saved on this device.")
                            .font(.subheadline)
                            .foregroundStyle(Palette.inkSoft)
                            .multilineTextAlignment(.center)
                    }
                    .padding(24)
                    Spacer()
                }
                .padding(16)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else {
                ScrollView {
                    VStack(spacing: 12) {
                        backupButtons
                        ForEach(library.projects) { project in
                            if let pattern = library.content.pattern(id: project.patternId) {
                                NavigationLink {
                                    ProjectView(projectId: project.id)
                                } label: {
                                    HStack(spacing: 12) {
                                        ColorCover(color: Color(hex: pattern.color), size: 64, emoji: pattern.emoji)
                                        VStack(alignment: .leading, spacing: 8) {
                                            Text(pattern.title).font(.headline).foregroundStyle(Palette.ink)
                                            StepProgress(done: project.done.filter { $0 }.count, total: project.done.count)
                                            if !project.done.isEmpty && project.done.allSatisfy({ $0 }) {
                                                Text("Finished").font(.caption.weight(.bold)).foregroundStyle(Palette.success)
                                            }
                                        }
                                        Spacer(minLength: 0)
                                        Image(systemName: "chevron.right").foregroundStyle(Palette.inkSoft)
                                    }
                                    .card()
                                }
                                .buttonStyle(.plain)
                            }
                        }
                    }
                    .padding(16)
                }
            }
        }
        .background(Palette.cream)
        .navigationTitle("My projects")
        .fileImporter(isPresented: $showImporter, allowedContentTypes: [.json]) { result in
            restore(result)
        }
        .alert("Backup", isPresented: Binding(get: { restoreMessage != nil }, set: { if !$0 { restoreMessage = nil } })) {
            Button("OK", role: .cancel) {}
        } message: {
            Text(restoreMessage ?? "")
        }
    }

    private var backupButtons: some View {
        HStack(spacing: 10) {
            if let url = library.backupFileURL() {
                ShareLink(item: url) {
                    Label("Export backup", systemImage: "square.and.arrow.up")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)
            }
            Button {
                showImporter = true
            } label: {
                Label("Restore backup", systemImage: "square.and.arrow.down")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
        }
    }

    private func restore(_ result: Result<URL, Error>) {
        switch result {
        case .failure:
            restoreMessage = "That file could not be opened."
        case .success(let url):
            let accessed = url.startAccessingSecurityScopedResource()
            defer { if accessed { url.stopAccessingSecurityScopedResource() } }
            guard let data = try? Data(contentsOf: url) else {
                restoreMessage = "That file could not be read."
                return
            }
            let added = library.restore(from: data)
            restoreMessage = added > 0 ? "Added \(added) project(s) from the backup." : "No new projects were found in that file."
        }
    }
}
