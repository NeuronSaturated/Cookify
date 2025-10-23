pluginManagement {
    repositories {
        google() // Repositorio de Google para Firebase y otros plugins
        mavenCentral() // Repositorio de Maven Central
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // Asegura que Google esté en el repositorio
        mavenCentral() // También Maven Central
    }
}

rootProject.name = "Cookify"
include(":app")