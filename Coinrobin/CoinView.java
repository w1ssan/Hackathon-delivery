package Coinrobin;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class CoinView extends Pane {

    //Game Variables
    protected static int score = 0;
    protected Text scoretxt;
    protected AnimatedAsset player;
    protected Point2D playerVelocity = new Point2D(0, 0);
    protected boolean canJump = true, complete = false, running = true , dead = false;

    //lists for collision logic
    protected final HashMap<KeyCode, Boolean> keys = new HashMap<>();
    protected final ArrayList<Node> platforms = new ArrayList<>();
    protected final ArrayList<Node> coins = new ArrayList<>();
    protected final ArrayList<Node> animations = new ArrayList<>();
    protected final ArrayList<Node> powers = new ArrayList<>();
    protected final Pane gameRoot = new Pane();
    protected final Pane uiRoot = new Pane();

    //levelVariables
    protected int levelWidth;
    protected final int TILE_SIZE = 60;

    protected int teller = 0;
    protected int speedBuffTime = 20;
    protected boolean speedBuff = false;
    protected int playerspeed = 5;


    public CoinView(int level){
        startLevel(level);
    }


    //update methods
    private void updateAnimations() {
        player.runAnimation();
        for (Node ani : animations){
            if ( ani instanceof AnimatedAsset){
                ((AnimatedAsset) ani).runAnimation();
            }
        }
        if (speedBuff){
            if (speedBuffTime > teller){
                playerspeed = 10;
                teller++;
                System.out.println(teller);
            }else{
                speedBuff = false;
                playerspeed = 5;
            }
        }


    }

    private void update() {
        if (isPressed(KeyCode.W) && player.getTranslateY() >= 5) {
            jumpPlayer();
        }

        if (isPressed(KeyCode.A) && player.getTranslateX() >= 5) {
            movePlayerX(-playerspeed);
        }

        if (isPressed(KeyCode.D) && player.getTranslateX() + 40 <= levelWidth - 5) {
            movePlayerX(playerspeed);
        }

        if (playerVelocity.getY() < 10) {
            playerVelocity = playerVelocity.add(0, 1);
        }
        movePlayerY((int)playerVelocity.getY());
        for (Node coin : coins) {
            if (player.getBoundsInParent().intersects(coin.getBoundsInParent())) {
                coin.getProperties().put("alive", false);
                score += 100;
                scoretxt.setText("Score: " + score);
            }
        }
        for (Node power : powers) {
            if (player.getBoundsInParent().intersects(power.getBoundsInParent())) {
                power.getProperties().put("alive", false);
                speedBuff = true;
                score += 300;
                scoretxt.setText("Score: " + score);
            }
        }

        for (Node asset : animations) {
            if (player.getBoundsInParent().intersects(asset.getBoundsInParent())) {
                asset.getProperties().put("alive", false);
                score += 300;
                scoretxt.setText("Score: " + score);
            }
        }

        for (Iterator<Node> it = animations.iterator(); it.hasNext(); ) {
            Node contraption = it.next();
            if (!(Boolean)contraption.getProperties().get("alive")) {
                it.remove();
                gameRoot.getChildren().remove(contraption);
            }
        }

        for (Iterator<Node> it = coins.iterator(); it.hasNext(); ) {
            Node coin = it.next();
            if (!(Boolean)coin.getProperties().get("alive")) {
                it.remove();
                gameRoot.getChildren().remove(coin);
            }
        }
        for (Iterator<Node> it = powers.iterator(); it.hasNext(); ) {
            Node power = it.next();
            if (!(Boolean)power.getProperties().get("alive")) {
                it.remove();
                gameRoot.getChildren().remove(power);
            }
        }

        if (player.getTranslateY() > 700){
            dead = true;
        }
        if (player.getTranslateX() > levelWidth - 100){
            Text win = new Text("YOU WIN!");
            win.setX(500);
            win.setY(450);
            win.setFont(Font.font(50));
            //clear();
            getChildren().add(win);
            complete = true;

        }
    }

    //level creation methods
    private void startLevel(int level){
        try {
            initContent(level);
            setOnKeyPressed(event -> keys.put(event.getCode(), true));
            setOnKeyReleased(event -> keys.put(event.getCode(), false));
            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (running) {
                        update();
                    }
                }
            };
            timer.start();
            Timeline animations = new Timeline();
            animations.getKeyFrames().add(new KeyFrame(Duration.millis(200), e-> updateAnimations()));
            animations.setCycleCount(Timeline.INDEFINITE);
            animations.play();
            requestFocus();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initContent(int level) throws FileNotFoundException {
        Rectangle camera = new Rectangle(1280, 720);
        camera.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("assets\\graveyard.png")), 0, 0, 1, 1, true));
        String[] map;

        switch (level){
            case 1:
                map =  LevelData.LEVEL1;
                break;
            case 2:
                map =  LevelData.LEVEL2;
                break;
            case 3:
                map =  LevelData.LEVEL3;
                break;
            case 4:
                map =  LevelData.LEVEL4;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + level);
        }
        levelWidth = map[0].length() * 60;

        for (int i = 0; i < map.length; i++) {
            String line = map[i];
            for (int j = 0; j < line.length(); j++) {
                switch (line.charAt(j)) {
                    case '0':
                        break;
                    case '1':
                        Node platform = createEntity(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE, new Image(getClass().getResourceAsStream("assets\\tile.png")));
                        platforms.add(platform);
                        break;
                    case '2':
                        Node contraption = new AnimatedAsset(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE,
                                new Image(getClass().getResourceAsStream("assets\\coin1.png")),
                                new Image(getClass().getResourceAsStream("assets\\coin2.png")),
                                new Image(getClass().getResourceAsStream("assets\\coin3.png")),
                                new Image(getClass().getResourceAsStream("assets\\coin4.png")));
                        contraption.getProperties().put("alive", true);
                        animations.add(contraption);
                        gameRoot.getChildren().add(contraption);
                        break;
                    case '3':
                        Node apple = createEntity(j*TILE_SIZE, i*TILE_SIZE, TILE_SIZE, TILE_SIZE, new Image(getClass().getResourceAsStream("assets\\apple.png")));
                        powers.add(apple);
                        break;
                }
            }
        }
        scoretxt = new Text("Score: " + score);
        scoretxt.setFont(Font.font(30));
        scoretxt.setFill(Color.GOLD);
        scoretxt.setX(20);
        scoretxt.setY(20);

        player = new AnimatedAsset(0, 600, 40,
                new Image(new FileInputStream("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\slime1.png")),
                new Image(new FileInputStream("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\slime2.png")),
                new Image(new FileInputStream("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\slime3.png")),
                new Image(new FileInputStream("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\slime4.png")),
                new Image(new FileInputStream("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\slime5.png")),
                new Image(new FileInputStream("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\slime6.png")),
                new Image(new FileInputStream("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\slime7.png")),
                new Image(new FileInputStream("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\slime8.png")),
                new Image(new FileInputStream("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\slime9.png")),
                new Image(new FileInputStream("D:\\IdeaProjects\\letsfx\\src\\Coinrobin\\assets\\slime10.png")));

                //new Image(getClass().getResourceAsStream("assets\\Blinky.png")),
                //new Image(getClass().getResourceAsStream("assets\\blinky2.png")));
        gameRoot.getChildren().add(player);


        player.translateXProperty().addListener((obs, old, newValue) -> {
            int offset = newValue.intValue();

            if (offset > 640 && offset < levelWidth - 640) {
                gameRoot.setLayoutX(-(offset - 640));
            }
        });

        getChildren().addAll(camera, gameRoot, scoretxt, uiRoot);
    }

    private Node createEntity(int x, int y, int w, int h, Image image) {
        ImageView entity = new ImageView(image);
        entity.setTranslateX(x);
        entity.setTranslateY(y);
        entity.setFitWidth(w);
        entity.setFitHeight(h);
        entity.getProperties().put("alive", true);

        gameRoot.getChildren().add(entity);
        return entity;
    }

    public void clear(){
        platforms.clear();
        keys.clear();
        coins.clear();
        gameRoot.getChildren().clear();
        uiRoot.getChildren().clear();
        getChildren().clear();
        startLevel(2);
    }


    //movement related methods
    private boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }

    private void movePlayerX(int value) {
        boolean movingRight = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingRight) {
                        if (player.getTranslateX() + 40 == platform.getTranslateX()) {
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateX() == platform.getTranslateX() + 60) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateX(player.getTranslateX() + (movingRight ? 1 : -1));
        }
    }

    private void movePlayerY(int value) {
        boolean movingDown = value > 0;

        for (int i = 0; i < Math.abs(value); i++) {
            for (Node platform : platforms) {
                if (player.getBoundsInParent().intersects(platform.getBoundsInParent())) {
                    if (movingDown) {
                        if (player.getTranslateY() + 40 == platform.getTranslateY()) {
                            player.setTranslateY(player.getTranslateY() - 1);
                            canJump = true;
                            return;
                        }
                    }
                    else {
                        if (player.getTranslateY() == platform.getTranslateY() + 60) {
                            return;
                        }
                    }
                }
            }
            player.setTranslateY(player.getTranslateY() + (movingDown ? 1 : -1));
        }
    }

    private void jumpPlayer() {
        if (canJump) {
            playerVelocity = playerVelocity.add(0, -30);
            canJump = false;
        }
    }




}
