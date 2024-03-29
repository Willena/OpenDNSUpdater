package fr.guillaumevillena.opendnsupdater.utils;

import android.view.View;

import java.util.HashMap;

import androidx.appcompat.widget.AppCompatImageView;
import fr.guillaumevillena.opendnsupdater.TestState;

/**
 * Created by guill on 25/06/2018.
 */

public class StateSwitcher {

    private TestState currentState;

    private final HashMap<TestState, Integer> drawableMap;
    private final HashMap<TestState, View> viewMap;

    public StateSwitcher() {
        this.currentState = TestState.UNKNOWN;
        this.viewMap = new HashMap<>();
        this.drawableMap = new HashMap<>();

    }

    public void setDefaults(View defaultView, int defaultDrawable) {
        this.drawableMap.put(TestState.UNKNOWN, defaultDrawable);
        this.viewMap.put(TestState.UNKNOWN, defaultView);
    }


    public TestState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(TestState currentState) {
        this.currentState = currentState;

        this.setCorrespondingDrawable();

    }

    private void setCorrespondingDrawable() {

        this.hideAll();

        TestState currentState = TestState.UNKNOWN;

        if (!this.viewMap.containsKey(this.currentState))
            currentState = this.currentState;

        if (!this.drawableMap.containsKey(this.currentState))
            currentState = this.currentState;

        View currentView = this.viewMap.get(this.currentState);
        int drawable = this.drawableMap.get(this.currentState);


        if (currentView instanceof AppCompatImageView) {
            AppCompatImageView imageView = (AppCompatImageView) currentView;
            imageView.setImageResource(drawable);
        }

        currentView.setVisibility(View.VISIBLE);


    }

    private void hideAll() {
        for (TestState s : this.viewMap.keySet()) {
            this.viewMap.get(s).setVisibility(View.INVISIBLE);
        }
    }


    public void putDrawable(int drawable, TestState state) {
        this.drawableMap.put(state, drawable);
    }

    public void putView(View view, TestState state) {
        this.viewMap.put(state, view);
    }

    public int getDrawable() {
        return this.drawableMap.get(this.currentState);
    }

    public View getView() {
        return this.viewMap.get(currentState);
    }


}
