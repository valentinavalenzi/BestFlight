// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.android.application") version "8.5.2" apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false
}