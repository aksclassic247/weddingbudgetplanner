import Foundation

/// Stores pattern PDFs and progress photos inside the app's Documents folder.
/// Files are removed when their project is deleted.
enum ProjectFiles {
    static func folder(projectId: String, kind: String) throws -> URL {
        let base = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
        let dir = base.appendingPathComponent("StitchBookFiles/\(projectId)/\(kind)", isDirectory: true)
        try FileManager.default.createDirectory(at: dir, withIntermediateDirectories: true)
        return dir
    }

    /// Writes the data and returns the new file name.
    static func write(_ data: Data, projectId: String, kind: String, extension ext: String) throws -> String {
        let name = "\(UUID().uuidString).\(ext)"
        try data.write(to: try folder(projectId: projectId, kind: kind).appendingPathComponent(name))
        return name
    }

    static func url(projectId: String, kind: String, name: String) -> URL? {
        try? folder(projectId: projectId, kind: kind).appendingPathComponent(name)
    }

    static func delete(projectId: String, kind: String, name: String) {
        if let url = url(projectId: projectId, kind: kind, name: name) {
            try? FileManager.default.removeItem(at: url)
        }
    }

    static func deleteProject(projectId: String) {
        guard let base = try? FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: false) else { return }
        try? FileManager.default.removeItem(at: base.appendingPathComponent("StitchBookFiles/\(projectId)", isDirectory: true))
    }
}
