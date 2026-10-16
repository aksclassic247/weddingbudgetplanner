import SwiftUI

struct ToolsView: View {
    @State private var type: ShapingType = .increase
    @State private var stitches = ""
    @State private var changes = ""

    @State private var projectGrams = ""
    @State private var ballGrams = ""
    @State private var ballMetres = ""

    private var shapingOutcome: Result<String, Error>? {
        guard let stitchCount = Int(stitches), let changeCount = Int(changes) else { return nil }
        do {
            let plan = try ShapingCalculator.plan(stitches: stitchCount, changes: changeCount, type: type)
            return .success("\(plan.describe())\n\nYou will have \(plan.stitchesAfter) stitches after this row.")
        } catch {
            return .failure(error)
        }
    }

    private var yarnOutcome: Result<String, Error>? {
        guard let project = Double(projectGrams), let ball = Double(ballGrams), let metres = Double(ballMetres) else { return nil }
        do {
            let estimate = try YarnCalculator.estimate(projectGrams: project, ballGrams: ball, ballMetres: metres)
            return .success("You need about \(Int(estimate.metres)) m of yarn, which is \(estimate.balls) ball(s). This includes a 10 percent margin.")
        } catch {
            return .failure(error)
        }
    }

    var body: some View {
        Form {
            Section {
                Picker("Type", selection: $type) {
                    ForEach(ShapingType.allCases) { option in
                        Text(option.label).tag(option)
                    }
                }
                .pickerStyle(.segmented)
                TextField("Stitches in the row now", text: $stitches)
                    .keyboardType(.numberPad)
                TextField("How many \(type.label.lowercased())s", text: $changes)
                    .keyboardType(.numberPad)
            } header: {
                Text("Shaping calculator")
            } footer: {
                Text("Spreads increases or decreases evenly across a row.")
            }

            Section("Work it like this") {
                switch shapingOutcome {
                case .none:
                    Text("Enter the stitch count and how many to make.").foregroundStyle(Palette.inkSoft)
                case .some(.success(let text)):
                    Text(text)
                case .some(.failure(let error)):
                    Text(error.localizedDescription).foregroundStyle(Palette.coralDark)
                }
            }

            Section {
                TextField("Grams the project needs", text: $projectGrams)
                    .keyboardType(.decimalPad)
                TextField("Grams per ball, from the label", text: $ballGrams)
                    .keyboardType(.decimalPad)
                TextField("Metres per ball, from the label", text: $ballMetres)
                    .keyboardType(.decimalPad)
            } header: {
                Text("Yarn estimate")
            } footer: {
                Text("Read the ball weight and length from the yarn label.")
            }

            Section("How much to buy") {
                switch yarnOutcome {
                case .none:
                    Text("Fill in all three numbers.").foregroundStyle(Palette.inkSoft)
                case .some(.success(let text)):
                    Text(text)
                case .some(.failure(let error)):
                    Text(error.localizedDescription).foregroundStyle(Palette.coralDark)
                }
            }
        }
        .navigationTitle("Tools")
    }
}
