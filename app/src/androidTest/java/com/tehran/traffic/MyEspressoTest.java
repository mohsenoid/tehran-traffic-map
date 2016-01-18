package com.tehran.traffic;

import android.test.ActivityInstrumentationTestCase2;

import com.tehran.traffic.ui.MainActivity;

import org.junit.Before;

import android.support.test.InstrumentationRegistry;

/**
 * Created by Mohsen on 11/19/15.
 */
public class MyEspressoTest
        extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public MyEspressoTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }

    //...
}