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
    let credit: String?
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

/// A counter that follows another counter, for example "repeats" of an 8-row pattern.
struct RepeatCounter: Codable, Equatable, Identifiable {
    let id: UUID
    let name: String
    let sourceCounterId: UUID
    let rowsPerRepeat: Int

    init(name: String, sourceCounterId: UUID, rowsPerRepeat: Int) {
        self.id = UUID()
        self.name = name
        self.sourceCounterId = sourceCounterId
        self.rowsPerRepeat = rowsPerRepeat
    }
}

/// One project started from a pattern. Saved on the device.
/// Newer fields are optional so projects saved by older versions still load.
struct Project: Codable, Identifiable, Equatable {
    let id: String
    let patternId: String
    let startedAt: Date
    var done: [Bool]
    var notes: String
    var counters: [RowCounter]
    var repeats: [RepeatCounter]? = nil
    var reminderHour: Int? = nil
    var reminderMinute: Int? = nil
    var pdfs: [String]? = nil
    var photos: [String]? = nil

    var repeatCounters: [RepeatCounter] { repeats ?? [] }
    var pdfFiles: [String] { pdfs ?? [] }
    var photoFiles: [String] { photos ?? [] }
}
