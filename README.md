# Marcapollo QuestSDK for Android #



##  Getting Started ##

Add lines to build.gradle:

- This block will be removed once we link the repository to jCenter
```gradle

// Remove this once we link the repository to jCenter.
repositories {
    maven {
        url 'https://dl.bintray.com/shine-chen/maven'
    }
}
```

- Add to dependencies.
```gradle
dependencies {
	compile ('com.marcapollo.questsdk:questsdk:1.0.2@aar') {
		transitive = true
	}
}

```

## Example ##
Examples can be found in [app](app)

## Dependencies ##

The beacon detection depends on [Android Beacon Library](http://altbeacon.github.io/android-beacon-library/index.html)

HTTP client depends on [Retrofit](http://square.github.io/retrofit/) Beta 1

JSON seriealization depends on [Moshi](https://github.com/square/moshi) 1.0.0
