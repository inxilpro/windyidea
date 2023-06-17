package com.github.inxilpro.windyidea

object ClassNameComparator : Comparator<Pair<String?, String>> {
    private val comparator = compareBy<Pair<String?, String>>(
        ClassNameComparator::isTailwindClass,
        ClassNameComparator::isNonVariantClass,
        ClassNameComparator::sortByVariant,
        ClassNameComparator::sortByClassName,
    )

    override fun compare(o1: Pair<String?, String>?, o2: Pair<String?, String>?): Int {
        return comparator.compare(o1, o2)
    }

    private fun isTailwindClass(parts: Pair<String?, String>): Int {
        return if (Ordering.SORT_ORDER.indexOf(parts.second) >= 0) 0 else 1
    }

    private fun isNonVariantClass(parts: Pair<String?, String>): Int {
        return parts.first?.let { 1 } ?: 0
    }

    private fun sortByVariant(parts: Pair<String?, String>): Int? {
        return parts.first?.let { variant -> indexOfOrMaxInt(Ordering.VARIANT_ORDER, variant) }
    }

    private fun sortByClassName(parts: Pair<String?, String>): Int? {
        return indexOfOrMaxInt(Ordering.SORT_ORDER, parts.second)
    }

    private fun indexOfOrMaxInt(order: List<String>, value: String): Int {
        return order.indexOf(value).takeIf { it >= 0 } ?: Int.MAX_VALUE
    }
}