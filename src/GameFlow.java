import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class GameFlow {
    private Player selected;
    private GameBoard gameBoard;
    private TileFactory tileFactory;
    private LevelManager levelManager;
    GameFlow() {
    }

    public void startGame(String path) {
        this.tileFactory = new TileFactory();
        int player_index = selectPlayer(); // ask for player
        tileFactory.setSelected(player_index);
        this.gameBoard = new GameBoard();
        tileFactory.setDeathCallbackInitializer((e) -> (() -> gameBoard.remove(e))); //init callbacks
        this.levelManager= new LevelManager(tileFactory, path);
        this.gameBoard.initialize(this.levelManager);
        this.selected = levelManager.getSelected();
        gameTick();
    }

    private void gameTick() {
        gameBoard.sortTiles();
        System.out.println("You have selected:");
        System.out.println(selected.getName());
        System.out.println(gameBoard);
        char c;
        do {
            System.out.println(selected.describe());
            c = validInput(); //ask for action
            Position newPosition = new Position(selected.getPosition().getX(), selected.getPosition().getY());
            switch (c) {
                case 'w' -> {
                    newPosition.moveUp();
                    playerTick(newPosition);
                }
                case 'a' -> {
                    newPosition.moveLeft();
                    playerTick(newPosition);
                }
                case 's' -> {
                    newPosition.moveDown();
                    playerTick(newPosition);
                }
                case 'd' -> {
                    newPosition.moveRight();
                    playerTick(newPosition);
                }
                case 'q' -> { // do nothing
                    selected.gameTick();
                }
                case 'e' -> { //cast special ability
                    selected.castAbility(levelManager.getEnemies());
                }
            }
                gameBoard.sortTiles();
                for (Enemy enemy : levelManager.getEnemies()){ // enemy turn
                    enemy.gameTick(selected);
                    Position newEnemyPosition = enemy.move(selected); // enemy move
                    Tile newT = tileInPosition(newEnemyPosition);
                    enemy.interact(newT);
                }
                if (levelManager.getEnemies().size() == 0) //if no enemies left -> advance level
                    gameBoard.advanceLevel();

                System.out.println(gameBoard);
        } while(!(selected.getHealthAmount() == 0 || levelManager.isWon()) ); // else game over
            if (levelManager.isWon())
                System.out.println("You WON!!!");
    }

    private Tile tileInPosition(Position position){
        int calc = position.getY() * gameBoard.boardWidth + position.getX(); //find the tile in position
        return gameBoard.tiles.get(calc);
    }

    private void playerTick(Position newPosition){
        Tile newT = tileInPosition(newPosition);
        int enemiesSize = levelManager.getEnemies().size();
        selected.interact(newT);
        gameBoard.sortTiles();
        if(levelManager.getEnemies().size() < enemiesSize) // player kill the enemy near him
            selected.interact(tileInPosition(newPosition)); // move to enemy position
        selected.gameTick();
    }

    private int selectPlayer() {
        int chosen;
        while (true){
            int iter = 1;
            for (Player player : tileFactory.listPlayers()){
                System.out.println(iter+".  " + player.describe()); // print all the players
                iter++;
            }
            Scanner scanner = new Scanner(System.in);
            if (scanner.hasNextInt()){
                chosen = scanner.nextInt();
                if (chosen >= 1 && chosen <=tileFactory.listPlayers().size() )
                    return chosen -1;
            }
        }
    }

    private char validInput(){
        List<Character> validChars = Arrays.asList('a', 's', 'd', 'w', 'e', 'q') ;  // valid keys
        while (true){
            Scanner scanner = new Scanner(System.in);
            String string = scanner.next();
            if (string.length() == 1){  // else input invalid
                char c = string.charAt(0);
                if(validChars.contains(c))
                    return c;
            }
        }
    }
}
