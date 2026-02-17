# Grails JavaExec Toolchain Inheritance Bug

This sample Grails 7 app demonstrates a Gradle Java toolchain bug where JavaExec tasks ignore the configured toolchain and instead use the JDK running the Gradle daemon.

## Bug Summary

When a project configures a Java toolchain, Gradle applies it automatically to JavaCompile, Javadoc, and Test tasks. JavaExec tasks are excluded and therefore default to the Gradle daemon JDK. In Grails, this affects forked tasks like dbm-* migration commands, console, and shell.

## Reproduction

1. Run:

   ```bash
   ./gradlew checkToolchain
   ```

2. Observe output similar to:

   ```
   java.home=...
   java.version=...
   matchesToolchain=false
   ```

The JavaExec task should use the JDK configured by the toolchain, but it uses the Gradle daemon JDK instead.

## Expected Behavior

`checkToolchain` should run with the toolchain JDK configured in `build.gradle`:

```groovy
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
```

## Workaround

Manually set `javaLauncher` on each JavaExec task:

```groovy
import org.gradle.jvm.toolchain.JavaToolchainService

def toolchainService = extensions.getByType(JavaToolchainService)
def launcher = toolchainService.launcherFor(java.toolchain)

tasks.withType(JavaExec).configureEach {
    javaLauncher.set(launcher)
}
```

## Impact

This primarily affects JavaExec-based tasks such as dbm-* database migrations, console, shell, and other ApplicationContextCommandTask usages.
