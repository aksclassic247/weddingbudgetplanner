import SwiftUI

enum Palette {
    static let coral = Color(hex: "#E76F5E")
    static let coralDark = Color(hex: "#C9523F")
    static let peach = Color(hex: "#FBE8DA")
    static let peachDeep = Color(hex: "#F6D5C0")
    static let cream = Color(hex: "#FFF8F3")
    static let ink = Color(hex: "#2B2320")
    static let inkSoft = Color(hex: "#6E615B")
    static let line = Color(hex: "#EEDFD6")
    static let success = Color(hex: "#5E9E63")
}

extension Color {
    init(hex: String) {
        let cleaned = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        let value = UInt64(cleaned, radix: 16) ?? 0
        let red = Double((value >> 16) & 0xFF) / 255
        let green = Double((value >> 8) & 0xFF) / 255
        let blue = Double(value & 0xFF) / 255
        self.init(red: red, green: green, blue: blue)
    }
}

/// A white rounded card with a thin border. Used across the app.
struct CardBackground: ViewModifier {
    var fill: Color = .white

    func body(content: Content) -> some View {
        content
            .padding(14)
            .frame(maxWidth: .infinity, alignment: .leading)
            .background(fill, in: RoundedRectangle(cornerRadius: 16))
            .overlay(RoundedRectangle(cornerRadius: 16).stroke(Palette.line, lineWidth: 1))
    }
}

extension View {
    func card(fill: Color = .white) -> some View {
        modifier(CardBackground(fill: fill))
    }
}
