# Fastream - an Android Event Tracking Library

[![Release](https://jitpack.io/v/fastream/android-sdk.svg)]
(https://jitpack.io/#fastream/android-sdk)

## Installation

Integrating the Fastream SDK can be done in a few simple steps

### Add JitPack repository to your build.gradle

```
repositories {
    jcenter()
    maven { url "https://jitpack.io" }
}
```
### Add Fastream SDK dependency

```
dependencies {
    implementation 'com.github.fastream:sdk:0.0.8'
}
```

### Usage

```
val fastream = Fastream.init(
    url = "https://<YOUR-PREFIX>.fastream.io",
    token = "<YOUR_TOKEN>",
    context = this.context!!
    )
fastream.track("Your event")
```

That's all!

## Author

Fastream dev@fastream.io

## License

Fastream SDK is available under the Apache v2 license. See the LICENSE file for more info.