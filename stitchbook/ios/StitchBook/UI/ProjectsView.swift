import SwiftUI

struct ProjectsView: View {
    @EnvironmentObject private var library: LibraryStore

    var body: some View {
        Group {
            if library.projects.isEmpty {
                VStack(spacing: 8) {
                    Text("No projects yet").font(.headline).foregroundStyle(Palette.ink)
                    Text("Pick a pattern and start a project. Your progress is saved on this device.")
                        .font(.subheadline)
                        .foregroundStyle(Palette.inkSoft)
                        .multilineTextAlignment(.center)
                }
                .padding(24)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else {
                ScrollView {
                    VStack(spacing: 12) {
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
    }
}
