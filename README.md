Usage

dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url = uri("https://jitpack.io") }
		}
	}

Add at dependency

dependencies {
	        implementation("com.github.ticherhaz:FireLog:1.0.0")
	}
