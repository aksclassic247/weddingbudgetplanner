import Foundation

/// Contact details. Change `email` before you publish.
enum Support {
    static let email = "aksclassic247@gmail.com"

    /// The About text shown on Home. It says what the app does not do, so nobody is misled.
    static let aboutText = "StitchBook does not turn photos into patterns, and it has no AI chat. "
        + "Sample patterns were drafted with AI help and are not yet test-made, so check them before you start. "
        + "Everything is free, with no ads and no subscription."

    static func mailURL(subject: String) -> URL? {
        var components = URLComponents()
        components.scheme = "mailto"
        components.path = email
        components.queryItems = [URLQueryItem(name: "subject", value: subject)]
        return components.url
    }
}
