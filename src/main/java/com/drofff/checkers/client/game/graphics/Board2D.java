package com.drofff.checkers.client.game.graphics;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.enums.BoardSide;

import java.awt.*;

import static com.drofff.checkers.client.constants.BoardConstants.*;
import static com.drofff.checkers.client.utils.BoardUtils.getBoardSizeExcludingBorders;
import static com.drofff.checkers.client.utils.BoardUtils.getSizeOfSquare;
import static java.awt.Color.BLACK;
import static java.awt.Color.WHITE;

public class Board2D extends Canvas {

    private static final Color BROWN = new Color(102, 51, 0);
    private static final Color SELECTED_OUTLINE_COLOR = new Color(0, 255, 255);

    private static final int PIECE_INNER_CIRCLE_OFFSET = 10;

    public Board2D() {
        setSize(BOARD_SIZE, BOARD_SIZE);
    }

    @Override
    public void paint(Graphics graphics) {
        initBoard();
    }

    private void initBoard() {
        fillBase();
        fillSquares();
    }

    private void fillBase() {
        Graphics graphics = getGraphics();
        graphics.setColor(BLACK);
        graphics.fillRect(0, 0, BOARD_SIZE, BOARD_SIZE);
        graphics.setColor(WHITE);
        int boardSizeExcludingBorders = getBoardSizeExcludingBorders();
        graphics.fillRect(BORDER_WIDTH, BORDER_WIDTH, boardSizeExcludingBorders, boardSizeExcludingBorders);
    }

    private void fillSquares() {
        Graphics graphics = getGraphics();
        graphics.setColor(BROWN);
        for(int rowIndex = 0; rowIndex < ROWS_COUNT; rowIndex++) {
            int columnIndex = rowIndex % 2;
            for( ; columnIndex < ROWS_COUNT; columnIndex += 2) {
                fillSquareOfPosition(rowIndex, columnIndex, graphics);
            }
        }
    }

    public void displayPieceAtBoardSide(Piece piece, BoardSide boardSide) {
        Piece.Position piecePosition = piece.getPosition();
        displayPieceAtPositionOfBoardSide(piecePosition, boardSide);
    }

    public void movePieceAtBoardSide(Piece.Position from, Piece.Position to, BoardSide boardSide) {
        clearSquareAtPosition(from);
        displayPieceAtPositionOfBoardSide(to, boardSide);
    }

    private void clearSquareAtPosition(Piece.Position position) {
        Graphics graphics = getGraphics();
        graphics.setColor(BROWN);
        fillSquareOfPosition(position.getRow(), position.getColumn(), graphics);
    }

    private void fillSquareOfPosition(int row, int column, Graphics graphics) {
        int[] square = getSquareOfPosition(row, column);
        graphics.fillRect(square[0], square[1], square[2], square[3]);
    }

    public void selectSquareAtBoardSide(Piece.Position square, BoardSide boardSide) {
        displayPieceWithOutlineColorAtPositionOfBoardSide(SELECTED_OUTLINE_COLOR, square, boardSide);
    }

    public void unselectSquareAtBoardSide(Piece.Position square, BoardSide boardSide) {
        displayPieceAtPositionOfBoardSide(square, boardSide);
    }

    private void displayPieceAtPositionOfBoardSide(Piece.Position piecePosition, BoardSide boardSide) {
        displayPieceWithOutlineColorAtPositionOfBoardSide(boardSide.getOutlineColor(), piecePosition, boardSide);
    }

    private void displayPieceWithOutlineColorAtPositionOfBoardSide(Color outlineColor, Piece.Position piecePosition,
                                                                   BoardSide boardSide) {
        int[] square = getSquareOfPosition(piecePosition.getRow(), piecePosition.getColumn());
        fillPieceOfBoardSideAtSquare(boardSide, square);
        drawPieceOutlineOfColorAtSquare(outlineColor, square);
    }

    private int[] getSquareOfPosition(int row, int column) {
        int squareX = calculateSquareAxisAtPosition(column);
        int squareY = calculateSquareAxisAtPosition(row);
        int squareSize = getSizeOfSquare();
        return new int[] { squareX, squareY, squareSize, squareSize };
    }

    private int calculateSquareAxisAtPosition(int position) {
        int squareSize = getSizeOfSquare();
        return position * squareSize + BORDER_WIDTH;
    }

    private void fillPieceOfBoardSideAtSquare(BoardSide boardSide, int[] pieceSquare) {
        Graphics graphics = getGraphics();
        graphics.setColor(boardSide.getPieceColor());
        graphics.fillOval(pieceSquare[0], pieceSquare[1], pieceSquare[2], pieceSquare[3]);
    }

    private void drawPieceOutlineOfColorAtSquare(Color outlineColor, int[] pieceSquare) {
        Graphics graphics = getGraphics();
        graphics.setColor(outlineColor);
        graphics.drawOval(pieceSquare[0], pieceSquare[1], pieceSquare[2], pieceSquare[3]);
        int innerCircleX = pieceSquare[0] + PIECE_INNER_CIRCLE_OFFSET;
        int innerCircleY = pieceSquare[1] + PIECE_INNER_CIRCLE_OFFSET;
        int innerCircleSize = pieceSquare[2] - 2 * PIECE_INNER_CIRCLE_OFFSET;
        graphics.drawOval(innerCircleX, innerCircleY, innerCircleSize, innerCircleSize);
    }

}