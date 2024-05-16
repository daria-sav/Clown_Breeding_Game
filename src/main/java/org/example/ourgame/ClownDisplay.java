package org.example.ourgame;

import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;

public class ClownDisplay {
    private ImageView view;
    private TranslateTransition transition;

    public ClownDisplay(ImageView view, TranslateTransition transition) {
        this.view = view;
        this.transition = transition;
    }

    public ImageView getView() {
        return view;
    }

    public TranslateTransition getTransition() {
        return transition;
    }

    public void startAnimation() {
        transition.play();
    }
}

