package PDBExplorer.window;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

public class AnimationHandler {

    public static Timeline rotationAnimation(Group figure, Rotate rotate){

        KeyFrame key = new KeyFrame(Duration.millis(150), actionEvent -> {
            Transform oldTransform = figure.getTransforms().get(0);
            Transform newTransform = rotate.createConcatenation(oldTransform);

            figure.getTransforms().set(0, newTransform);

        });

        Timeline timeLine = new Timeline(key);
        timeLine.setCycleCount(Timeline.INDEFINITE);
        return timeLine;
    }
}
