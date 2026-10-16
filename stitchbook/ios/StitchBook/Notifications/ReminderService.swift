import Foundation
import UserNotifications

/// Daily reminders for each project, using local notifications.
enum ReminderService {
    static func requestPermission() async -> Bool {
        (try? await UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .sound])) ?? false
    }

    static func schedule(projectId: String, title: String, hour: Int, minute: Int) {
        let content = UNMutableNotificationContent()
        content.title = "Time to knit"
        content.body = title.isEmpty ? "Pick up your project." : "Pick up \(title)."
        content.sound = .default

        var components = DateComponents()
        components.hour = hour
        components.minute = minute
        let trigger = UNCalendarNotificationTrigger(dateMatching: components, repeats: true)

        let request = UNNotificationRequest(identifier: identifier(for: projectId), content: content, trigger: trigger)
        UNUserNotificationCenter.current().add(request) { _ in }
    }

    static func cancel(projectId: String) {
        UNUserNotificationCenter.current().removePendingNotificationRequests(withIdentifiers: [identifier(for: projectId)])
    }

    private static func identifier(for projectId: String) -> String {
        "reminder-\(projectId)"
    }
}
