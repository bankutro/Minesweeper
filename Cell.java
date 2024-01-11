public class Cell {
    boolean isOpen = false;
    boolean isFlagged = false;
    int bombsNearby = 0;
    byte type = 0;
    byte x;
    byte y;

    public Cell(byte x, byte y){
        int randomNumber = (int)(Math.random() * 100);
        this.x = x;
        this.y = y;
        if (randomNumber <= 10){
            type = 1;
        }
    }
}
