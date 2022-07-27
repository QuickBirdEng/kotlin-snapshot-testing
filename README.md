# Kotlin Snapshot Testing Library

<img src="https://quickbirdstudios.com/blog/wp-content/uploads/2022/07/Snapshot-2-1024x518.jpeg" alt="Cover-Image" width="700"/>
[![](https://jitpack.io/v/QuickBirdEng/kotlin-snapshot-testing.svg)](https://jitpack.io/#QuickBirdEng/kotlin-snapshot-testing)

This is an extensible Kotlin Multiplatform library to easily create Snapshot tests for Android and other Kotlin applications. You can create snapshots of every serializable property, like screenshots or view hierachies, of your application! 

# How it works!
Probably every mobile developer knows these small but annoying bugs: 
The general toolbar of the app is restyled, and it looks good on 29 out of the total 30 screens but
on one of the screens some UI elements moved unnoticed onto the wrong positions. Something like this
is easily overseen in manual testing since the QA would need to test the change on every device.

Wouldn’t it be cool to have a convenient and easy way to find such regressions quickly and keep 
track of UI changes? Snapshot testing solves this by automatically comparing the previous and the 
new build.

With our library you simply define a test case for a property of your application that can be
serialized, for example, a screenshot of your main screen. By recording this property (aka taking a 
screenshot) on the first run of your test case, a reference is saved. On every following run of your 
test suite, the property is checked against the reference. If something changes and for instance, 
the screen looks different, the test case fails and shows you the difference to the original
screenshot.

Not only screenshots can be snapshotted, but everything that can be serialized (and even more)!

For a detailed explanation see our related article [Kotlin Snapshot Testing](https://quickbirdstudios.com/blog/snapshot-testing-kotlin).

This is an early version and work in progress. 
Do not hesitate to give feedback, ideas or improvements via an issue.

# Examples

<img src="https://quickbirdstudios.com/blog/wp-content/uploads/2022/07/screenshot_main_reference-1.png" alt="Example" width="600"/>

An example test case for snapshot testing a screen of our example App:

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class ComposeScreenshotTest : AndroidFileSnapshotTest() {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    
    @Test
    fun settingsScreenScreenshot() = runTest {
        composeTestRule.onNodeWithTag("Settings").performClick()

        FileSnapshotting
            .composeScreenshot
            .snapshotToFilesDir(composeTestRule)
    }
}

```

Use `AndroidFileSnapshotTest` for Android tests and `JUnitFileSnapshotTest` for jvm tests.

To record snapshots either set

```kotlin
AndroidFileSnapshotTest(record = true)
```

or

```kotlin
.snapshotToFilesDir(composeTestRule, record = true)
```

When recording is turned on, a new reference will be saved and the test case fails. After recording
the snapshots and setting the parameters back to false, all further runs will test against this 
recording and show a diff when something changed.

<img src="https://quickbirdstudios.com/blog/wp-content/uploads/2022/07/screenshot_settings_diff.png" alt="Diff" width="300"/>


If you want to be able to snapshot test other properties of your projects, you can create new 
personalized `Snapshotting` instances. 

```kotlin
data class QuickBird(
    val shape: String = "Potato",
    val numberOfEatenWorms: Int = 42
)
```

If you want to test a `QuickBird` class for example, it 
can look like this:

```kotlin
val Diffing.Companion.lines
    get() = Diffing { first: String, second: String ->
        if (first == second) null
        else first
            .split("\n")
            .zip(second.split("\n"))
            .joinToString(separator = "\n") { (first, second) ->
                if (first == second) first
                else "-$first\n+$second"
            }
    }

val Snapshotting.Companion.quickBird
    get() = Snapshotting(
        diffing = Diffing.lines,
        snapshot = QuickBird::toString
    )
```

With this corresponding test case:

```kotlin
class FileSnapshotTest : JUnitFileSnapshotTest() {

    private val quickBird = QuickBird(numberOfEatenWorms = 42)

    @Test
    fun quickBird() = runTest {
        Snapshotting
            .quickBird
            .fileSnapshotting(FileStoring.text)
            .snapshot(quickBird)
    }
}
```

For more examples and a more detailed explanation see our related article
[Kotlin Snapshot Testing](link).

# 🏃 Library Setup
## 1. Add the repository
`build.gradle.kts`

```kotlin
allprojects {
    repositories {
        //...
        maven { url = uri("https://jitpack.io") }
    }
}
```

## 2. Add the dependency
`build.gradle.kts`

```kotlin
dependencies {
    //...
    // Android:
    implementation("com.github.quickbirdstudios.kotlin-snapshot-testing:snapshot-android:$version")
    // Jvm:
    implementation("com.github.quickbirdstudios.kotlin-snapshot-testing:snapshot-jvm:$version")
    // Common:
    implementation("com.github.quickbirdstudios.kotlin-snapshot-testing:snapshot:$version")
}
```

# 👤 Author
This Kotlin library is created with ❤️ by [QuickBird Studios](https://quickbirdstudios.com/).

# ❤️ Contributing
Open an issue if you need help, if you found a bug, or if you want to discuss a feature request.

Open a PR if you want to make changes to our snapshot testing library.

# 📃 License
kotlin-snapshot-testing is released under an MIT license. 
See [License](LICENSE) for more information.
