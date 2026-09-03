# Hanten parsers

This library provides a collection of manga parsers for convenient access manga available on the web. It can be used in
JVM and Android applications.

> This repository is based on [Kotatsu-Redo/kotatsu-parsers-redo](https://github.com/clquwu/kotatsu-parsers-redo)
> (the parser base used by Futon) with the package rebranded to `hanten.wre.app.parsers`.

![Sources count](https://img.shields.io/badge/dynamic/yaml?url=https%3A%2F%2Fraw.githubusercontent.com%2FDezFix%2Fhanten-parsers%2Frefs%2Fheads%2Fmaster%2F.github%2Fsummary.yaml&query=total&label=manga%20sources&color=%23E9321C) [![](https://jitpack.io/v/DezFix/hanten-parsers.svg)](https://jitpack.io/#DezFix/hanten-parsers) ![License](https://img.shields.io/github/license/DezFix/Hanten)

## Usage

1. Add it to your root build.gradle at the end of repositories:

   ```groovy
   allprojects {
	   repositories {
		   ...
		   maven { url 'https://jitpack.io' }
	   }
   }
   ```

2. Add the dependency

   For Java/Kotlin project:
    ```groovy
    dependencies {
        implementation("com.github.DezFix:hanten-parsers:$parsers_version")
    }
    ```

   For Android project:
    ```groovy
    dependencies {
        implementation("com.github.DezFix:hanten-parsers:$parsers_version") {
            exclude group: 'org.json', module: 'json'
        }
    }
    ```

   Versions are available on [JitPack](https://jitpack.io/#DezFix/hanten-parsers)

   When used in Android
   projects, [core library desugaring](https://developer.android.com/studio/write/java8-support#library-desugaring) with
   the [NIO specification](https://developer.android.com/studio/write/java11-nio-support-table) should be enabled to
   support Java 8+ features.


3. Usage in code

   ```kotlin
   val parser = mangaLoaderContext.newParserInstance(MangaParserSource.MANGADEX)
   ```

   `mangaLoaderContext` is an implementation of the `MangaLoaderContext` class.
   See examples
   of [Android](https://github.com/DezFix/Hanten/blob/devel/app/src/main/kotlin/hanten/wre/app/core/parser/MangaLoaderContextImpl.kt)
   implementation.

## Projects that use the library

- [Hanten](https://github.com/DezFix/Hanten)
- [Futon](https://github.com/AppFuton/Futon)
- [Kototoro](https://github.com/Kototoro-app/Kototoro)
  
## Contribution

See [CONTRIBUTING.md](./CONTRIBUTING.md) for the guidelines.

## DMCA disclaimer

The developers of this application have no affiliation with the content available in the app. It is collected from
sources freely available through any web browser.
