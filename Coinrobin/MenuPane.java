package Coinrobin;


import javafx.scene.control.Button;
import javafx.scene.layout.HBox;


public class MenuPane extends HBox {
    Button start;
    Button highScores;

    public MenuPane(){
        start = new Button("New Game");
        highScores = new Button("HighScores");
        getChildren().addAll(start, highScores);
    }
}
