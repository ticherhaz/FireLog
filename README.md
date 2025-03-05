# Usage

## Dependency Resolution Management

Add the following to your `settings.gradle` file:

```groovy
dependencyResolutionManagement { 
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { 
            url = uri("https://jitpack.io") 
        }
    }
}

dependencies { 
    implementation("com.github.ticherhaz:FireLog:1.1.5")
}
```