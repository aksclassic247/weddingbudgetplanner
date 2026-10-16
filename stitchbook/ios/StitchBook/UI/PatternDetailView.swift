import SwiftUI

struct PatternDetailView: View {
    @EnvironmentObject private var library: LibraryStore
    let pattern: Pattern
    @State private var startedProjectId: String?
    @State private var showProject = false

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 14) {
                ZStack {
                    RoundedRectangle(cornerRadius: 24).fill(Color(hex: pattern.color)).frame(height: 170)
                    Text(pattern.emoji).font(.system(size: 72))
                }

                Text(pattern.title).font(.title2.weight(.heavy)).foregroundStyle(Palette.ink)
                HStack(spacing: 12) {
                    LevelBadge(level: pattern.level)
                    Text(craftName(pattern.craft)).font(.caption).foregroundStyle(Palette.inkSoft)
                    Text(pattern.time).font(.caption).foregroundStyle(Palette.inkSoft)
                }

                VStack(alignment: .leading, spacing: 6) {
                    Text("Materials").font(.headline).foregroundStyle(Palette.ink)
                    ForEach(pattern.materials, id: \.self) { item in
                        Text("• \(item)").font(.subheadline).foregroundStyle(Palette.ink)
                    }
                    if let gauge = pattern.gauge {
                        Text("Gauge: \(gauge)").font(.footnote).foregroundStyle(Palette.inkSoft)
                    }
                }
                .card()

                Text("Instructions").font(.title3.weight(.bold)).foregroundStyle(Palette.ink)
                ForEach(pattern.steps, id: \.self) { step in
                    VStack(alignment: .leading, spacing: 4) {
                        Text(step.label).font(.subheadline.weight(.bold)).foregroundStyle(Palette.coral)
                        Text(step.text).font(.body).foregroundStyle(Palette.ink)
                        if let stitches = step.stitches {
                            Text("\(stitches) stitches").font(.caption).foregroundStyle(Palette.inkSoft)
                        }
                    }
                    .card()
                }

                Button {
                    if let id = library.start(patternId: pattern.id) {
                        startedProjectId = id
                        showProject = true
                    }
                } label: {
                    Text("Start this project")
                        .font(.headline)
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 14)
                        .background(Palette.coral, in: RoundedRectangle(cornerRadius: 14))
                        .foregroundStyle(.white)
                }

                if let url = Support.mailURL(subject: "Pattern problem: \(pattern.title)") {
                    Link("Report a problem with this pattern", destination: url)
                        .font(.footnote.weight(.semibold))
                        .foregroundStyle(Palette.coralDark)
                }
                VStack(alignment: .leading, spacing: 4) {
                    Text(pattern.testLabel).font(.subheadline.weight(.bold)).foregroundStyle(Palette.ink)
                    if let credit = pattern.credit, !credit.isEmpty {
                        Text(credit).font(.footnote).foregroundStyle(Palette.inkSoft)
                    }
                }
                .card(fill: pattern.testedOn == nil ? Palette.peach : .white)
                Text("Check these instructions against your own yarn and tools before you begin.")
                    .font(.footnote)
                    .foregroundStyle(Palette.inkSoft)
            }
            .padding(16)
        }
        .background(Palette.cream)
        .navigationTitle(pattern.title)
        .navigationBarTitleDisplayMode(.inline)
        .navigationDestination(isPresented: $showProject) {
            ProjectView(projectId: startedProjectId ?? "")
        }
    }
}
