package com.drofff.checkers.client.enums;

import java.awt.*;

public enum BoardSide {

    RED(Color.RED, Color.BLACK), BLACK(Color.BLACK, Color.WHITE);

    private final Color pieceColor;
    private final Color outlineColor;

    public static BoardSide oppositeSide(BoardSide boardSide) {
        return boardSide == RED ? BLACK : RED;
    }

    BoardSide(Color pieceColor, Color outlineColor) {
        this.pieceColor = pieceColor;
        this.outlineColor = outlineColor;
    }

    public Color getPieceColor() {
        return pieceColor;
    }

    public Color getOutlineColor() {
        return outlineColor;
    }

}