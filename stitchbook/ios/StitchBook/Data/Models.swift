import Foundation

struct TutorialStep: Codable, Hashable {
    let title: String
    let text: String
}

struct Tutorial: Codable, Identifiable, Hashable {
    let id: String
    let craft: String
    let title: String
    let level: String
    let minutes: Int
    let color: String
    let summary: String
    let steps: [TutorialStep]
}

struct Stitch: Codable, Identifiable, Hashable {
    let abbr: String
    let name: String
    let craft: String
    let meaning: String

    var id: String { abbr }
}

struct PatternStep: Codable, Hashable {
    let label: String
    let text: String
    let stitches: Int?
}

struct Pattern: Codable, Identifiable, Hashable {
    let id: String
    let craft: String
    let title: String
    let level: String
    let color: String
    let emoji: String
    let time: String
    let materials: [String]
    let gauge: String?
    let steps: [PatternStep]
}

/// Matches shared/content.json. Android reads the same file.
struct StitchContent: Codable {
    let tutorials: [Tutorial]
    let stitches: [Stitch]
    let patterns: [Pattern]

    func pattern(id: String) -> Pattern? { patterns.first { $0.id == id } }
    func tutorial(id: String) -> Tutorial? { tutorials.first { $0.id == id } }
}

/// One project started from a pattern. Saved on the device.
struct Project: Codable, Identifiable, Equatable {
    let id: String
    let patternId: String
    let startedAt: Date
    var done: [Bool]
    var notes: String
    var counters: [RowCounter]
}
