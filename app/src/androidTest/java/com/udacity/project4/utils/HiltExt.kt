package com.udacity.project4.utils

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions
import com.udacity.project4.HiltTestActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.udacity.project4.R

@ExperimentalCoroutinesApi
inline fun <reified T : Fragment> launchFragmentInHiltActivity(
    fragmentArgs: Bundle? = null,
    themeResId: Int = R.style.Theme_SAMTasks,
    fragmentFactory: FragmentFactory? = null,
    crossinline action: T.() -> Unit = {}
) {
    val mainActivityIntent = Intent.makeMainActivity(
        ComponentName(
            ApplicationProvider.getApplicationContext(),
            HiltTestActivity::class.java
        )
    ).putExtra(
        "androidx.fragment.app.testing.FragmentScenario.EmptyFragmentActivity.THEME_EXTRAS_BUNDLE_KEY",
        themeResId
    )
    ActivityScenario.launch<HiltTestActivity>(mainActivityIntent).onActivity { testActivity ->
        fragmentFactory?.let {
            testActivity.supportFragmentManager.fragmentFactory = it
        }
        val fragment = testActivity.supportFragmentManager.fragmentFactory.instantiate(
            Preconditions.checkNotNull(T::class.java.classLoader),
            T::class.java.name
        )
        fragment.arguments = fragmentArgs
        testActivity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment, "")
            .commitNow()
        (fragment as T).action()
    }
}