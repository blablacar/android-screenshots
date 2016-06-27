
### Android Plugin and Library to automate taking screenshots on Android

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat)](https://github.com/blablacar/android-screenshots/blob/master/LICENSE.txt)

###### Android Plugin and Library to automate taking screenshots on Android with custom content on multiple devices and with multiple languages

Usage
----

  - For the plugin :
      - For all gradle versions : 
      ```gradle
      buildscript {
        repositories {
          maven {
            url "https://plugins.gradle.org/m2/"
          }
        }
        dependencies {
          classpath "gradle.plugin.com.comuto.screenshots:screenshots-plugin:0.1.1"
        }
      }
      
      apply plugin: "com.comuto.screenshots"
      ```
      
      - For Gradle >= 2.1 
      ```gradle
        plugins {
          id "com.comuto.screenshots" version "0.1.1"
        }
        ```

Contributing
============

* [Check for open issues](https://github.com/blablacar/android-screenshots/issues) or open
   a fresh issue to start a discussion around a feature idea or a bug.
* Fork the [repository on Github](https://github.com/blablacar/android-screenshots)
   to start making your changes.
* Send a pull request and bug the maintainer until it gets merged and published.
   :) Make sure to add yourself to ``CONTRIBUTORS.txt``.

License
-------

    Copyright (C) 2016 BlablaCar

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



