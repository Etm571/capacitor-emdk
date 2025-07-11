import Foundation

@objc public class EMDK: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
