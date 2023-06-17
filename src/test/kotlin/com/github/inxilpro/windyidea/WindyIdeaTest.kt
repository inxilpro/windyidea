package com.github.inxilpro.windyidea

import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase

class WindyIdeaTest : BasePlatformTestCase() {
    fun testItReordersClassNamesAsExpected() {
        val input = "  xl:relative   bogus:foo absolute sm:flex inline-block sm:absolute inline flex  relative sm:inline-block   sm:relative xl:absolute   xl:random-class  4xl:inline-block random-class  justify-items    another-random-class 4xl:flex     ";
        val expected = "inline-block inline flex absolute relative sm:inline-block sm:flex sm:absolute sm:relative xl:absolute xl:relative 4xl:inline-block 4xl:flex random-class justify-items another-random-class xl:random-class bogus:foo";

        TestCase.assertEquals(expected, ClassNameSorter.sortClasses(input))
    }
}
