# Fastream - an Android Event Tracking Library

[![Release](https://jitpack.io/v/fastream/android-sdk.svg)](https://jitpack.io/#fastream/android-sdk)

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
    implementation 'com.github.fastream:android-sdk:0.0.10'
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

### Flushing events

Automatic flushing is enabled by default.

- Change frequency to 10s
```
val fastream = Fastream.init(
    url = "https://<YOUR-PREFIX>.fastream.io",
    token = "<YOUR_TOKEN>",
    context = this.context!!,
    autoFlushIntervalSeconds = 10
)
```

- Disable auto flush
```
val fastream = Fastream.init(
    url = "https://<YOUR-PREFIX>.fastream.io",
    token = "<YOUR_TOKEN>",
    context = this.context!!,
    autoFlushIntervalSeconds = null
)
```

- Flush manually
```
fastream.flush()
```

### Sample event

```
{
  "event": "Your event",
  "_metadata": {
    "@uuid": "97619935-16e2-4b56-9635-d6acb6f26f23-0",
    "token": "<YOUR_TOKEN>",
    "@version": 1,
    "client_ip": "81.51.118.138",
    "@timestamp": "2020-03-18T15:23:36.137",
    "input_name": "<YOUR_INPUT_NAME>",
    "input_type": "MobileSdkInput",
    "@parent_uuid": "",
    "failures_count": 0,
    "restream_count": 0,
    "input_event_name": "<YOUR_INPUT_EVENT_NAME>",
    "client_user_agent": "OKHTTP/3.14.9",
    "input_event_uniq_name": "<YOUR_INPUT_EVENT_UNIQ_NAME>"
  },
  "properties": {
    "$os": "Android",
    "time": 1618759412,
    "$wifi": true,
    "$brand": "samsung",
    "$model": "SM-N975F",
    "$carrier": "Orange",
    "$has_nfc": true,
    "session_id": "74bb79f0-4121-482d-831b-983e1cd5a874",
    "$os_version": "11",
    "$screen_dpi": 280,
    "distinct_id": "9b7509389051224d",
    "$app_version": "1.0",
    "$lib_version": "0.0.10",
    "fastream_sdk": "android",
    "$manufacturer": "samsung",
    "$screen_width": 720,
    "$has_telephone": true,
    "$screen_height": 1379,
    "$bluetooth_enabled": true,
    "$bluetooth_version": "ble"
  }
}
```

## Author

Fastream dev@fastream.io

## License

Fastream SDK is available under the Apache v2 license. See the LICENSE file for more info.