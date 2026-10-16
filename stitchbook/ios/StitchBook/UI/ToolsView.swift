import SwiftUI

struct ToolsView: View {
    @State private var type: ShapingType = .increase
    @State private var stitches = ""
    @State private var changes = ""

    private var outcome: Result<String, Error>? {
        guard let stitchCount = Int(stitches), let changeCount = Int(changes) else { return nil }
        do {
            let plan = try ShapingCalculator.plan(stitches: stitchCount, changes: changeCount, type: type)
            return .success("\(plan.describe())\n\nYou will have \(plan.stitchesAfter) stitches after this row.")
        } catch {
            return .failure(error)
        }
    }

    var body: some View {
        Form {
            Section {
                Text("Spread increases or decreases evenly across a row. Plain arithmetic, so the answer is the same every time.")
                    .font(.subheadline)
                    .foregroundStyle(Palette.inkSoft)
            }

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
            }

            Section("Work it like this") {
                switch outcome {
                case .none:
                    Text("Enter the stitch count and how many to make.").foregroundStyle(Palette.inkSoft)
                case .some(.success(let text)):
                    Text(text).font(.body)
                case .some(.failure(let error)):
                    Text(error.localizedDescription).foregroundStyle(Palette.coralDark)
                }
            }
        }
        .navigationTitle("Tools")
    }
}
