package com.comuto

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import com.mounacheikhna.capture.CaptureRunnerTask

/**
 * Top-level plugin for managing task for running tests that generate screenshots and copying them
 * to src/main/play folder.
 **/
public class ScreenshotsPlugin implements Plugin<Project> {

    private static final String TASK_PREFIX = "screenshots"
    private static final String GROUP_SCREENSHOTS = "screenshots"

    public static final String DEFAULT_PRODUCT_FLAVOR = "defaultConfig"
    public static final String DEFAULT_BUILD_TYPE = "debug"

    @Override
    void apply(Project project) {
        project.extensions.add("screenshots", ScreenshotsExtension)

        sanitizeInput(project)

        project.afterEvaluate {
            def configPropertiesFile = "${project.projectDir}/${project.screenshots.configPropertiesFile}"
            Properties properties = null
            String[] localesStr
            if(new File(configPropertiesFile).exists()) {
                properties = ParseUtils.parseProperties(configPropertiesFile)
                String strl = properties.locales
                localesStr = strl.split(",");
            }

            def screenshotsDirName = "${project.projectDir}/${project.screenshots.screenshotsDir}"
            def putInPlayFoldersTask = project.task("screenshots", //PutInPlayStoreFolders
                    type: ProcessScreenshotsTask,
                    group: GROUP_SCREENSHOTS,
                    description: "Copy generated screenshots into play folder each in the right place.") {
                localesValues localesStr
                screenshotsOutputDir project.screenshots.screenshotsDir
                phoneSerialNo project.screenshots.phone
                sevenInchDeviceSerialNo project.screenshots.sevenInchDevice
                tenInchDeviceSerialNo project.screenshots.tenInchDevice
            }

            List<Task> localesTasks = createTestsRunTasks(project, screenshotsDirName, properties)
            String productFlavor = project.screenshots.productFlavor
            def flavorTaskName = productFlavor.capitalize()

            Task assembleTask = project.tasks.findByName("assemble$flavorTaskName")
            Task assembleTestTask = project.tasks.findByName("assembleAndroidTest")

            if (localesTasks.isEmpty()) {
                return
            }
            //TODO: use dependsOnOrdered instead of this buggy thing here
            localesTasks.get(0).dependsOn assembleTask
            localesTasks.get(0).dependsOn assembleTestTask
            int size = localesTasks.size();
            for (int i = 1; i < size; i++) {
                localesTasks.get(i).dependsOn localesTasks.get(i - 1)
            }
            putInPlayFoldersTask.dependsOn localesTasks.get(size - 1)
        }
    }

    private boolean sanitizeInput(Project project) {
        //first lets check that at least one serial nb is provided
        if([project.screenshots.phone, project.screenshots.sevenInchDevice, project.screenshots.tenInchDevice]
               .every { it?.trim() == false }) {
            throw new IllegalArgumentException("You must provide a serial number of a phone or seven " +
                    "inch or tablet device. Use adb devices command to find the serial number for the connected device.")
        }
        project.screenshots.productFlavor = project.screenshots.productFlavor ?: DEFAULT_PRODUCT_FLAVOR
        project.screenshots.buildType = project.screenshots.buildType ?: DEFAULT_BUILD_TYPE
        return true
    }

    private List<Task> createTestsRunTasks(Project project, String screenshotOutputDirName, Properties properties) {
        String screenshotProductFlavor = project.screenshots.productFlavor

        String apkPath
        if(project.screenshots.hasApkSplit) {
            apkPath = "${project.buildDir}/outputs/apk/${project.name}-$screenshotProductFlavor-universal-${project.screenshots.buildType}-unaligned.apk"
        }
        else {
            apkPath = "${project.buildDir}/outputs/apk/${project.name}-$screenshotProductFlavor-${project.screenshots.buildType}-unaligned.apk"
        }
        String testAppPath = "${project.buildDir}/outputs/apk/${project.name}-$screenshotProductFlavor-${project.screenshots.buildType}-androidTest-unaligned.apk"

        String localesStr = properties.locales
        println properties
        def locales = localesStr.split(",")
        List<Task> localesTasks = new ArrayList<>()
        locales.each {
            String currentLocale = it
            def localeFileName = properties."$currentLocale"

            def instrumArgs = [:]
            instrumArgs.put("locale", currentLocale)
            properties.findAll { k, v -> k.contains(currentLocale) }
                    .each {
                key, val ->
                    instrumArgs.put(key, val)
            }
            println "instrumArgs : $instrumArgs "

            def buildType = project.screenshots.buildType
            def suffix = project.android.buildTypes."$buildType".getVersionNameSuffix()
            def testPackage
            if(suffix?.trim()) {
                suffix = suffix.replace("-", "")
                testPackage = "${project.screenshots.appPackageName}" + "." + "${suffix}" + ".test"
            }
            else {
                testPackage = "${project.screenshots.appPackageName}" + ".test"
            }

            Task task = project.tasks.create("${currentLocale}testRunTask", CaptureRunnerTask) {
                appApkPath apkPath
                testApkPath testAppPath
                testPackageName testPackage
                serialNumber project.screenshots.phone
                outputPath "$screenshotOutputDirName"
                instrumentationArgs instrumArgs
                testClassName project.screenshots.screenshotClass
                taskPrefix "${currentLocale}"
            }

            Task generateJsonTask = project.tasks.create("${currentLocale}GenerateJson", GenerateJsonForLocaleTask) {
                locale(currentLocale)
                propertiesPath("${project.projectDir}/src/$screenshotProductFlavor/assets/$localeFileName")
                imagesPropertiesFilePath("${project.projectDir}/src/$screenshotProductFlavor/assets/images.properties")
                productFlavor(screenshotProductFlavor)
            }
            task.dependsOn generateJsonTask
            localesTasks.add(task)
        }
        localesTasks
    }

}

