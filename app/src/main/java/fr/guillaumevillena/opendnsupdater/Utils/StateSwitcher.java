package fr.guillaumevillena.opendnsupdater.Utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;

import fr.guillaumevillena.opendnsupdater.TestState;

/**
 * Created by guill on 25/06/2018.
 */

public class StateSwitcher {

    private TestState currentState;

    private View defaultView;
    private Drawable defaultDrawable;


    private HashMap<TestState, Drawable> drawableMap;
    private HashMap<TestState, View> viewMap;

    public StateSwitcher() {
        this.currentState = TestState.UNKNOWN;
        this.viewMap = new HashMap<>();
        this.drawableMap = new HashMap<>();

    }

    public void setDefaults(View defaultView, Drawable defaultDrawable) {
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
        Drawable drawable = this.drawableMap.get(this.currentState);


        if ( currentView instanceof ImageView){
            ImageView imageView = (ImageView)currentView;
            imageView.setImageDrawable(drawable);
        }

        currentView.setVisibility(View.VISIBLE);


    }

    private void hideAll() {
        for (TestState s : this.viewMap.keySet()){
            this.viewMap.get(s).setVisibility(View.INVISIBLE);
        }
    }


    public void putDrawable(Drawable drawable, TestState state) {
        this.drawableMap.put(state, drawable);
    }

    public void putView(View view, TestState state) {
        this.viewMap.put(state, view);
    }

    public Drawable getDrawable(){
        return this.drawableMap.get(this.currentState);
    }

    public View getView(){
        return this.viewMap.get(currentState);
    }


}
