package by.equestriadev.nikishin_rostislav;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Bronydell on 2/23/18.
 */

public class WelcomeUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class,
            true,
            false);
    Intent intent;
    SharedPreferences.Editor preferencesEditor;

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Before
    public void setUp() {
        intent = new Intent();
        Context context = getInstrumentation().getTargetContext();

        // create a SharedPreferences editor
        preferencesEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    @Test
    public void themeTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        preferencesEditor.clear();
        preferencesEditor.apply();
        mActivityRule.launchActivity(intent);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = mActivityRule.getActivity().getDefaultPrefs();
        String theme = prefs.getString("theme", "light");
        ViewInteraction viewPager = onView(
                allOf(withId(R.id.pager),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        viewPager.perform(swipeLeft());


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int clickPosition = 0;
        if (theme.equals("light"))
            clickPosition = 1;
        onView(allOf(isDisplayed(), withId(R.id.theme_list)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(clickPosition, click()));
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        prefs = mActivityRule.getActivity().getDefaultPrefs();
        assertNotEquals(prefs.getString("theme", "light"), theme);

    }

    @Test
    public void layoutTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        preferencesEditor.clear();
        preferencesEditor.apply();
        mActivityRule.launchActivity(intent);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = mActivityRule.getActivity().getDefaultPrefs();
        String layout = prefs.getString("layout", "compact");
        ViewInteraction viewPager = onView(
                allOf(withId(R.id.pager),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        viewPager.perform(swipeLeft());
        viewPager.perform(swipeLeft());


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int clickPosition = 0;
        if (layout.equals("compact"))
            clickPosition = 1;
        onView(allOf(isDisplayed(), withId(R.id.theme_list)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(clickPosition, click()));
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        prefs = mActivityRule.getActivity().getDefaultPrefs();
        assertNotEquals(prefs.getString("layout", "compact"), layout);

    }

    @Test
    public void infoTest() {
        preferencesEditor.putBoolean("visited", true);
        preferencesEditor.apply();

        mActivityRule.launchActivity(intent);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ViewInteraction viewPager = onView(
                allOf(withId(R.id.gridPager),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragmentManager),
                                        0),
                                0),
                        isDisplayed()));
        viewPager.perform(swipeLeft());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(allOf(isDisplayed(), withId(R.id.app_grid)))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));
        onView(withText("Usage frequency"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void welcomeTest() {
        preferencesEditor.clear();
        preferencesEditor.apply();
        mActivityRule.launchActivity(intent);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = mActivityRule.getActivity().getDefaultPrefs();
        String layout = prefs.getString("layout", "compact");
        ViewInteraction viewPager = onView(
                allOf(withId(R.id.pager),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        viewPager.perform(swipeLeft());
        viewPager.perform(swipeLeft());
        viewPager.perform(swipeLeft());


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.finish_button)).perform(click());
        viewPager = onView(
                allOf(withId(R.id.gridPager),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.fragmentManager),
                                        0),
                                0),
                        isDisplayed()));
        viewPager.check(matches(isDisplayed()));

    }

    @Test
    public void navTest() {
        preferencesEditor.putBoolean("visited", true);
        preferencesEditor.apply();

        mActivityRule.launchActivity(intent);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.navView)).perform(NavigationViewActions.navigateTo(R.id.nav_grid));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.drawer_layout)).check(matches(isClosed()));
        onView(allOf(isDisplayed(), withId(R.id.app_grid))).check(matches(isDisplayed()));
    }
}
