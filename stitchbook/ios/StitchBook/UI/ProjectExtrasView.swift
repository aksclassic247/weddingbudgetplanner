import PhotosUI
import SwiftUI
import UniformTypeIdentifiers

/// The extra sections on a project: reminder, repeat counters, photos and pattern PDFs.
struct ProjectExtrasSection: View {
    @EnvironmentObject private var library: LibraryStore
    let projectId: String
    let patternTitle: String

    @State private var reminderMessage: String?
    @State private var showAddRepeat = false
    @State private var photoItem: PhotosPickerItem?
    @State private var showPDFImporter = false
    @State private var viewingPDF: PDFItem?
    @State private var errorMessage: String?

    var body: some View {
        if let project = library.project(id: projectId) {
            VStack(alignment: .leading, spacing: 14) {
                reminderCard(project)
                repeatCard(project)
                photoCard(project)
                pdfCard(project)
            }
            .alert("Something went wrong", isPresented: Binding(get: { errorMessage != nil }, set: { if !$0 { errorMessage = nil } })) {
                Button("OK", role: .cancel) {}
            } message: {
                Text(errorMessage ?? "")
            }
        }
    }

    // MARK: Reminder

    private func reminderCard(_ project: Project) -> some View {
        let enabled = project.reminderHour != nil && project.reminderMinute != nil
        return VStack(alignment: .leading, spacing: 10) {
            Text("Daily reminder").font(.headline).foregroundStyle(Palette.ink)
            Toggle(isOn: Binding(get: { enabled }, set: { setReminderOn($0) })) {
                Text(enabled ? "Every day at \(timeText(project))" : "Off").foregroundStyle(Palette.ink)
            }
            .tint(Palette.coral)
            if enabled {
                DatePicker("Time", selection: timeBinding(project), displayedComponents: .hourAndMinute)
                    .foregroundStyle(Palette.ink)
            }
            if let reminderMessage {
                Text(reminderMessage).font(.footnote).foregroundStyle(Palette.coralDark)
            }
        }
        .card()
    }

    private func setReminderOn(_ on: Bool) {
        if on {
            Task {
                let granted = await ReminderService.requestPermission()
                if granted {
                    library.setReminder(projectId, hour: 19, minute: 0, patternTitle: patternTitle)
                    reminderMessage = nil
                } else {
                    reminderMessage = "Notifications are off for StitchBook. Turn them on in Settings to get reminders."
                }
            }
        } else {
            library.setReminder(projectId, hour: nil, minute: nil, patternTitle: patternTitle)
        }
    }

    private func timeBinding(_ project: Project) -> Binding<Date> {
        Binding(
            get: {
                var parts = DateComponents()
                parts.hour = project.reminderHour
                parts.minute = project.reminderMinute
                return Calendar.current.date(from: parts) ?? Date()
            },
            set: { date in
                let parts = Calendar.current.dateComponents([.hour, .minute], from: date)
                library.setReminder(projectId, hour: parts.hour, minute: parts.minute, patternTitle: patternTitle)
            }
        )
    }

    private func timeText(_ project: Project) -> String {
        guard let hour = project.reminderHour, let minute = project.reminderMinute else { return "" }
        var parts = DateComponents()
        parts.hour = hour
        parts.minute = minute
        let date = Calendar.current.date(from: parts) ?? Date()
        return date.formatted(date: .omitted, time: .shortened)
    }

    // MARK: Repeat counters

    private func repeatCard(_ project: Project) -> some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Repeat counters").font(.headline).foregroundStyle(Palette.ink)
            Text("Follow a row counter to count pattern repeats, for example every 8 rows.")
                .font(.footnote).foregroundStyle(Palette.inkSoft)

            ForEach(project.repeatCounters) { repeatCounter in
                let source = project.counters.first { $0.id == repeatCounter.sourceCounterId }
                let rows = source?.count ?? 0
                VStack(alignment: .leading, spacing: 4) {
                    Text("\(repeatCounter.name): \(RepeatMath.completedRepeats(rows: rows, rowsPerRepeat: repeatCounter.rowsPerRepeat)) done")
                        .font(.subheadline.weight(.bold)).foregroundStyle(Palette.ink)
                    Text("\(source?.name ?? "Source counter removed"): \(RepeatMath.rowsIntoCurrentRepeat(rows: rows, rowsPerRepeat: repeatCounter.rowsPerRepeat)) of \(repeatCounter.rowsPerRepeat) rows into this repeat")
                        .font(.caption).foregroundStyle(Palette.inkSoft)
                    ProgressView(value: Double(RepeatMath.rowsIntoCurrentRepeat(rows: rows, rowsPerRepeat: repeatCounter.rowsPerRepeat)),
                                 total: Double(repeatCounter.rowsPerRepeat))
                        .tint(Palette.coral)
                    Button("Remove", role: .destructive) {
                        library.removeRepeat(projectId, repeatId: repeatCounter.id)
                    }
                    .font(.caption)
                }
            }

            if project.counters.isEmpty {
                Text("Add a row counter above first.").font(.footnote).foregroundStyle(Palette.inkSoft)
            } else {
                Button {
                    showAddRepeat = true
                } label: {
                    Label("Add repeat counter", systemImage: "plus")
                }
                .sheet(isPresented: $showAddRepeat) {
                    AddRepeatSheet(counters: project.counters) { name, sourceId, rows in
                        library.addRepeat(projectId, name: name, sourceCounterId: sourceId, rowsPerRepeat: rows)
                    }
                }
            }
        }
        .card()
    }

    // MARK: Photos

    private func photoCard(_ project: Project) -> some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Progress photos").font(.headline).foregroundStyle(Palette.ink)
            Text("Save photos of your work as you go. They stay on this device.")
                .font(.footnote).foregroundStyle(Palette.inkSoft)

            if !project.photoFiles.isEmpty {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: 10) {
                        ForEach(project.photoFiles, id: \.self) { name in
                            photoThumbnail(name: name)
                                .contextMenu {
                                    Button("Remove", role: .destructive) {
                                        library.removeAttachment(projectId, kind: .photos, name: name)
                                    }
                                }
                        }
                    }
                }
            }

            PhotosPicker(selection: $photoItem, matching: .images) {
                Label("Add photo", systemImage: "photo.on.rectangle")
            }
            .onChange(of: photoItem) { _, item in
                guard let item else { return }
                Task {
                    defer { photoItem = nil }
                    do {
                        if let data = try await item.loadTransferable(type: Data.self) {
                            try library.addAttachment(projectId, kind: .photos, data: data, extension: "jpg")
                        }
                    } catch {
                        errorMessage = "That photo could not be saved."
                    }
                }
            }
        }
        .card()
    }

    @ViewBuilder
    private func photoThumbnail(name: String) -> some View {
        if let url = ProjectFiles.url(projectId: projectId, kind: "photos", name: name),
           let image = UIImage(contentsOfFile: url.path) {
            Image(uiImage: image)
                .resizable()
                .scaledToFill()
                .frame(width: 96, height: 96)
                .clipShape(RoundedRectangle(cornerRadius: 12))
        } else {
            RoundedRectangle(cornerRadius: 12).fill(Palette.peach).frame(width: 96, height: 96)
        }
    }

    // MARK: PDFs

    private func pdfCard(_ project: Project) -> some View {
        VStack(alignment: .leading, spacing: 10) {
            Text("Your pattern PDFs").font(.headline).foregroundStyle(Palette.ink)
            Text("Add a PDF pattern from your files. It opens inside the app.")
                .font(.footnote).foregroundStyle(Palette.inkSoft)

            ForEach(Array(project.pdfFiles.enumerated()), id: \.offset) { index, name in
                HStack {
                    Text("PDF \(index + 1)").foregroundStyle(Palette.ink)
                    Spacer()
                    Button("View") {
                        if let url = ProjectFiles.url(projectId: projectId, kind: "pdfs", name: name) {
                            viewingPDF = PDFItem(url: url)
                        }
                    }
                    Button("Remove", role: .destructive) {
                        library.removeAttachment(projectId, kind: .pdfs, name: name)
                    }
                }
                .buttonStyle(.borderless)
            }

            Button {
                showPDFImporter = true
            } label: {
                Label("Add PDF", systemImage: "doc.badge.plus")
            }
        }
        .card()
        .fileImporter(isPresented: $showPDFImporter, allowedContentTypes: [.pdf]) { result in
            importPDF(result)
        }
        .sheet(item: $viewingPDF) { item in
            NavigationStack {
                PDFPreview(url: item.url)
                    .navigationTitle("Pattern")
                    .navigationBarTitleDisplayMode(.inline)
                    .toolbar {
                        ToolbarItem(placement: .confirmationAction) {
                            Button("Done") { viewingPDF = nil }
                        }
                    }
            }
        }
    }

    private func importPDF(_ result: Result<URL, Error>) {
        switch result {
        case .failure:
            errorMessage = "That PDF could not be opened."
        case .success(let url):
            let accessed = url.startAccessingSecurityScopedResource()
            defer { if accessed { url.stopAccessingSecurityScopedResource() } }
            do {
                let data = try Data(contentsOf: url)
                try library.addAttachment(projectId, kind: .pdfs, data: data, extension: "pdf")
            } catch {
                errorMessage = "That PDF could not be saved."
            }
        }
    }
}

struct PDFItem: Identifiable {
    let id = UUID()
    let url: URL
}

/// Asks for a name, a row counter to follow, and how many rows make one repeat.
struct AddRepeatSheet: View {
    let counters: [RowCounter]
    var onAdd: (String, UUID, Int) -> Void
    @Environment(\.dismiss) private var dismiss

    @State private var name = "Repeats"
    @State private var rows = ""
    @State private var sourceId: UUID?

    private var parsedRows: Int? { Int(rows) }
    private var isValid: Bool {
        !name.trimmingCharacters(in: .whitespaces).isEmpty && (parsedRows ?? 0) > 0 && (sourceId != nil)
    }

    var body: some View {
        NavigationStack {
            Form {
                TextField("Name", text: $name)
                Picker("Follow which counter", selection: $sourceId) {
                    Text("Choose").tag(UUID?.none)
                    ForEach(counters) { counter in
                        Text(counter.name).tag(UUID?.some(counter.id))
                    }
                }
                TextField("Rows in one repeat", text: $rows)
                    .keyboardType(.numberPad)
            }
            .navigationTitle("New repeat counter")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button("Cancel") { dismiss() }
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button("Add") {
                        if let sourceId, let parsedRows {
                            onAdd(name.trimmingCharacters(in: .whitespaces), sourceId, parsedRows)
                        }
                        dismiss()
                    }
                    .disabled(!isValid)
                }
            }
        }
        .presentationDetents([.medium])
    }
}
