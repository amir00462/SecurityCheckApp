package ir.dunijet.securitycheckapp.util

// New ->
enum class ZoneType {
    CheshmiFourTypes,
    CheshmiTwoTypes,
    GheirFaal,
    NimeFaal,
    Faal,
    DingDong,
    Hour24,
}

enum class ZoneNooe {
    Cheshmi,
    AtashDood
}

enum class ThemeData {
    LightTheme ,
    DarkTheme
}

enum class SensorType {
    Cheshmi,
    DoodAtash,
}

// Home Vaziat
enum class HomeVaziat {
    GheirFaal,
    NimeFaal,
    Faal
}

// Zones =>
enum class EyeTypes {
    GheirFaal,
    NimeFaal,
    Faal,
    DingDong
}

enum class CheshmiTwoTypesStep {
    FaalGheirFaal,
    BaleKheir,
    KhamooshRoshanLahzeii,
}

enum class SmsState {
    Init,
    Sent,
    NoService,
    AirplaneMode,
    Failed,
}

// Member
enum class MemberTask {
    AddUser,
    EditUser,
    DeleteUser
}

// Output =
enum class OutputType {
    KhamooshRoshan,
    Lahzeii,
    VabasteDoodAtash
}