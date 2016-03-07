package com.comuto


interface GenerateJsonSpec {

    void locale(String locale)
    void firstPassConfig(String firstPassConfigPath, String startKeyDelimiter,
                         String endKeyDelimiter, boolean escaped)
    void secondPassConfig(String secondPassConfigPath, String startKeyDelimiter,
                          String endKeyDelimiter, boolean escaped)
    void productFlavor(String productFlavor)
    void jsonFiles(Map<String, String> files)

}