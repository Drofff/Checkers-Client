package com.drofff.checkers.client.game.graphics;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.enums.BoardSide;

import java.awt.*;

import static com.drofff.checkers.client.constants.BoardConstants.*;
import static com.drofff.checkers.client.utils.BoardUtils.getBoardSizeExcludingBorders;
import static com.drofff.checkers.client.utils.BoardUtils.getSizeOfSquare;
import static java.awt.Color.*;
import static java.awt.Font.BOLD;
import static java.awt.Font.MONOSPACED;

public class Board2D extends Canvas {

    private static final Color BROWN = new Color(102, 51, 0);
    private static final Color SELECTED_OUTLINE_COLOR = new Color(0, 255, 255);

    private static final int PIECE_INNER_CIRCLE_OFFSET = 10;
    private static final int PIECE_CROWN_OFFSET_X = 18;
    private static final int PIECE_CROWN_OFFSET_Y = 22;

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

    public void moveKingAtBoardSide(Piece.Position from, Piece.Position to, BoardSide boardSide) {
        movePieceAtBoardSide(from, to, boardSide);
        displayCrownForPieceAtPosition(to);
    }

    public void moveManAtBoardSide(Piece.Position from, Piece.Position to, BoardSide boardSide) {
        movePieceAtBoardSide(from, to, boardSide);
        if(to.isAtOpponentEnd()) {
            displayCrownForPieceAtPosition(to);
        }
    }

    private void movePieceAtBoardSide(Piece.Position from, Piece.Position to, BoardSide boardSide) {
        clearSquareAtPosition(from);
        displayPieceAtPositionOfBoardSide(to, boardSide);
    }

    public void clearSquareAtPosition(Piece.Position position) {
        Graphics graphics = getGraphics();
        graphics.setColor(BROWN);
        fillSquareOfPosition(position.getRow(), position.getColumn(), graphics);
    }

    private void fillSquareOfPosition(int row, int column, Graphics graphics) {
        int[] square = getSquareOfPosition(row, column);
        graphics.fillRect(square[0], square[1], square[2], square[3]);
    }

    public void displayPieceAtBoardSide(Piece piece, BoardSide boardSide) {
        Piece.Position piecePosition = piece.getPosition();
        displayPieceAtPositionOfBoardSide(piecePosition, boardSide);
        if(piece.isKing()) {
            displayCrownForPieceAtPosition(piecePosition);
        }
    }

    private void displayPieceAtPositionOfBoardSide(Piece.Position piecePosition, BoardSide boardSide) {
        int[] square = getSquareOfPosition(piecePosition.getRow(), piecePosition.getColumn());
        fillPieceOfBoardSideAtSquare(boardSide, square);
        drawPieceOutlineOfColorAtSquare(boardSide.getOutlineColor(), square);
    }

    private void fillPieceOfBoardSideAtSquare(BoardSide boardSide, int[] pieceSquare) {
        Graphics graphics = getGraphics();
        graphics.setColor(boardSide.getPieceColor());
        graphics.fillOval(pieceSquare[0], pieceSquare[1], pieceSquare[2], pieceSquare[3]);
    }

    public void selectSquare(Piece.Position position) {
        int[] square = getSquareOfPosition(position.getRow(), position.getColumn());
        drawPieceOutlineOfColorAtSquare(SELECTED_OUTLINE_COLOR, square);
    }

    public void unselectSquareAtBoardSide(Piece.Position position, BoardSide boardSide) {
        int[] square = getSquareOfPosition(position.getRow(), position.getColumn());
        drawPieceOutlineOfColorAtSquare(boardSide.getOutlineColor(), square);
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

    private void displayCrownForPieceAtPosition(Piece.Position position) {
        int[] square = getSquareOfPosition(position.getRow(), position.getColumn());
        fillCrownAtSquare(square);
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

    private void fillCrownAtSquare(int[] square) {
        Graphics graphics = getGraphics();
        graphics.setColor(YELLOW);
        int[] crownXY = getCrownXYForSquare(square);
        Polygon leftCrownPart = leftCrownPartAtXY(crownXY[0], crownXY[1]);
        graphics.fillPolygon(leftCrownPart);
        Polygon centralCrownPart = centralCrownPartAtXY(crownXY[0], crownXY[1]);
        graphics.fillPolygon(centralCrownPart);
        Polygon rightCrownPart = rightCrownPartAtXY(crownXY[0], crownXY[1]);
        graphics.fillPolygon(rightCrownPart);
    }

    private int[] getCrownXYForSquare(int[] square) {
        int crownX = square[0] + PIECE_CROWN_OFFSET_X;
        int crownY = square[1] + PIECE_CROWN_OFFSET_Y;
        return new int[] { crownX, crownY };
    }

    private Polygon leftCrownPartAtXY(int x, int y) {
        Polygon leftCrownPart = new Polygon();
        leftCrownPart.addPoint(x, y);
        int leftPartEndY = y + getCrownPartHeight();
        leftCrownPart.addPoint(x, leftPartEndY);
        int leftPartEndX = x + getCrownPartWidth();
        leftCrownPart.addPoint(leftPartEndX, leftPartEndY);
        return leftCrownPart;
    }

    private Polygon centralCrownPartAtXY(int x, int y) {
        Polygon centralCrownPart = new Polygon();
        int[] centralPartXY = toCentralCrownPartXY(x, y);
        centralCrownPart.addPoint(centralPartXY[0], centralPartXY[1]);
        int centralPartEndX = centralPartXY[0] + getCrownPartWidth();
        centralCrownPart.addPoint(centralPartEndX, centralPartXY[1]);
        int centralPartMiddleX = x + getCrownPartWidth();
        centralCrownPart.addPoint(centralPartMiddleX, y);
        return centralCrownPart;
    }

    private int[] toCentralCrownPartXY(int x, int y) {
        int centralPartX = x + getCrownPartWidth() / 2;
        int centralPartY = y + getCrownPartHeight();
        return new int[] { centralPartX, centralPartY };
    }

    private Polygon rightCrownPartAtXY(int x, int y) {
        Polygon rightCrownPart = new Polygon();
        int[] rightPartXY = toRightCrownPartXY(x, y);
        rightCrownPart.addPoint(rightPartXY[0], rightPartXY[1]);
        int rightPartEndX = rightPartXY[0] + getCrownPartWidth();
        rightCrownPart.addPoint(rightPartEndX, rightPartXY[1]);
        rightCrownPart.addPoint(rightPartEndX, y);
        return rightCrownPart;
    }

    private int[] toRightCrownPartXY(int x, int y) {
        int rightPartX = x + getCrownPartWidth();
        int rightPartY = y + getCrownPartHeight();
        return new int[] { rightPartX, rightPartY };
    }

    private int getCrownPartWidth() {
        return getSizeOfSquare() / 4;
    }

    private int getCrownPartHeight() {
        return getSizeOfSquare() / 3;
    }

    public void displayText(String text) {
        fillTextBackground();
        drawText(text);
    }

    private void fillTextBackground() {
        int[] backgroundCoords = getTextBackgroundCoords();
        Graphics graphics = getGraphics();
        graphics.setColor(BLACK);
        graphics.fillRect(backgroundCoords[0], backgroundCoords[1], backgroundCoords[2], backgroundCoords[3]);
    }

    private int[] getTextBackgroundCoords() {
        int backgroundY = BOARD_SIZE / 4;
        int backgroundHeight = BOARD_SIZE - 2 * backgroundY;
        return new int[] { 0, backgroundY, BOARD_SIZE, backgroundHeight };
    }

    private void drawText(String text) {
        Graphics graphics = getGraphics();
        graphics.setColor(WHITE);
        graphics.setFont(getTextFont());
        graphics.drawString(text, getTextX(), getTextY());
    }

    private Font getTextFont() {
        return new Font(MONOSPACED, BOLD, 17);
    }

    private int getTextY() {
        return BOARD_SIZE / 3;
    }

    private int getTextX() {
        return BOARD_SIZE / 5;
    }

}