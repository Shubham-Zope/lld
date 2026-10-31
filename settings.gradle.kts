rootProject.name = "lld-mastery"

// Every problems/<level>/<NN-slug>/ folder is a subproject named after its slug,
// e.g. problems/L1-foundations/01-parking-lot -> :parking-lot
file("problems").listFiles()?.filter { it.isDirectory }?.sorted()?.forEach { level ->
    level.listFiles()?.filter { it.isDirectory && File(it, "src").isDirectory }?.sorted()?.forEach { dir ->
        val name = dir.name.replace(Regex("^\\d+-"), "")
        include(name)
        project(":$name").projectDir = dir
    }
}
