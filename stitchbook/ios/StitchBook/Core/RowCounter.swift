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

    /// The count before the last change, so `undo()` can reverse one step.
    private(set) var previousCount: Int? = nil

    mutating func increment() {
        previousCount = count
        count += 1
    }

    /// Never goes below zero. A tap at zero is not recorded.
    mutating func decrement() {
        guard count > 0 else { return }
        previousCount = count
        count -= 1
    }

    mutating func reset() {
        guard count > 0 else { return }
        previousCount = count
        count = 0
    }

    /// Reverses the last increment, decrement or reset. Only one step is remembered.
    mutating func undo() {
        guard let previous = previousCount else { return }
        count = previous
        previousCount = nil
    }

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
