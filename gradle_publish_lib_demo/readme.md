# 项目概览

## 1.代码层

![image-20231228110742333](https://s2.loli.net/2023/12/28/ujyrNenb4m2BsgI.png)

## 2.lib 的build.gradle/build.gradle.kts配置

```
plugins {
	// 上传plugin需要使用的依赖库
    // Apply the java-library plugin for API and implementation separation. 
    `java-library`
    java
    id("com.gradle.plugin-publish") version "1.2.1"
}

// 你的lib版本信息
// The project version will be used as your plugin version when publishing.
version = "1.0.2"
group = "io.github.nolan"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

//你的lib项目使用的依赖
dependencies {
    // Use JUnit test framework.
    testImplementation(libs.junit)

    // This dependency is exported to consumers, that is to say found on their compile classpath.
    api(libs.commons.math3)

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
    implementation(libs.guava)
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

//gradle lib项目的信息配置
gradlePlugin {
	//网站
    website.set("https://github.com/F1Robert/stack-overflow-of-nolan")
    vcsUrl.set("https://github.com/F1Robert/stack-overflow-of-nolan")
    plugins {
        create("NolanGreetingsPlugin") {
            id = "io.github.nolan.greeting"
            //主Class需要写报名+类名的完整路径
            implementationClass = "io.github.nolan.gradle.GreetingPlugin"
            displayName = "Gradle Greeting plugin"
            description = "Gradle plugin to say hello!"
            tags.set(listOf("search", "tags", "for", "your", "hello"))
        }
        create("NolanGoodbyePlugin") {
            id = "io.github.nolan.goodbye"
            implementationClass = "io.github.nolan.gradle.GoodbyePlugin"
            displayName = "Gradle Goodbye plugin"
            description = "Gradle plugin to say goodbye!"
            tags.set(listOf("search", "tags", "for", "your", "goodbye"))
        }
    }
}
```

