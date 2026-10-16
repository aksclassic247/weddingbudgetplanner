import SwiftUI

struct LearnView: View {
    @EnvironmentObject private var library: LibraryStore
    @State private var craft = "knitting"

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 12) {
                Picker("Craft", selection: $craft) {
                    Text("Knitting").tag("knitting")
                    Text("Crochet").tag("crochet")
                }
                .pickerStyle(.segmented)

                Text("Tutorials").font(.title3.weight(.bold)).foregroundStyle(Palette.ink)
                ForEach(library.content.tutorials.filter { $0.craft == craft }) { tutorial in
                    NavigationLink {
                        TutorialView(tutorial: tutorial)
                    } label: {
                        HStack(spacing: 12) {
                            ColorCover(color: Color(hex: tutorial.color), size: 64)
                            VStack(alignment: .leading, spacing: 4) {
                                Text(tutorial.title).font(.headline).foregroundStyle(Palette.ink)
                                Text(tutorial.summary).font(.subheadline).foregroundStyle(Palette.inkSoft)
                                HStack(spacing: 10) {
                                    LevelBadge(level: tutorial.level)
                                    Text("\(tutorial.minutes) min").font(.caption).foregroundStyle(Palette.inkSoft)
                                }
                            }
                            Spacer(minLength: 0)
                        }
                        .card()
                    }
                    .buttonStyle(.plain)
                }

                Text("Abbreviations").font(.title3.weight(.bold)).foregroundStyle(Palette.ink)
                ForEach(library.content.stitches.filter { $0.craft == craft }) { stitch in
                    HStack(alignment: .top, spacing: 12) {
                        Text(stitch.abbr)
                            .font(.headline)
                            .foregroundStyle(Palette.coralDark)
                            .frame(width: 64, alignment: .leading)
                        VStack(alignment: .leading, spacing: 2) {
                            Text(stitch.name).font(.subheadline.weight(.semibold)).foregroundStyle(Palette.ink)
                            Text(stitch.meaning).font(.footnote).foregroundStyle(Palette.inkSoft)
                        }
                    }
                    .card()
                }
            }
            .padding(16)
        }
        .background(Palette.cream)
        .navigationTitle("Learn")
    }
}
