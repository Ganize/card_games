package com.game.flashcard;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Optional;
import java.util.Random;

public class FlashCardGameApp extends Application {

    private int score = 0;
    private boolean[][] shownPairs = new boolean[13][13];
    private Random rand = new Random();
    private int correctAnswer = 0;
    private Label pairsUsedLabel;
    private int timeRemaining = 60;
    private Label timerLabel = new Label();
    Button submitButton = new Button();


    @Override
    public void start(Stage primaryStage) {
        Label questionLabel = new Label("Question will appear here");
        TextField answerField = new TextField();

        Label scoreLabel = new Label("Score: " + score);
        pairsUsedLabel = new Label("Pairs used: 0");
        Label messageLabel = new Label();

        // Initialize the submit button and set it to disabled initially
        submitButton = new Button("Submit");
        submitButton.setDisable(true);

        // Add a listener to the text property of the answer field
        answerField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Enable the submit button only if the answer field is not empty
            submitButton.setDisable(newValue.trim().isEmpty());
        });

        submitButton.setOnAction(event -> {
            boolean isValidInput = true;

            try {
                double answer = Double.parseDouble(answerField.getText());

                if (answer == correctAnswer) {
                    messageLabel.setText("Correct!");
                    score++;
                } else {
                    messageLabel.setText("Incorrect. The correct answer was " + correctAnswer);
                    score--;
                }
            } catch (NumberFormatException e) {
                messageLabel.setText("Please enter a valid number.");
                isValidInput = false; // Set flag to false as input is not valid

            } catch (ArithmeticException e) {
                messageLabel.setText("Cannot divide by zero. Moving to next question.");
            }

            scoreLabel.setText("Score: " + score);
            answerField.clear();

            if (isValidInput) {
                updatePairsUsedAndGenerateNextQuestion(questionLabel);
            }

        });

        timerLabel = new Label("Time left: 60");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeRemaining--;
            timerLabel.setText("Time left: " + timeRemaining);
            if (timeRemaining <= 0) {
                submitButton.setDisable(true); // Disable the button when time is up
                timerLabel.setText("Time's up!");
            }
        }));
        timeline.setOnFinished(e -> Platform.runLater(this::showScorePopup));
        timeline.setCycleCount(60); // Set the cycle count to 60 for a 60-second countdown
        timeline.play(); // Start the countdown

        VBox layout = new VBox(10);
        layout.getChildren().addAll(questionLabel, answerField, submitButton, scoreLabel, messageLabel, pairsUsedLabel, timerLabel);

        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setTitle("Flash Card Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        updatePairsUsedAndGenerateNextQuestion(questionLabel);
    }

    private void updatePairsUsedAndGenerateNextQuestion(Label questionLabel) {
        int totalPairsUsed = countUsedPairs(shownPairs);
        pairsUsedLabel.setText("Total unique pairs used: " + totalPairsUsed);
        generateQuestion(questionLabel);
    }

    private void generateQuestion(Label questionLabel) {
        boolean validQuestionGenerated;
        int num1;
        int num2;
        do {
            validQuestionGenerated = true;
            num1 = rand.nextInt(13);
            num2 = rand.nextInt(13);
            int operation = rand.nextInt(4); // 0: addition, 1: subtraction, 2: multiplication, 3: division

            try {
                switch (operation) {
                    case 0: // Addition
                        correctAnswer = num1 + num2;
                        questionLabel.setText("What is " + num1 + " + " + num2 + "?");
                        break;
                    case 1: // Subtraction
                        correctAnswer = num1 - num2;
                        questionLabel.setText("What is " + num1 + " - " + num2 + "?");
                        break;
                    case 2: // Multiplication
                        correctAnswer = num1 * num2;
                        questionLabel.setText("What is " + num1 + " * " + num2 + "?");
                        break;
                    case 3: // Division
                        if (num2 == 0) {
                            throw new ArithmeticException("Division by zero");
                        }
                        correctAnswer = num1 / num2;
                        questionLabel.setText("What is " + num1 + " / " + num2 + "?");
                        break;
                }
            } catch (ArithmeticException e) {
                validQuestionGenerated = false; // Mark question as invalid and generate a new one
            }
        } while (!validQuestionGenerated);


        // Update the shownPairs array
        shownPairs[num1][num2] = true;
        // Update pairs used count
        int totalPairsUsed = countUsedPairs(shownPairs);
        pairsUsedLabel.setText("Total unique pairs used: " + totalPairsUsed);
    }

    private void showScorePopup() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Your Score");
        alert.setContentText("Total score: " + score);

        ButtonType exitButton = new ButtonType("Exit");
        alert.getButtonTypes().setAll(exitButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == exitButton) {
            Platform.exit();
        }
}

    private static int countUsedPairs(boolean[][] shownPairs) {
        int count = 0;
        for (boolean[] shownPair : shownPairs) {
            for (boolean b : shownPair) {
                if (b) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
