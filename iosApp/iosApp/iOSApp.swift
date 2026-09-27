import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        AppModuleKt.initKoin(configuracionAdicional: { _ in })
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}