rootProject.name = "lld"
 
// Registers every problem folder under problems/ as a Gradle subproject.
// A problem folder is any directory that has a src/ folder, a REQUIREMENTS.md or a DESIGN.md,
// at any depth: problems/01-parking-lot, problems/L1-foundations/01-parking-lot, etc.
// The subproject name is the folder name without its number prefix:
//   01-parking-lot -> :parking-lot
val problemsDir = file("problems")
val markers = listOf("src", "REQUIREMENTS.md", "DESIGN.md")
 
if (problemsDir.isDirectory) {
    problemsDir.walkTopDown()
        .maxDepth(4)
        .filter { dir -> dir.isDirectory && dir != problemsDir && markers.any { File(dir, it).exists() } }
        .filter { dir -> generateSequence(dir.parentFile) { it.parentFile }
            .takeWhile { it != problemsDir }
            .none { parent -> markers.any { File(parent, it).exists() } } }   // skip src/ etc. inside a problem
        .sortedBy { it.path }
        .forEach { dir ->
            val name = dir.name.replace(Regex("^\\d+[-_.\\s]*"), "").lowercase().replace(Regex("[^a-z0-9]+"), "-")
            include(name)
            project(":$name").projectDir = dir
        }
}
