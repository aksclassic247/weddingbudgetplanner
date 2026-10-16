import Foundation

/// Spreads increases or decreases evenly across a row. Plain arithmetic, so the answer is always the same.
enum ShapingType: String, CaseIterable, Identifiable {
    case increase
    case decrease

    var id: String { rawValue }

    var label: String { self == .increase ? "Increase" : "Decrease" }
    var operation: String { self == .increase ? "inc" : "k2tog" }
    var stitchesUsed: Int { self == .increase ? 1 : 2 }
    var stitchChange: Int { self == .increase ? 1 : -1 }
}

/// Knit `plainStitches`, then do the shaping stitch, repeated `repeats` times.
struct ShapingRun: Equatable {
    let plainStitches: Int
    let repeats: Int
}

struct ShapingPlan: Equatable {
    let type: ShapingType
    let stitchesBefore: Int
    let stitchesAfter: Int
    let runs: [ShapingRun]

    /// A written version of the plan, such as "k9, inc (repeat 6 times)".
    func describe() -> String {
        runs.map { run -> String in
            let op = run.plainStitches == 0 ? type.operation : "k\(run.plainStitches), \(type.operation)"
            return run.repeats == 1 ? op : "\(op) (repeat \(run.repeats) times)"
        }
        .joined(separator: ", then ")
    }
}

enum ShapingError: LocalizedError {
    case invalid(String)

    var errorDescription: String? {
        switch self {
        case .invalid(let message): return message
        }
    }
}

enum ShapingCalculator {
    static func plan(stitches: Int, changes: Int, type: ShapingType) throws -> ShapingPlan {
        guard stitches > 0 else { throw ShapingError.invalid("Stitch count must be more than zero.") }
        guard changes > 0 else {
            throw ShapingError.invalid("Number of \(type.label.lowercased())s must be more than zero.")
        }

        let plain = stitches - type.stitchesUsed * changes
        guard plain >= 0 else {
            throw ShapingError.invalid("A row with \(stitches) stitches cannot fit \(changes) \(type.label.lowercased())s.")
        }

        let base = plain / changes
        let extra = plain % changes

        // The first `extra` repeats get one more plain stitch, so the spacing stays even.
        var runs: [ShapingRun] = []
        for index in 0..<changes {
            let count = index < extra ? base + 1 : base
            if let last = runs.last, last.plainStitches == count {
                runs[runs.count - 1] = ShapingRun(plainStitches: count, repeats: last.repeats + 1)
            } else {
                runs.append(ShapingRun(plainStitches: count, repeats: 1))
            }
        }

        return ShapingPlan(
            type: type,
            stitchesBefore: stitches,
            stitchesAfter: stitches + type.stitchChange * changes,
            runs: runs
        )
    }
}
