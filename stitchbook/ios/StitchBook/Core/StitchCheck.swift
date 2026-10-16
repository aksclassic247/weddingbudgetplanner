import Foundation

enum StitchCheckResult: Equatable {
    case match
    /// `difference` is counted minus expected. Negative means too few stitches.
    case off(Int)
}

enum StitchCheck {
    static func compare(expected: Int, counted: Int) -> StitchCheckResult {
        expected == counted ? .match : .off(counted - expected)
    }

    static func message(_ result: StitchCheckResult, expected: Int) -> String {
        switch result {
        case .match:
            return "Your count matches the pattern (\(expected) stitches)."
        case .off(let difference) where difference > 0:
            return "You have \(difference) extra. The pattern says \(expected). Check for an extra increase or a missed decrease."
        case .off(let difference):
            return "You are \(-difference) short. The pattern says \(expected). Check for a skipped stitch or a missed increase."
        }
    }
}
