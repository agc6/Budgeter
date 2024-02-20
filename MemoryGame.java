package cards;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

class Tile {
    private int number;
    private boolean flipped;

    public Tile(int number) {
        this.number = number;
        this.flipped = false;
    }

    public int getNumber() {
        return number;
    }

    public boolean isFlipped() {
        return flipped;
    }

    public void flip() {
        flipped = !flipped;
    }
}

public class MemoryGame extends JFrame {
    private ArrayList<Tile> tiles;
    private ArrayList<JButton> buttons;
    private Tile selectedTile1;
    private Tile selectedTile2;
    private int pairsFound;

    public MemoryGame(int gridSize) {
        super("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(gridSize, gridSize));
        tiles = new ArrayList<>();
        buttons = new ArrayList<>();
        pairsFound = 0;

        //Create pairs of tiles
        for (int i = 1; i <= gridSize * gridSize / 2; i++) {
            tiles.add(new Tile(i));
            tiles.add(new Tile(i));
        }

        //Shuffle the tiles
        Collections.shuffle(tiles);

        //Add tiles to the game board
        for (Tile tile : tiles) {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(100, 100));
            add(button);
            buttons.add(button);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = buttons.indexOf(button);
                    Tile currentTile = tiles.get(index);

                    if (!currentTile.isFlipped() && selectedTile1 == null) {
                        selectedTile1 = currentTile;
                        currentTile.flip();
                        updateButton(button, currentTile);
                    } else if (!currentTile.isFlipped() && selectedTile2 == null) {
                        selectedTile2 = currentTile;
                        currentTile.flip();
                        updateButton(button, currentTile);
                        checkTiles();
                    }
                }
            });
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateButton(JButton button, Tile tile) {
        if (tile.isFlipped()) {
            button.setText(String.valueOf(tile.getNumber()));
        } else {
            button.setText("");
        }
    }

    private void checkTiles() {
        if (selectedTile1.getNumber() == selectedTile2.getNumber()) {
            selectedTile1 = null;
            selectedTile2 = null;
            pairsFound++;
            if (pairsFound == tiles.size() / 2) {
                JOptionPane.showMessageDialog(this, "Congratulations! You've won!");
                System.exit(0);
            }
        } else {
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedTile1.flip();
                    selectedTile2.flip();
                    updateButton(buttons.get(tiles.indexOf(selectedTile1)), selectedTile1);
                    updateButton(buttons.get(tiles.indexOf(selectedTile2)), selectedTile2);
                    selectedTile1 = null;
                    selectedTile2 = null;
                    ((Timer) e.getSource()).stop();
                    repaint();
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MemoryGame(4)); //Change the grid size as needed
    }
}
