# Marcapollo QuestSDK for Android #



##  Getting Started ##

Add lines to build.gradle:

- This block will be removed once we link the repository to jCenter
```gradle

// Remove this once we link the repository to jCenter.
repositories {
    maven {
        url 'https://dl.bintray.com/marcapollo/maven'
    }
}
```

- Add to dependencies.
```gradle
dependencies {
	compile ('com.marcapollo.questsdk:questsdk:1.0.5@aar') {
		transitive = true
	}
}
```

- Add permissions to application AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```

- Add to proguard-rules
```proguard
# Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# model
-dontwarn com.marcapollo.questsdk.**
-keep class com.marcapollo.questsdk.** { *; }
# https://github.com/square/okio/issues/60
-dontwarn okio.**
# https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-square-okhttp.pro
-keepattributes *Annotation*
-keepattributes Signature
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**
```

## Example ##
Examples can be found in [app](app)

## Dependencies ##

The beacon detection depends on [Android Beacon Library](http://altbeacon.github.io/android-beacon-library/index.html)

HTTP client depends on [Retrofit](http://square.github.io/retrofit/) Beta 1

JSON seriealization depends on [Moshi](https://github.com/square/moshi) 1.0.0
