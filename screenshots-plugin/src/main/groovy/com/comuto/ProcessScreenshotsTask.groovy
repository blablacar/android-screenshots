package com.comuto

import groovy.io.FileType
import org.apache.commons.lang3.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Nullable
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction

/**
 * A {@linkplain DefaultTask task} that parses screenshots generated using spoon by the android
 * tests provided in {@link ScreenshotsExtension#buildType} in the flavor
 * {@link ScreenshotsExtension#productFlavor}.
 *
 * To debug this, you can use the following command and attach a remote debugger to port 5005
 * ./gradlew clean build --no-daemon -Dorg.gradle.debug=true
 */
public class ProcessScreenshotsTask extends DefaultTask implements ProcessScreenshotsSpec {

    static final String PLAY_FOLDER_RELATIVE_PATH = "src/main/play"
    private static final String PHONE = "phone"
    private static final String SEVEN_INCH_DEVICE = "sevenInch"
    private static final String TEN_INCH_DEVICE = "tenInch"

    private List<DeviceDetails> devicesDetails
    private String[] localesValues
    private String screenshotsOutputDir
    private String phoneSerialNo
    private String sevenInchDeviceSerialNo
    private String tenInchDeviceSerialNo

    @TaskAction
    void performTask() {
        initDevicesDetails()
        if (!project.plugins.hasPlugin('android')) {
            throw new StopExecutionException("The 'android' plugin is required.")
        }
        File screenshotsDir = getScreenshotsOutputDir()
        copyToPlayFolders(screenshotsDir, localesValues)
    }

    void initDevicesDetails() {
        devicesDetails = new ArrayList<>(3)
        if (phoneSerialNo != null && !phoneSerialNo.empty) {
            devicesDetails.add(new DeviceDetails(PHONE, phoneSerialNo))
        }

        if (sevenInchDeviceSerialNo != null && !sevenInchDeviceSerialNo.empty) {
            devicesDetails.add(new DeviceDetails(SEVEN_INCH_DEVICE, sevenInchDeviceSerialNo))
        }

        if (tenInchDeviceSerialNo != null && !tenInchDeviceSerialNo.empty) {
            this.devicesDetails.add(new DeviceDetails(TEN_INCH_DEVICE, tenInchDeviceSerialNo))
        }
    }

    File getScreenshotsOutputDir() {
        return new File("${project.projectDir}/$screenshotsOutputDir")
    }

    void copyToPlayFolders(File screenshotsDir, String[] locales) {
        DeviceDetails defaultDevice = new DeviceDetails(PHONE, phoneSerialNo)
        screenshotsDir.eachFileRecurse(FileType.FILES) {
            def foundlocalIndex = StringUtils.indexOfAny(it.name, locales);
            if (it.name.contains(".png") && foundlocalIndex != -1) {
                def locale = it.name.substring(foundlocalIndex, foundlocalIndex + 5)
                def localeFolder = playFolderNameByLocale(locale)
                copyImageToPlayFolder(it, new File(playImagesDir(defaultDevice, localeFolder)), locale)
            }
        }
    }

    DeviceDetails findDeviceForDirectory(File dir) {
        def patternDeviceNbPart = ~/\d+_/
        def deviceSerialNumber = dir.name.findAll(patternDeviceNbPart).join(".").replace("_", "")
        if (deviceSerialNumber == null || deviceSerialNumber.empty) {
            deviceSerialNumber = dir.name
        }
        this.devicesDetails.find({ it.serialNo.contains(deviceSerialNumber) })
    }

    void copyImageToPlayFolder(File file, File playImagesDir, String locale) {
        def name = "copy${file.name}"
        if (project.tasks.findByName(name)) {
            return
        }
        project.tasks.create(name, Copy) {
            from file.path
            into playImagesDir
            rename "(.*)_($locale)_(.*).png", '$3.png'
        }.execute()
    }

    String playFolderNameByLocale(String locale) {
        locale.replace("_", "-")
    }

    String playImagesDir(@Nullable DeviceDetails deviceDetails, String localeFolder) {
        String playImagesDir = "${project.getProjectDir()}/$PLAY_FOLDER_RELATIVE_PATH/$localeFolder/listing/"

        if (deviceDetails == null || deviceDetails.type == PHONE) {
            playImagesDir += "phoneScreenshots"
        } else if (deviceDetails.type == SEVEN_INCH_DEVICE) {
            playImagesDir += "sevenInchScreenshots"
        } else if (deviceDetails.type == TEN_INCH_DEVICE) {
            playImagesDir += "tenInchScreenshots"
        }
        playImagesDir
    }

    @Override
    void screenshotsOutputDir(String dir) {
        this.screenshotsOutputDir = dir
    }

    @Override
    void localesValues(String[] localesValues) {
        this.localesValues = localesValues
    }

    @Override
    void phoneSerialNo(String phoneSerialNo) {
        this.phoneSerialNo = phoneSerialNo
    }

    @Override
    void sevenInchDeviceSerialNo(String sevenInchDeviceSerialNo) {
        this.sevenInchDeviceSerialNo = sevenInchDeviceSerialNo
    }

    @Override
    void tenInchDeviceSerialNo(String tenInchDeviceSerialNo) {
        this.tenInchDeviceSerialNo = tenInchDeviceSerialNo
    }

}
