plugins {
    kotlin("jvm") version "1.6.20" // Adjust according to the latest version if needed
}

repositories {
    mavenCentral()
    // Add additional repositories if needed
    maven { url = uri("https://jitpack.io") } // For Cloudstream dependencies
}

dependencies {
    implementation(kotlin("stdlib"))
    // Add Cloudstream dependencies
    compileOnly("com.github.recloudstream:cloudstream:master-SNAPSHOT") // Cloudstream API
    implementation("org.jsoup:jsoup:1.15.3") // For HTML parsing
    // Add additional dependencies here if required
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    
    jar {
        // Ensure the jar is executable
        manifest {
            attributes(
                mapOf(
                    "Main-Class" to "com.example.cloudstream.TukTukProvider",
                    "Plugin-Name" to "TukTuk Cinema",
                    "Plugin-Version" to "1.0.0"
                )
            )
        }
        // Include dependencies in the jar
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
        // Exclude META-INF files from included JARs
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }
    
    // Task to create .cs3 file
    register<Zip>("createCS3") {
        archiveFileName.set("TukTukProvider.cs3")
        destinationDirectory.set(file("$buildDir/libs"))
        
        from(jar)
        from(file("META-INF/MANIFEST.MF"))
    }
}