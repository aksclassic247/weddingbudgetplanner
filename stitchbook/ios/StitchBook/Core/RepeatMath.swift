import Foundation

/// A repeat counter follows a row counter. When a pattern repeats every N rows, it counts full repeats.
enum RepeatMath {
    /// Full repeats finished after `rows` rows.
    static func completedRepeats(rows: Int, rowsPerRepeat: Int) -> Int {
        precondition(rowsPerRepeat > 0 && rows >= 0, "Rows must be zero or more and a repeat needs at least one row.")
        return rows / rowsPerRepeat
    }

    /// Rows done inside the repeat that is in progress.
    static func rowsIntoCurrentRepeat(rows: Int, rowsPerRepeat: Int) -> Int {
        precondition(rowsPerRepeat > 0 && rows >= 0, "Rows must be zero or more and a repeat needs at least one row.")
        return rows % rowsPerRepeat
    }
}
