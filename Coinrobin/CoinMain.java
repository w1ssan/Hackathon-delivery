package Coinrobin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;

import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;


public class CoinMain extends Application {
    protected BorderPane window;
    protected MenuPane menu = new MenuPane();
    protected CoinView game;
    protected ArrayList<Integer> highScores;
    protected boolean done = false;
    protected int highScore = 0;
    protected int level = 1;
    @Override
    public void start(Stage stage) throws Exception {
        highScores = new ArrayList<>();
        window = new BorderPane();
        window.setPrefHeight(740);
        window.setPrefWidth(1280);
        menu = new MenuPane();
        window.setTop(menu);
        window.setCenter(new HighScorePane());
        menu.start.setOnAction(e->{
            done = false;
            game = new CoinView(1);
            CoinView.score = 0;
            game.scoretxt.setText("Score: " + game.score);
            window.setCenter(game);
            game.requestFocus();
        });
        menu.highScores.setOnAction(e-> window.setCenter(new HighScorePane()));
        Timeline time = new Timeline();
        time.getKeyFrames().add(new KeyFrame(Duration.millis(1000), e-> checkStatus()));
        time.setCycleCount(Timeline.INDEFINITE);
        time.play();
        stage.setScene(new Scene(window));
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
    private void checkStatus(){
        if (game != null){
            if (game.dead){
                if (!done){
                    highScore = CoinView.score;
                    highScores.add(highScore);
                    game.dead = false;
                    window.setCenter(new GameEndPane(false, highScore));
                    done = true;
                }

            }else if (game.complete){
                level++;
                if (level > 4){
                    if (!done){
                        highScore = CoinView.score;
                        highScores.add(highScore);
                        game.dead = false;
                        window.setCenter(new GameEndPane(true, highScore));
                        done = true;
                    }

                }else{
                    game.complete = false;
                    game = new CoinView(level);
                    window.setCenter(game);
                    game.requestFocus();
                }
            }
        }
    }

}
