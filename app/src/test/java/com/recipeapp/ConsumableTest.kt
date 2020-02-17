package com.recipeapp

import com.recipeapp.core.Resource
import com.recipeapp.core.asConsumable
import com.recipeapp.core.isOfType
import org.junit.Test
import kotlin.test.assertFalse

class ConsumableTest: UnitTest() {

    @Test
    fun testConsumableType(){
        val resource = Resource.Uninitialized.asConsumable()
        assert(resource.isOfType(Resource.Uninitialized))
        assertFalse {
            resource.isOfType(Resource.Loading)
        }
    }

}