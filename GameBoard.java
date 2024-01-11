import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

public class GameBoard extends JPanel {
    int cellSize = 25;
    Cell[][] cellList = new Cell[20][20];
    boolean isBoomed = false;
    Font font = new Font("serif", Font.PLAIN, 25);
    String path = GameBoard.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    String decodedPath = URLDecoder.decode(path, "UTF-8");
    Image flagImage = ImageIO.read(new File(decodedPath + "Images/flag.png"));
    Image bombImage = ImageIO.read(new File(decodedPath + "Images/bomb.png"));

    public GameBoard(int boardWidth, int boardHeight) throws IOException {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        spawnCells();
    }

    public void paint(Graphics g){
        super.paintComponent(g);
        try {
            drawCells(g);
            drawGrid(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawGrid(Graphics g){
        for (int i = 1; i <= 20; i++){
            g.setColor(Color.black);
            g.drawLine(0, (i * cellSize), 500, (i * cellSize));
            g.drawLine((i * cellSize), 0, (i * cellSize), 500);
        }
    }

    private void drawCells(Graphics g) throws IOException {
        for (Cell[] cellsRow : cellList){
            for (Cell cell : cellsRow){
                if (isBoomed && cell.type == 1) {
                    cell.isFlagged = false;
                    cell.isOpen = true;
                }
                if (cell.isFlagged){
                    g.fillRect(cell.x * cellSize, cell.y * cellSize, cellSize, cellSize);
                    g.drawImage(flagImage, cell.x * cellSize, cell.y * cellSize, this);
                }
                else if (cell.isOpen && cell.type == 0) {
                    g.setColor(Color.green);
                    g.fillRect(cell.x * cellSize, cell.y * cellSize, cellSize, cellSize);
                }
                else if (cell.isOpen && cell.type == 1)
                    g.drawImage(bombImage, cell.x * cellSize, cell.y * cellSize, this);
                else{
                    g.setColor(Color.white);
                    g.fillRect(cell.x * cellSize, cell.y * cellSize, cellSize, cellSize);
                    if (cell.bombsNearby != 0) {
                        g.setColor(Color.black);
                        g.setFont(font);
                        g.drawString(Integer.toString(cell.bombsNearby), cell.x * cellSize + 5, cell.y * cellSize + 20);
                    }
                }
            }
        }
    }

    private void spawnCells(){
        for (byte i = 0; i < 20; i++){
            for (byte j = 0; j < 20; j++){
                cellList[i][j] = new Cell(i, j);
            }
        }
    }

    public void changeCellState(int buttonPressed, int coordinateX, int coordinateY){
        int posX = coordinateX / cellSize;
        int posY = coordinateY / cellSize;
        if (buttonPressed == 1 && !cellList[posX][posY].isOpen && cellList[posX][posY].type == 0) {
            cellList[posX][posY].isOpen = true;
            checkForBomb(cellList[posX][posY]);
        }
        else if (buttonPressed == 1 && cellList[posX][posY].type == 1){
            isBoomed = true;
            repaint();
            JOptionPane.showMessageDialog(null, "You BOOMed!");
            System.exit(0);
        }
        else if (buttonPressed == 3 && !cellList[posX][posY].isOpen && !cellList[posX][posY].isFlagged)
            cellList[posX][posY].isFlagged = true;
        else if (buttonPressed == 3 && !cellList[posX][posY].isOpen && cellList[posX][posY].isFlagged)
            cellList[posX][posY].isFlagged = false;
        repaint();
    }

    private void checkForBomb(Cell cell){
        for (int i = Math.max(cell.x - 1, 0); i <= Math.min(cellList.length - 1, cell.x + 1); i++){
            for (int j = Math.max(cell.y - 1, 0); j <= Math.min(cellList.length - 1, cell.y + 1); j++){
                if (!cellList[i][j].isOpen && cellList[i][j].bombsNearby == 0)
                    countBombsNearby(cellList[i][j]);
            }
        }
    }

    private void countBombsNearby(Cell cell){
        int bombCount = 0;
        for (int i = Math.max(cell.x - 1, 0); i <= Math.min(cellList.length - 1, cell.x + 1); i++){
            for (int j = Math.max(cell.y - 1, 0); j <= Math.min(cellList.length - 1, cell.y + 1); j++){
                if (cell.x == i && cell.y == j)
                    continue;
                if (cellList[i][j].type == 1)
                    bombCount += 1;
            }
        }
        cell.bombsNearby = bombCount;
        if (cell.bombsNearby == 0 && cell.type == 0){
            cell.isOpen = true;
            checkForBomb(cell);
        }
    }

    public boolean checkIfWin(){
        boolean isWin = true;
        for (Cell[] cellsRow : cellList) {
            for (Cell cell : cellsRow) {
                if (cell.type == 1 && !cell.isFlagged || cell.type == 0 && cell.isFlagged) {
                    isWin = false;
                    return isWin;
                }
            }
        }
        JOptionPane.showMessageDialog(null, "You won!");
        System.exit(0);
        return isWin;
    }
}
