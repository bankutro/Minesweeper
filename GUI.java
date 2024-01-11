import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class GUI extends JFrame implements MouseListener {
    int boardHeight = 500;
    int boardWidth = boardHeight;
    GameBoard panel = new GameBoard(boardWidth, boardHeight);
    public GUI() throws IOException {
        setSize(boardWidth, boardHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        validate();
        addMouseListener(this);
        add(panel);
        pack();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int coordinateX = e.getX() - 8;
        int coordinateY = e.getY() - 31;
        panel.changeCellState(e.getButton(), coordinateX, coordinateY);
        panel.checkIfWin();
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
