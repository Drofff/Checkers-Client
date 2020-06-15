package com.drofff.checkers.client.utils;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.enums.BoardSide;

import static com.drofff.checkers.client.constants.BoardConstants.*;

public class BoardUtils {

    private BoardUtils() {}

    public static int getSizeOfSquare() {
        int boardSizeExcludingBorders = getBoardSizeExcludingBorders();
        return boardSizeExcludingBorders / ROWS_COUNT;
    }

    public static int getBoardSizeExcludingBorders() {
        return BOARD_SIZE - 2 * BORDER_WIDTH;
    }

    public static Piece.Position adjustPositionToBoardSide(Piece.Position position, BoardSide boardSide) {
        return boardSide == BoardSide.BLACK ? position.inverse() : position;
    }

}