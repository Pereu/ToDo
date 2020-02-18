package com.example.mytodo.tasks

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mytodo.R
import com.example.mytodo.tasks.ListMatcher.Companion.listIsEmpty
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class TasksFragmentTest {


    @Test
    fun taskIsCreated() {
        launchActivity()

        onView(withId(R.id.tasks_list)).check(matches(listIsEmpty()))

    }

    private fun launchActivity() {
        ActivityScenario.launch(TasksActivity::class.java)
    }
}