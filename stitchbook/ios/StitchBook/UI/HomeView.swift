import SwiftUI

struct HomeView: View {
    @EnvironmentObject private var library: LibraryStore

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                hero

                if let latest = library.projects.first, let pattern = library.content.pattern(id: latest.patternId) {
                    NavigationLink(value: latest.id) {
                        VStack(alignment: .leading, spacing: 8) {
                            Text("CONTINUE YOUR PROJECT").font(.caption.weight(.semibold)).foregroundStyle(Palette.inkSoft)
                            Text("\(pattern.emoji) \(pattern.title)").font(.headline).foregroundStyle(Palette.ink)
                            StepProgress(done: latest.done.filter { $0 }.count, total: latest.done.count)
                        }
                        .card()
                    }
                    .buttonStyle(.plain)
                }

                Text("Quick start").font(.title3.weight(.bold)).foregroundStyle(Palette.ink)
                HStack(spacing: 10) {
                    quickTile("Learn stitches", icon: "book.fill") { LearnView() }
                    quickTile("Browse patterns", icon: "square.grid.2x2.fill") { PatternsView() }
                    quickTile("My projects", icon: "bookmark.fill") { ProjectsView() }
                }
                quickTile("Shaping calculator", icon: "wrench.and.screwdriver.fill") { ToolsView() }

                Text("Beginner friendly tutorials").font(.title3.weight(.bold)).foregroundStyle(Palette.ink)
                ForEach(library.content.tutorials.filter { $0.level == "Easy" || $0.level == "Beginner" }.prefix(4)) { tutorial in
                    NavigationLink {
                        TutorialView(tutorial: tutorial)
                    } label: {
                        HStack(spacing: 12) {
                            ColorCover(color: Color(hex: tutorial.color), size: 52)
                            VStack(alignment: .leading, spacing: 2) {
                                Text(tutorial.title).font(.headline).foregroundStyle(Palette.ink)
                                Text("\(tutorial.minutes) min · \(craftName(tutorial.craft))")
                                    .font(.subheadline).foregroundStyle(Palette.inkSoft)
                            }
                            Spacer()
                            Image(systemName: "chevron.right").foregroundStyle(Palette.inkSoft)
                        }
                        .card()
                    }
                    .buttonStyle(.plain)
                }

                VStack(alignment: .leading, spacing: 10) {
                    Text("About StitchBook").font(.headline).foregroundStyle(Palette.ink)
                    Text(Support.aboutText).font(.footnote).foregroundStyle(Palette.inkSoft)
                    if let url = Support.mailURL(subject: "StitchBook feedback") {
                        Link("Contact us or report a problem", destination: url)
                            .font(.subheadline.weight(.semibold))
                            .foregroundStyle(Palette.coralDark)
                    }
                }
                .card()
            }
            .padding(16)
        }
        .background(Palette.cream)
        .navigationTitle("StitchBook")
        .navigationDestination(for: String.self) { projectId in
            ProjectView(projectId: projectId)
        }
    }

    private var hero: some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Easy to follow crochet & knit")
                .font(.largeTitle.weight(.heavy))
                .foregroundStyle(.white)
            Text("Step by step tutorials, patterns, a stitch counter, and project tracking. Free, with no ads.")
                .font(.body)
                .foregroundStyle(.white.opacity(0.95))
        }
        .padding(22)
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(Palette.coral, in: RoundedRectangle(cornerRadius: 24))
    }

    private func quickTile<Destination: View>(_ title: String, icon: String, @ViewBuilder destination: () -> Destination) -> some View {
        NavigationLink {
            destination()
        } label: {
            VStack(spacing: 8) {
                Image(systemName: icon).font(.title2).foregroundStyle(Palette.coral)
                Text(title).font(.caption.weight(.semibold)).foregroundStyle(Palette.ink).multilineTextAlignment(.center)
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 14)
            .background(.white, in: RoundedRectangle(cornerRadius: 16))
            .overlay(RoundedRectangle(cornerRadius: 16).stroke(Palette.line, lineWidth: 1))
        }
        .buttonStyle(.plain)
    }
}
