play-publisher-bootstrapper
===========================

Download Play Store Metadata for your app in a special structure so it can be used in the gradle-play-publisher plugin you can find here: https://github.com/Triple-T/gradle-play-publisher

Introduction
------------

The guys from Triple-T created a nice gradle plugin that adds publishing tasks to an existing Android Application Project. You can find more information [here](https://github.com/Triple-T/gradle-play-publisher). That plugin needs a special folder structure for your metadata. This small java tool helps in bootstrapping that strucutre from an existing Play Store entry.

Prerequisites
-------------

In order to use this tool you need access to your Google Play Developer Console using a so called Service Account E-Mail and a pk12 file. You can find more information on how to create those credentials here: https://developers.google.com/android-publisher/getting_started

Usage
-----

Download the sources and compile the project with gradle
```
./gradlew assemble
```

Run
```
java -jar ./build/libs/play-publisher-bootstrap.jar
```

The tool will ask you for the following information

* **Package Name**: The package name of your app in the Google Play Store
* **Version Code**: The current version code (in order to fetch the summary of recent changes)
* **Service Account Mail**: see https://developers.google.com/android-publisher/getting_started
* **Path to pk12 File**: see https://developers.google.com/android-publisher/getting_started
* **Destination Path**: The destination you want to download the metadata to.
