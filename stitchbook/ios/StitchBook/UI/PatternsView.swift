import SwiftUI

struct PatternsView: View {
    @EnvironmentObject private var library: LibraryStore
    @State private var level = "All"

    private let levels = ["All", "Easy", "Beginner", "Intermediate", "Advanced"]

    private var visible: [Pattern] {
        library.content.patterns.filter { level == "All" || $0.level == level }
    }

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 14) {
                Text("Patterns for every skill level. Start one and track each round.")
                    .font(.subheadline)
                    .foregroundStyle(Palette.inkSoft)

                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 8) {
                        ForEach(levels, id: \.self) { label in
                            Button(label) { level = label }
                                .font(.footnote.weight(.semibold))
                                .padding(.horizontal, 14)
                                .padding(.vertical, 8)
                                .background(level == label ? Palette.ink : .white, in: Capsule())
                                .foregroundStyle(level == label ? .white : Palette.inkSoft)
                                .overlay(Capsule().stroke(Palette.line, lineWidth: level == label ? 0 : 1))
                        }
                    }
                }

                LazyVGrid(columns: [GridItem(.flexible(), spacing: 12), GridItem(.flexible(), spacing: 12)], spacing: 12) {
                    ForEach(visible) { pattern in
                        NavigationLink {
                            PatternDetailView(pattern: pattern)
                        } label: {
                            VStack(alignment: .leading, spacing: 0) {
                                ZStack {
                                    Color(hex: pattern.color).frame(height: 110)
                                    Text(pattern.emoji).font(.system(size: 44))
                                }
                                VStack(alignment: .leading, spacing: 6) {
                                    Text(pattern.title).font(.subheadline.weight(.bold)).foregroundStyle(Palette.ink)
                                        .frame(maxWidth: .infinity, alignment: .leading)
                                    LevelBadge(level: pattern.level)
                                    Text(craftName(pattern.craft)).font(.caption).foregroundStyle(Palette.inkSoft)
                                }
                                .padding(12)
                            }
                            .background(.white, in: RoundedRectangle(cornerRadius: 16))
                            .overlay(RoundedRectangle(cornerRadius: 16).stroke(Palette.line, lineWidth: 1))
                            .clipShape(RoundedRectangle(cornerRadius: 16))
                        }
                        .buttonStyle(.plain)
                    }
                }
            }
            .padding(16)
        }
        .background(Palette.cream)
        .navigationTitle("Get inspired")
    }
}
