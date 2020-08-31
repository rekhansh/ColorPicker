# ColorPicker [![](https://jitpack.io/v/rekhansh/ColorPicker.svg)](https://jitpack.io/#rekhansh/ColorPicker) [![platform](https://img.shields.io/badge/platform-android-brightgreen.svg)](https://developer.android.com/index.html)
A `ColorPicker` for Android.

<img src='Screenshots/ColorPicker1.png' width='32%'/> <img src='Screenshots/ColorPicker2.png' width='32%'/> <img src='Screenshots/ColorPicker3.png' width='32%'/>

## Gradle

```
...
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
...
dependencies {
    implementation 'com.github.rekhansh:ColorPicker:${latestVersion}'
    ...
}
```
> Replace `${latestVersion}` with the latest version code. See [releases](https://github.com/rekhansh/ColorPicker/releases).

## Use
#### Java
```
ColorPickerDialog colorPicker = new ColorPickerDialog(selectedColor,(red, green, blue, alpha) -> {
                    int selectedColor = Color.argb((int) alpha,(int) red,(int) green, (int) blue);
                    //TODO - Use Color
                });
colorPicker.show(requireActivity().getSupportFragmentManager(),"color_picker");
```
#### Kotlin
```
ColorPickerDialog(selectedColor,object :ColorPickerDialog.ColorPickerDialogListener{
                override fun onColorChange(red: Float, green: Float, blue: Float, alpha: Float) {
                    val selectedColor = Color.argb(alpha.toInt(),red.toInt(),green.toInt(), blue.toInt())
                    //TODO - Use Color
                }
            }).show(supportFragmentManager,"colorPicker")
```
## License
  
    Copyright 2020 Rekhansh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
