package com.drofff.checkers.client.utils;

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

}
