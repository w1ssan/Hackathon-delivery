package Coinrobin;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AnimatedAsset extends Group {

    private final int frames;
    private int index = 0;
    private final int size;
    private final Image[] images;
    private Image current;
    private final ImageView iv;

    public AnimatedAsset(double x, double y, int size,Image... args){
        images = new Image[args.length];
        this.size = size;
        for (int i = 0; i < args.length; i++) {
            images[i] = args[i];
        }
        frames = args.length;
        iv = new ImageView();
        getChildren().add(iv);
        iv.setFitWidth(size);
        iv.setFitHeight(size);
        setTranslateX(x);
        setTranslateY(y);
    }

    public void runAnimation(){
        if (index == frames){
            index = 0;
        }
        current = images[index];
        iv.setImage(current);
        index++;

    }

    public void moveAnimation(Point2D destination){
        if (getTranslateX() < destination.getX()){
            setTranslateX(getTranslateX() + (double)size / 5);
        }
        if (getTranslateY() < destination.getY()){
            setTranslateY(getTranslateX() + (double)size / 5);
        }
    }
}