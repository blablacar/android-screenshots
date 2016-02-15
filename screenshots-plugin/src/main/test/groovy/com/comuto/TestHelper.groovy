package com.comuto

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

public class TestHelper {

    public static final File FIXTURE_WORKING_DIR = new File("src/test/fixtures/android_app")

    public static Project evaluatableProject() {
        Project project = ProjectBuilder.builder().withProjectDir(FIXTURE_WORKING_DIR).build()
        project.apply plugin: 'com.android.application'
        project.apply plugin: com.comuto.ScreenshotsPlugin

        project.android {
            compileSdkVersion 23
            buildToolsVersion '23.0.1'

            defaultConfig {
                versionCode 1
                versionName '1.0'
                minSdkVersion 23
                targetSdkVersion 23
            }

            buildTypes {
                release {
                    signingConfig signingConfigs.debug
                }
            }

            productFlavors {
                defaultConfig {}
                screenshots {
                    minSdkVersion 14
                }
            }

        }

        return project
    }

    public static Project withExtensionEvaluatableProject() {
        Project project = evaluatableProject()
        project.screenshots {
            phone="d5246a5f"
            sevenInchDevice="" //"192.168.56.101:5555"
            tenInchDevice="" //"192.168.56.102:5555"

            buildType = "debug"
            productFlavor = "screenshots"

            screenshotClass = "com.comuto.v3.TakeScreenshot"

            spoonRunnerLibPath = "gradle-screenshots-plugin/libs/spoon-runner-1.3.1-jar-with-dependencies.jar" //TODO: find a way to use it as gradle dependency
            screenshotsDir = "screenshots"

            propertiesFile = "screenshots-config.properties"
        }

    }
}
