package com.comuto

import org.gradle.api.Task

/**
 * Created by m.cheikhna on 14/02/2016.
 */
final class Utils {

    static void dependsOnOrdered(Task task, Task... others) {
        task.dependsOn(others)
        for (int i = 0; i < others.size() - 1; i++) {
            others[i + 1].mustRunAfter(task[i])
        }
    }

}
