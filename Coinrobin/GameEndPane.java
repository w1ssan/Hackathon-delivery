package Coinrobin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;

public class GameEndPane extends Pane {

    boolean recorded = false;

    public GameEndPane(boolean win , int Score){
        setPadding(new Insets(500, 500, 500, 500));
        VBox scoreBox = new VBox();
        scoreBox.setAlignment(Pos.CENTER);
        Text t1 = new Text("Your score is : " + Score);
        TextField nametf = new TextField();
        t1.setFont(Font.font(20));
        Button recbtn = new Button("Record your Score!");
        recbtn.setOnAction(e-> {
            if (!recorded){
                doWrite("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\HighScores.txt", "" + Score);
                recorded = true;
            }
        });
        scoreBox.getChildren().addAll(t1, nametf, recbtn);
        Label gameOver = new Label();
        if (win){
            setBackground(new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, null, null)));
            gameOver.setText(" You win !");
        }else{
            setBackground(new Background(new BackgroundFill(Color.MEDIUMVIOLETRED, null, null)));
            gameOver.setText("Game Over!");
        }
        gameOver.setFont(Font.font(70));
        gameOver.setTranslateX(450);
        gameOver.setTranslateY(300);
        scoreBox.setTranslateX(550);
        scoreBox.setTranslateY(100);
        getChildren().addAll(gameOver, scoreBox);
    }
    public static void doWrite(String filePath,String contentToBeAppended){

        try(
                FileWriter fw = new FileWriter(filePath, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter out = new PrintWriter(bw)
        )
        {
            out.println(contentToBeAppended);
        }
        catch( IOException e ){
            // File writing/opening failed at some stage.
        }
    }
}
