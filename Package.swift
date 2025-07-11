// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "CapacitorEmdk",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "CapacitorEmdk",
            targets: ["EMDKPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "EMDKPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/EMDKPlugin"),
        .testTarget(
            name: "EMDKPluginTests",
            dependencies: ["EMDKPlugin"],
            path: "ios/Tests/EMDKPluginTests")
    ]
)