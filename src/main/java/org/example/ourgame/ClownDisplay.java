package org.example.ourgame;

import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;

// Klass ClownDisplay haldab klouni kuvamist ja animatsiooni
public class ClownDisplay {
    // Väljad ImageView (pildi kuvamiseks) ja TranslateTransition (animatsiooni jaoks)
    private ImageView view;
    private TranslateTransition transition;

    // Konstruktor, mis võtab ImageView ja TranslateTransition objektid ning määrab need vastavatele väljadele
    public ClownDisplay(ImageView view, TranslateTransition transition) {
        this.view = view;
        this.transition = transition;
    }

    // Meetod, mis tagastab ImageView objekti
    public ImageView getView() {
        return view;
    }

    // Meetod, mis tagastab TranslateTransition objekti
    public TranslateTransition getTransition() {
        return transition;
    }

    // Meetod, mis alustab animatsiooni
    public void startAnimation() {
        transition.play();
    }
}