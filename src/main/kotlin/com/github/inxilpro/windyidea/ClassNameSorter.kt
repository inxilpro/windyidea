package com.github.inxilpro.windyidea

object ClassNameSorter
{
    // This ensures that we're only manipulating normal CSS classes and not Blade/JS/etc
    private val validator = Regex("""([_a-zA-Z0-9.\s\-:\[\]/]+)""")

    fun sortClasses(classes: String): String {
        if (! validator.matches(classes)) {
            return classes
        }

        return classes
            .trim()
            .split(Regex("\\s+"))
            .distinct()
            .map(ClassNameSorter::splitClassNameToParts)
            .sortedWith(ClassNameComparator)
            .joinToString(separator = " ", transform = ClassNameSorter::mergePartsIntoClassName)
    }

    private fun splitClassNameToParts(className: String): Pair<String?, String> {
        val parts = className.split(":", limit = 2).reversed()
        return parts.getOrNull(1) to parts[0]
    }

    private fun mergePartsIntoClassName(parts: Pair<String?, String>): String {
        return "${parts.first?.let { variant -> "$variant:" } ?: ""}${parts.second}"
    }
}