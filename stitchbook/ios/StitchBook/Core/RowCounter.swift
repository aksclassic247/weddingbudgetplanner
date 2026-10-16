import Foundation

/// One row or round counter. Never drops below zero. `target` is optional.
struct RowCounter: Codable, Equatable, Identifiable {
    let id: UUID
    let name: String
    private(set) var count: Int
    let target: Int?

    init(name: String, count: Int = 0, target: Int? = nil) {
        self.id = UUID()
        self.name = name
        self.count = count
        self.target = target
    }

    mutating func increment() { count += 1 }
    mutating func decrement() { count = max(0, count - 1) }
    mutating func reset() { count = 0 }

    /// A value from 0 to 1, or nil when there is no target.
    var progress: Double? {
        guard let target else { return nil }
        return min(1, Double(count) / Double(target))
    }

    var isComplete: Bool {
        guard let target else { return false }
        return count >= target
    }
}
