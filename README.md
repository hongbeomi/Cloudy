# <p align="center">Cloudy</p>
<p align="center">
<img alt="api" src="https://img.shields.io/badge/API-21%2B-green?logo=android"/>
<img alt="api" src="https://img.shields.io/github/workflow/status/hongbeomi/Cloudy/Android%20CI?color=dark%20green"/>
<img alt="license" src="https://img.shields.io/github/license/hongbeomi/cloudy?color=blue&logo=apache"/>
</p>
<p align="center">☁️ Cloudy is help apply blur and filters to any view</p>

<img src="https://github.com/hongbeomi/Cloudy/blob/master/image/sample_cloudy.gif" align="left" width="30%"></img>  <img src="https://github.com/hongbeomi/Cloudy/blob/master/image/sample_white.png" align="center" width="30%"></img>  <img src="https://github.com/hongbeomi/Cloudy/blob/master/image/sample_gray.png" align="right" width="30%"></img> 

<br/>

## ⚡️ Include project   

### Gradle 

### [![](https://jitpack.io/v/hongbeomi/Cloudy.svg)](https://jitpack.io/#hongbeomi/Cloudy)

Add it in your **root** build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

And add a this code to your **module**'s `build.gradle` file.

```groovy
dependencies {
    implementation 'com.github.hongbeomi:Cloudy:Tag'
}
```

<br/>

## 👀 Usage

### Basic Example

```kotlin
val cloud = Cloudy.with(this) // context
    // source view (any view) isPreBlur If you specify true, it starts immediately.
    .from(imageView, isPreBlur = true) 
    // recommend argb (@ColorInt)
    .color(Color.parseColor("#50ffffff")) 
    // set radius
    .radius(200f)
    // target view (any view)
    .into(textView) 
```

### Lazy start blur

```kotlin
cloud.blur() // start blur
```

### Lazy change Color

```kotlin
cloud.color(Color.CYAN) // @ColorInt
```

### Lazy change Radius

```kotlin
cloud.radius(25f) // range is 0f .. 200f
```

### Scroll Changed Listener

```kotlin
// vertical
verticalScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
    cloud.onVerticalScroll(scrollY)                                      
}
// horizontal 
horizontalScrollView.setOnScrollChangeListener { _, scrollX, _, _, _ ->
    cloud.onHorizontalScroll(scrollX)                                      
}
```

### Clear

```kotlin
// target view background = null
cloud.clear()
```

### Get blurred bitmap

```kotlin
cloud.get() // nullable bitmap
```

<br/>

## Blur Algorithm

- Stack Blur - Mario Klingemann [link](https://underdestruction.com/2004/02/25/stackblur-2004/)

<img src="https://underdestruction.com/wordpress/wp-content/uploads/2016/02/StackBlur01.png" align="center" width="50%"></img>

<br/>

## 🙌 Thanks

- [Blurry] wasabeef -  https://github.com/wasabeef/Blurry
- [BlurSample] charlezz - https://github.com/Charlezz/BlurSample

<br/>

## 🌟 Find this project useful?

Support it by joining [stargazers](https://github.com/hongbeomi/Cloudy/stargazers) for this repository

<br/>

## 📝 License

```
 Copyright [2021] [Hongbeom Ahn]

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```



### 