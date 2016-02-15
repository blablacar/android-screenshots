package com.comuto
/**
 * Extension for the parameters used by the plugin.
 */
class ScreenshotsExtension {

    /** Serial number of a phone device. **/
    String phone
    /** Serial number of a seven inch device. **/
    String sevenInchDevice
    /** Serial number of a ten inch device. **/
    String tenInchDevice

    /** The {@link com.android.builder.model.ProductFlavor} for which we take screenshots. **/
    String productFlavor

    /** The {@link com.android.builder.model.BuildType} in the flavor in which we take screenshots.**/
    String buildType

    /** Class name of the test class that uses spoon to take screenshots.**/
    String screenshotClass

    /** Name of the directory in which to put the generated screenshots (path is relative to the app folder)**/
    String screenshotsDir

    String spoonRunnerLibPath

    String configPropertiesFile //this is realy properties config files

    String localPropertiesFolderPath

    String appPackageName

    boolean hasApkSplit
}
