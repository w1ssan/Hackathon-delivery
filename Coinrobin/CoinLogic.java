package Coinrobin;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class CoinLogic {
    Tile[][] map;
    File levelFile;
    int rows = 0;
    int columns = 0;

    public CoinLogic(int level){
        levelPicker(level);
    }

    public void levelPicker(int level){
        switch (level){
            case 1:
                levelFile = new File("D:\\IdeaProjects\\letsfx\\src\\sideScroller\\scrollmap1.txt");
                break;
            case 2:
                levelFile = new File("D:\\IdeaProjects\\letsfx\\src\\sideScroller\\scrollmap1.txt");
                break;
        }
        makeLogicMap(levelFile);
    }

    public void getMapSize(File level){
        try {
            Scanner mapReader = new Scanner(level);
            while(mapReader.hasNextLine()){
                columns = mapReader.nextLine().toCharArray().length;
                rows++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(rows + ", " + columns);
    }
    public void makeLogicMap(File level){
        getMapSize(level);
        map = new Tile[rows][columns];
        try{
            Scanner mapReader = new Scanner(level);
            int row = 0;
            while (mapReader.hasNextLine()){
                char[] line = mapReader.nextLine().toCharArray();
                for (int i= 0; i < line.length; i++){
                    switch (line[i]){
                        case '1':
                            map[row][i] = new Tile(line[i],1, false);
                            break;
                        case '0':
                            map[row][i] = new Tile(line[i],0, false);
                        break;
                }
            }
            row++;
        }
        System.out.println(Arrays.deepToString(map));
        mapReader.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    }
    static class Tile{
        char type;
        int texture;
        boolean hasCoin;
        private Tile(char type, int texture, boolean hasCoin){
            this.type = type;
            this.texture = texture;
            this.hasCoin = hasCoin;
        }

        @Override
        public String toString() {
            return "Tile{" +
                    "type=" + type +
                    ", hasCoin=" + hasCoin +
                    '}';
        }
    }

}
