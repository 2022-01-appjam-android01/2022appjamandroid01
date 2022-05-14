package com.applay22.appjam01.partInfo

import junit.framework.TestCase
import org.junit.Test

class PartFieldsTest : TestCase(){
    @Test
    fun testIteration(){
        val fields =PartFields()
        fields.add_used(1f)
    }
}