import SwiftUI

struct LevelBadge: View {
    let level: String

    private var tint: Color {
        switch level {
        case "Easy": return Color(hex: "#7BC47F")
        case "Beginner": return Color(hex: "#F2A65A")
        case "Intermediate": return Palette.coral
        default: return Color(hex: "#8E6CC9")
        }
    }

    var body: some View {
        HStack(spacing: 6) {
            Circle().fill(tint).frame(width: 8, height: 8)
            Text(level).font(.caption.weight(.semibold)).foregroundStyle(Palette.ink)
        }
        .padding(.horizontal, 10)
        .padding(.vertical, 4)
        .background(tint.opacity(0.15), in: Capsule())
    }
}

struct StepProgress: View {
    let done: Int
    let total: Int

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            ProgressView(value: total == 0 ? 0 : Double(done) / Double(total))
                .tint(Palette.coral)
            Text("\(done)/\(total) done")
                .font(.caption)
                .foregroundStyle(Palette.inkSoft)
        }
    }
}

/// A colored block with an emoji, used instead of photos so the app ships with no image files.
struct ColorCover: View {
    let color: Color
    var size: CGFloat = 64
    var emoji: String? = nil

    var body: some View {
        RoundedRectangle(cornerRadius: 12)
            .fill(color)
            .frame(width: size, height: size)
            .overlay {
                if let emoji {
                    Text(emoji).font(.system(size: size * 0.5))
                }
            }
    }
}

func craftName(_ craft: String) -> String {
    craft == "knitting" ? "Knitting" : "Crochet"
}
