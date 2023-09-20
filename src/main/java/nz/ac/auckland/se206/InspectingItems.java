package nz.ac.auckland.se206;

import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class InspectingItems {
    public static void applyZoomInEffect(Node node, Pane inspectingPane) {

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), node);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(2.0); // Customize the scaling factor
        scaleTransition.setToY(2.0); // Customize the scaling factor

        // Calculate the translation needed to center the object

        // Create a TranslateTransition for centering
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(200), node);
        translateTransition.setToX(450);
        translateTransition.setToY(-260);

        // Combine both animations into a ParallelTransition
        ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, translateTransition);
        parallelTransition.play();

        parallelTransition.setOnFinished(event -> {
            inspectingPane.setVisible(true);
        });

    }
}
