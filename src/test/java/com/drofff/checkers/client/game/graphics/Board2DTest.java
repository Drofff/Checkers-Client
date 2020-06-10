package com.drofff.checkers.client.game.graphics;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.enums.BoardSide;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.UUID;

public class Board2DTest {

    @Test
    @SuppressWarnings("all")
    public void initBoard2DTest() {
        Board2D board2D = new Board2D();
        JFrame testFrame = new JFrame("Board 2D Test");
        testFrame.add(board2D);
        testFrame.pack();
        testFrame.setVisible(true);
        testFrame.setResizable(false);

        try {
            Thread.sleep(100);
        } catch(InterruptedException e) {

        }

        Piece testPiece0 = getTestPieceWithPosition(2, 4);
        board2D.displayPieceAtBoardSide(testPiece0, BoardSide.BLACK);
        Piece testPiece1 = getTestPieceWithPosition(1, 1);
        board2D.displayPieceAtBoardSide(testPiece1, BoardSide.RED);
    }

    private Piece getTestPieceWithPosition(int row, int column) {
        Piece piece = new Piece();
        piece.setKing(false);
        piece.setPosition(positionOf(row, column));
        piece.setOwnerId(UUID.randomUUID().toString());
        return piece;
    }

    private Piece.Position positionOf(int row, int column) {
        Piece.Position position = new Piece.Position();
        position.setRow(row);
        position.setColumn(column);
        return position;
    }

}