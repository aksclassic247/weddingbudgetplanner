import SwiftUI

struct ProjectView: View {
    @EnvironmentObject private var library: LibraryStore
    let projectId: String

    @State private var checkingExpected: Int?
    @State private var hapticTick = 0
    @State private var showAddCounter = false
    @State private var confirmRemove = false
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        if let project = library.project(id: projectId), let pattern = library.content.pattern(id: project.patternId) {
            content(project: project, pattern: pattern)
                .navigationTitle(pattern.title)
                .navigationBarTitleDisplayMode(.inline)
                .sheet(isPresented: Binding(get: { checkingExpected != nil }, set: { if !$0 { checkingExpected = nil } })) {
                    StitchCheckSheet(expected: checkingExpected ?? 0)
                }
                .sheet(isPresented: $showAddCounter) {
                    AddCounterSheet { counter in
                        library.update(projectId) { $0.counters.append(counter) }
                    }
                }
                .confirmationDialog("Remove this project?", isPresented: $confirmRemove, titleVisibility: .visible) {
                    Button("Remove", role: .destructive) {
                        library.remove(projectId)
                        dismiss()
                    }
                    Button("Cancel", role: .cancel) {}
                } message: {
                    Text("Your progress on this project will be deleted.")
                }
        } else {
            Text("This project was removed.")
                .foregroundStyle(Palette.inkSoft)
        }
    }

    @ViewBuilder
    private func content(project: Project, pattern: Pattern) -> some View {
        let done = project.done.filter { $0 }.count

        ScrollView {
            VStack(alignment: .leading, spacing: 14) {
                ZStack {
                    RoundedRectangle(cornerRadius: 24).fill(Color(hex: pattern.color)).frame(height: 120)
                    Text(pattern.emoji).font(.system(size: 52))
                }

                VStack(alignment: .leading, spacing: 6) {
                    StepProgress(done: done, total: project.done.count)
                    if !project.done.isEmpty && done == project.done.count {
                        Text("All steps done. Nice work!").font(.subheadline.weight(.semibold)).foregroundStyle(Palette.success)
                    }
                }
                .card()

                Text("Checklist").font(.title3.weight(.bold)).foregroundStyle(Palette.ink)
                ForEach(Array(pattern.steps.enumerated()), id: \.offset) { index, step in
                    stepRow(index: index, step: step, checked: project.done.indices.contains(index) && project.done[index])
                }

                Text("Row counters").font(.title3.weight(.bold)).foregroundStyle(Palette.ink)
                if project.counters.isEmpty {
                    Text("Add a counter to track rows, repeats or pattern sections.")
                        .font(.footnote).foregroundStyle(Palette.inkSoft)
                }
                ForEach(project.counters) { counter in
                    counterRow(counter)
                }
                Button {
                    showAddCounter = true
                } label: {
                    Label("Add counter", systemImage: "plus")
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 12)
                        .overlay(RoundedRectangle(cornerRadius: 14).stroke(Palette.coral, lineWidth: 1.5))
                }

                ProjectExtrasSection(projectId: projectId, patternTitle: pattern.title)

                Text("Notes").font(.title3.weight(.bold)).foregroundStyle(Palette.ink)
                TextEditor(text: Binding(
                    get: { project.notes },
                    set: { text in library.update(projectId) { $0.notes = text } }
                ))
                .frame(minHeight: 110)
                .padding(8)
                .background(.white, in: RoundedRectangle(cornerRadius: 12))
                .overlay(RoundedRectangle(cornerRadius: 12).stroke(Palette.line, lineWidth: 1))

                Button(role: .destructive) {
                    confirmRemove = true
                } label: {
                    Text("Remove project")
                        .frame(maxWidth: .infinity)
                        .padding(.vertical, 10)
                }
            }
            .padding(16)
        }
        .background(Palette.cream)
        .sensoryFeedback(.selection, trigger: hapticTick)
    }

    private func stepRow(index: Int, step: PatternStep, checked: Bool) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack(alignment: .top, spacing: 12) {
                Button {
                    library.toggleStep(projectId, index: index)
                } label: {
                    Image(systemName: checked ? "checkmark.circle.fill" : "circle")
                        .font(.title2)
                        .foregroundStyle(Palette.coral)
                }
                .buttonStyle(.plain)

                VStack(alignment: .leading, spacing: 4) {
                    Text(step.label).font(.subheadline.weight(.bold)).foregroundStyle(Palette.coralDark)
                    Text(step.text)
                        .font(.body)
                        .foregroundStyle(checked ? Palette.inkSoft : Palette.ink)
                        .strikethrough(checked)
                }
            }
            if let expected = step.stitches {
                Button("Check my count (pattern says \(expected))") {
                    checkingExpected = expected
                }
                .font(.footnote.weight(.semibold))
                .buttonStyle(.bordered)
                .tint(Palette.coral)
            }
        }
        .card(fill: checked ? Palette.peach : .white)
    }

    private func counterRow(_ counter: RowCounter) -> some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                VStack(alignment: .leading, spacing: 2) {
                    Text(counter.name).font(.headline).foregroundStyle(Palette.ink)
                    Text(counter.target.map { "\(counter.count) of \($0)" } ?? "\(counter.count)")
                        .font(.title2.weight(.heavy))
                        .foregroundStyle(Palette.coral)
                }
                Spacer()
                Button { change(counter.id) { $0.decrement() } } label: { Image(systemName: "minus.circle.fill").font(.title2) }
                Button { change(counter.id) { $0.increment() } } label: { Image(systemName: "plus.circle.fill").font(.title2) }
                Button { change(counter.id) { $0.reset() } } label: { Image(systemName: "arrow.counterclockwise.circle").font(.title2) }
                if counter.previousCount != nil {
                    Button { change(counter.id) { $0.undo() } } label: { Text("Undo").font(.subheadline.weight(.semibold)) }
                }
            }
            .buttonStyle(.plain)
            .foregroundStyle(Palette.coral)

            if let progress = counter.progress {
                ProgressView(value: progress).tint(Palette.coral)
                if counter.isComplete {
                    Text("Target reached").font(.caption.weight(.semibold)).foregroundStyle(Palette.success)
                }
            }
            Button("Remove counter", role: .destructive) {
                library.update(projectId) { project in project.counters.removeAll { $0.id == counter.id } }
            }
            .font(.caption)
        }
        .card()
    }

    private func change(_ counterId: UUID, _ transform: @escaping (inout RowCounter) -> Void) {
        library.update(projectId) { project in
            guard let index = project.counters.firstIndex(where: { $0.id == counterId }) else { return }
            transform(&project.counters[index])
        }
        hapticTick += 1
    }
}

struct StitchCheckSheet: View {
    let expected: Int
    @Environment(\.dismiss) private var dismiss
    @State private var entry = ""

    private var counted: Int? { Int(entry) }

    var body: some View {
        NavigationStack {
            Form {
                Section {
                    Text("Count the stitches in this round or row, then enter the number.")
                    TextField("Your count", text: $entry)
                        .keyboardType(.numberPad)
                }
                if let counted {
                    Section {
                        let result = StitchCheck.compare(expected: expected, counted: counted)
                        Text(StitchCheck.message(result, expected: expected))
                            .foregroundStyle(result == .match ? Palette.success : Palette.coralDark)
                    }
                }
            }
            .navigationTitle("Check your count")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .confirmationAction) {
                    Button("Done") { dismiss() }
                }
            }
        }
        .presentationDetents([.medium])
    }
}

struct AddCounterSheet: View {
    var onAdd: (RowCounter) -> Void
    @Environment(\.dismiss) private var dismiss
    @State private var name = ""
    @State private var target = ""

    private var parsedTarget: Int? { Int(target) }
    private var isValid: Bool {
        !name.trimmingCharacters(in: .whitespaces).isEmpty && (target.isEmpty || (parsedTarget ?? 0) > 0)
    }

    var body: some View {
        NavigationStack {
            Form {
                TextField("Name, for example Rows", text: $name)
                TextField("Target (optional)", text: $target)
                    .keyboardType(.numberPad)
                    .onChange(of: target) { _, newValue in
                        target = newValue.filter(\.isNumber)
                    }
            }
            .navigationTitle("New counter")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { dismiss() }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Add") {
                        onAdd(RowCounter(name: name.trimmingCharacters(in: .whitespaces), target: parsedTarget))
                        dismiss()
                    }
                    .disabled(!isValid)
                }
            }
        }
        .presentationDetents([.medium])
    }
}
