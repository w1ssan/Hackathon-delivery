package Coinrobin;

import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class HighScorePane extends Pane {

    public HighScorePane(){
        int antall = 1;
        setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream("assets\\highscoreBackground.jpg")), null, null, null,null)));
        VBox scoreWindow = new VBox();
        Text header = new Text("HighScores:");
        header.setTranslateX(520);
        header.setTranslateY(250);
        header.setFont(Font.font(30));
        header.setFill(Color.ORANGE);
        scoreWindow.setPadding(new Insets(10,10,10,10));
        ArrayList<Integer> scorelist = getScores();
        Collections.sort(scorelist, Collections.reverseOrder());
        for (Integer score : scorelist){
            if (antall < 11){
                Text text = new Text("HighScore nr " + antall + ": " + score);
                text.setFont(Font.font(20));
                text.setFill(Color.ORANGE);
                scoreWindow.getChildren().add(text);
                antall++;
            }
        }
        scoreWindow.setTranslateX(500);
        scoreWindow.setTranslateY(270);
        getChildren().addAll(header, scoreWindow);
    }

    public ArrayList<Integer> getScores(){
        ArrayList<Integer> scores = new ArrayList<>();
        try {
            Scanner scorereader = new Scanner(new FileInputStream(new File("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\HighScores.txt")));
            while(scorereader.hasNextLine()){
                scores.add(Integer.parseInt(scorereader.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return scores;
    }
}
