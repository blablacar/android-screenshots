package com.comuto

import org.junit.Before
/**
 * Created by m.cheikhna on 04/11/2016.
 */
class ScreenshotsTest {

    @Before
    public void setUp() throws Exception {
        project = ProjectBuilder.builder().withProjectDir(new File(FIXTURE_WORKING_DIR)).build()
        project.apply plugin: 'android'
        project.evaluate()
    }

/*
    @Test
    public void simple() {
        Task screenshotsTask = project.tasks.create("screenshotsTask", ProcessScreenshotsTask.class)

    }
*/

}
