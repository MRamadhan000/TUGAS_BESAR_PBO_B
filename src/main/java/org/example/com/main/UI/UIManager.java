package org.example.com.main.UI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class UIManager{
    private static Scene previousLayout;
    private static int width = 800;
    private static int height = 600;
    private static int buttonWidth = 230;
    private static int buttonHeight = 40;
    public static int buttonSeondaryWidth = 120;
    public static int buttonSecondaryHeight = 27;
    public static int fieldInputWidth = 300;
    public static int fieldInputHeight = 30;
    public static String primaryColour = "-fx-background-color: #d8f3dc;";
//    public static Font fontButton = Font.font("Sans-serif", FontWeight.BOLD, 14);
//    public static String stylePrimary = "-fx-background-color:  #1b4332;" +
//            "-fx-border-radius: 12px; " +     // Radius
//            "-fx-text-fill: white;";      // Warna teks
    public static Font fontButton = Font.font("Sans-serif", FontWeight.BOLD, 14);
    public static Font fontSecondary= Font.font("Sans-serif", FontWeight.BOLD,11);
    public static String stylePrimary = "-fx-background-color: #1b4332; " +
            "-fx-border-radius: 12px; " +
            "-fx-text-fill: white; " +
            "-fx-font-family: '" + fontButton.getFamily() + "'; " +  // Font family
            "-fx-font-size: " + fontButton.getSize() + "px; " +      // Font size
            "-fx-font-weight: " + (fontSecondary.getStyle().contains("Bold") ? "bold" : "normal");  // Font weight

    public static String styleSecondary = "-fx-background-color: #1b4332; " +
            "-fx-border-radius: 6px; " +
            "-fx-text-fill: white; " +
            "-fx-font-family: '" + fontSecondary.getFamily() + "'; " +  // Font family
            "-fx-font-size: " + fontSecondary.getSize() + "px; " +      // Font size
            "-fx-font-weight: " + (fontButton.getStyle().contains("Bold") ? "bold" : "normal");  // Font weight

    public static void setPreviousLayout(Scene scene) {
        previousLayout = scene;
    }
    public static Scene getPreviousLayout() {
        return previousLayout;
    }

    public static void showError(Text actionTarget, String message) {
        actionTarget.setFill(Color.FIREBRICK);
        actionTarget.setText(message);

        // Create a Timeline to clear the message after 5 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), ae -> actionTarget.setText("")));
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }
    public static void showSuccess(Text actionTarget, String message) {
        actionTarget.setFill(Color.GREEN);
        actionTarget.setText(message);

        // Create a Timeline to clear the message after 5 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), ae -> actionTarget.setText("")));
        timeline.setCycleCount(1); // Run only once
        timeline.play();
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static int getButtonWidth() {
        return buttonWidth;
    }

    public static int getButtonHeight() {
        return buttonHeight;
    }

}
