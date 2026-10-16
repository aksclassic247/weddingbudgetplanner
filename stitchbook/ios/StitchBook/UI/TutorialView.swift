import SwiftUI

struct TutorialView: View {
    let tutorial: Tutorial

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 14) {
                VStack(alignment: .leading, spacing: 6) {
                    Text(craftName(tutorial.craft).uppercased())
                        .font(.caption.weight(.bold))
                        .foregroundStyle(.white.opacity(0.9))
                    Text(tutorial.title)
                        .font(.title.weight(.heavy))
                        .foregroundStyle(.white)
                }
                .padding(20)
                .frame(maxWidth: .infinity, minHeight: 130, alignment: .bottomLeading)
                .background(Color(hex: tutorial.color), in: RoundedRectangle(cornerRadius: 24))

                HStack(spacing: 12) {
                    LevelBadge(level: tutorial.level)
                    Text("\(tutorial.minutes) min").font(.caption).foregroundStyle(Palette.inkSoft)
                }

                Text(tutorial.summary).font(.body).foregroundStyle(Palette.ink)

                Text("Step by step").font(.title3.weight(.bold)).foregroundStyle(Palette.ink)
                ForEach(Array(tutorial.steps.enumerated()), id: \.offset) { index, step in
                    HStack(alignment: .top, spacing: 12) {
                        Text("\(index + 1)")
                            .font(.subheadline.weight(.heavy))
                            .foregroundStyle(Palette.coralDark)
                            .frame(width: 30, height: 30)
                            .background(Palette.peach, in: Circle())
                        VStack(alignment: .leading, spacing: 4) {
                            Text(step.title).font(.headline).foregroundStyle(Palette.ink)
                            Text(step.text).font(.subheadline).foregroundStyle(Palette.inkSoft)
                        }
                    }
                    .card()
                }
            }
            .padding(16)
        }
        .background(Palette.cream)
        .navigationTitle(tutorial.title)
        .navigationBarTitleDisplayMode(.inline)
    }
}
