package com.example.mytodo.tasks

import android.view.View
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description

class ListMatcher {

    companion object {
        fun listIsEmpty(): BoundedMatcher<View, RecyclerView> {
            return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("RecyclerView should be empty in the beginning")
                }

                override fun matchesSafely(view: RecyclerView?): Boolean {
                    return view?.size == 0
                }
            }
        }
    }
}