import Foundation

struct YarnEstimate: Equatable {
    let metres: Double
    let balls: Int
}

enum CalculatorError: LocalizedError {
    case invalid(String)

    var errorDescription: String? {
        switch self {
        case .invalid(let message): return message
        }
    }
}

/// Estimates yarn from the weight and length on the label, with a safety margin.
enum YarnCalculator {
    static func estimate(projectGrams: Double, ballGrams: Double, ballMetres: Double, marginPercent: Double = 10) throws -> YarnEstimate {
        guard projectGrams > 0 else { throw CalculatorError.invalid("Project weight must be more than zero.") }
        guard ballGrams > 0 else { throw CalculatorError.invalid("Ball weight must be more than zero.") }
        guard ballMetres > 0 else { throw CalculatorError.invalid("Ball length must be more than zero.") }
        guard marginPercent >= 0 else { throw CalculatorError.invalid("Margin cannot be negative.") }

        let gramsPerMetre = ballGrams / ballMetres
        let metres = projectGrams / gramsPerMetre * (1 + marginPercent / 100)
        return YarnEstimate(metres: metres, balls: Int((metres / ballMetres).rounded(.up)))
    }
}
