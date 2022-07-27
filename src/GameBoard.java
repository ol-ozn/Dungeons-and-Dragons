import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameBoard {
    public List<Tile> tiles;
    public int boardWidth;
    public LevelManager levelManager;

//------------------TODO ------------------------------ why do we need game Board???___-----
    public GameBoard() {

    }

    public void initialize(LevelManager levelManager){
        this.levelManager = levelManager;
        this.tiles = levelManager.loadLevel();
        boardWidth = levelManager.getCurrLevelWidth();
    }

    public Tile get(int x, int y) throws Exception {
        Position p = new Position(x, y);
        for(Tile t : tiles){
            if (t.getPosition().equals(p)){
                return t;
            }
        }
        throw new Exception("Tile not found");// Throw an exception if no such tile.
    }

    public void sortTiles(){
        tiles = tiles.stream().sorted().collect(Collectors.toList());
    }

    public void remove(Enemy e) {
        Position p = e.getPosition();
        int t = tiles.indexOf(e);
        tiles.remove(e);
        levelManager.getEnemies().remove(e);
        Empty newEmpty = new Empty();
        newEmpty.initialize(p);
        tiles.add(newEmpty);
        int w = tiles.indexOf(newEmpty);
//        if(levelManager.selected.getPosition().distance(newEmpty.getPosition()) == 1)
//            levelManager.selected.interact(newEmpty);
    }


    @Override
    public String toString() {
        tiles = tiles.stream().sorted().collect(Collectors.toList());
        // TODO: Implement me
        String output = "";
        int counter = 0;
        for(Tile t : tiles) {

            if(counter < boardWidth) {
                output += t.toString();
                counter++;
            } else {
                output += "\n" + t.toString();
                counter = 1;
            }
        }
        return output;
    }

    public void advanceLevel(){
        levelManager.advanceLevel();
        if (!levelManager.isWon()){
            this.tiles = levelManager.loadLevel();
            this.boardWidth = levelManager.getCurrLevelWidth();
        }
    }

//    public void switchPosition(Tile tile1, Tile tile2, int shift) {
//        int t1calc = calcPosition(tile1.position.x,tile1.position.y);
//        tiles.remove(t1calc);
//        int t2calc = calcPosition(tile2.position.x,tile2.position.y);
//        tiles.remove(t2calc + shift); // -1 is specific to w,a
//        Position tmpPosition = tile1.position;
//        tile1.position = tile2.position;
//        tile2.position = tmpPosition;
//        tiles.add(tile1);
//        tiles.add(tile2);
//    }
//
//    public void replacePosition(Tile tile) {
//        tiles.remove(boardWidth * tile.position.y + tile.position.x);
//        tiles.add(tile);
//    }

    public int calcPosition(int x, int y) {
        return y * boardWidth + x;
    }

//    public Position calcPosition(int pos) {
//        return new Position(pos % boardWidth, pos - (pos % boardWidth));
////        return y * boardWidth + x + 1;
//    }
}