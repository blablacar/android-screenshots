package com.comuto

import org.gradle.api.Project
import org.junit.Assert
import org.junit.Test

class GenerateJsonForLocalTest {

    @Test
    public void testGenerateJsons() {
        Project project = TestHelper.withExtensionEvaluatableProject()
        project.evaluate()

        //add properties
        project.tasks.fr_FRGenerateJson.execute()

        //Assert.assertTrue(true)
    }

}
