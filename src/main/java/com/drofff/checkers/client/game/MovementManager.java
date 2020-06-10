package com.drofff.checkers.client.game;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.document.Step;
import com.drofff.checkers.client.enums.BoardSide;
import com.drofff.checkers.client.game.graphics.Board2D;
import com.drofff.checkers.client.service.PieceService;
import com.drofff.checkers.client.utils.BoardUtils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

import static com.drofff.checkers.client.constants.BoardConstants.BORDER_WIDTH;
import static java.util.Objects.nonNull;

public class MovementManager implements MouseListener {

    private final PieceService pieceService;
    private final BoardSide userSide;
    private final Board2D gameBoard;

    private Piece.Position fromSquare;

    public MovementManager(PieceService pieceService, BoardSide userSide, Board2D gameBoard) {
        this.pieceService = pieceService;
        this.userSide = userSide;
        this.gameBoard = gameBoard;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Piece.Position selectedSquare = getSelectedSquarePosition(mouseEvent);
        if(isSelectionOfPieceAtSquare(selectedSquare)) {
            selectSquare(selectedSquare);
        } else if(isStepToSquare(selectedSquare) || isCaptureEndingAtSquare(selectedSquare)) {
            stepToSquare(selectedSquare);
        }
    }

    private Piece.Position getSelectedSquarePosition(MouseEvent mouseEvent) {
        int column = getPositionByCoordinate(mouseEvent.getX());
        int row = getPositionByCoordinate(mouseEvent.getY());
        return new Piece.Position(column, row);
    }

    private int getPositionByCoordinate(int coordinate) {
        int squareSize = BoardUtils.getSizeOfSquare();
        return (coordinate + BORDER_WIDTH) / squareSize;
    }

    private boolean isSelectionOfPieceAtSquare(Piece.Position square) {
        Piece.Position correctPosition = correctPositionIfNeeded(square);
        Optional<BoardSide> pieceSideOptional = pieceService.getSideOfPieceAtPositionIfPresent(correctPosition);
        return pieceSideOptional.isPresent() && pieceSideOptional.get() == userSide;
    }

    private void selectSquare(Piece.Position square) {
        unselectPreviousOptionIfNeeded();
        fromSquare = square;
        gameBoard.selectSquareAtBoardSide(square, userSide);
    }

    private void unselectPreviousOptionIfNeeded() {
        if(fromSquare != null) {
            gameBoard.unselectSquareAtBoardSide(fromSquare, userSide);
        }
    }

    private boolean isStepToSquare(Piece.Position square) {
        Piece.Position correctPosition = correctPositionIfNeeded(square);
        return isValidMoveTo(correctPosition) && isAllowedStepTo(square);
    }

    private void stepToSquare(Piece.Position square) {
        Piece.Position fromPosition = correctPositionIfNeeded(fromSquare);
        Piece.Position toPosition = correctPositionIfNeeded(square);
        pieceService.movePieceByStep(Step.of(fromPosition, toPosition));
        gameBoard.movePieceAtBoardSide(fromSquare, square, userSide);
        fromSquare = null;
    }

    private boolean isAllowedStepTo(Piece.Position destinationSquare) {
        return hasAllowedStepRow(destinationSquare) && hasAllowedStepColumn(destinationSquare) &&
                pieceService.isTurnOfCurrentUser();
    }

    private boolean hasAllowedStepRow(Piece.Position destinationSquare) {
        int allowedRow = fromSquare.getRow() - 1;
        return allowedRow == destinationSquare.getRow();
    }

    private boolean hasAllowedStepColumn(Piece.Position destinationSquare) {
        int columnDiff = diff(destinationSquare.getColumn(), fromSquare.getColumn());
        return columnDiff == 1;
    }

    private boolean isCaptureEndingAtSquare(Piece.Position square) {
        Piece.Position correctPosition = correctPositionIfNeeded(square);
        return pieceService.isTurnOfCurrentUser() && isValidMoveTo(correctPosition) &&
                isValidCaptureMoveEndingAtSquare(square);
    }

    private boolean isValidMoveTo(Piece.Position square) {
        return nonNull(fromSquare) && isEmptySquare(square);
    }

    private boolean isEmptySquare(Piece.Position square) {
        Optional<BoardSide> boardSideOptional = pieceService.getSideOfPieceAtPositionIfPresent(square);
        return !boardSideOptional.isPresent();
    }

    private boolean isValidCaptureMoveEndingAtSquare(Piece.Position square) {
        return isAllowedCaptureMoveEndingAtSquare(square) &&
                doesMoveToSquareCaptureOpponentPiece(square);
    }

    private boolean isAllowedCaptureMoveEndingAtSquare(Piece.Position position) {
        int rowDiff = diff(position.getRow(), fromSquare.getRow());
        int columnDiff = diff(position.getColumn(), fromSquare.getColumn());
        return rowDiff == columnDiff && rowDiff == 2;
    }

    private int diff(int num0, int num1) {
        return Math.abs(num0 - num1);
    }

    private boolean doesMoveToSquareCaptureOpponentPiece(Piece.Position toSquare) {
        Piece.Position capturedPosition = getSquareBetweenFromAndTo(toSquare);
        Piece.Position correctedPosition = correctPositionIfNeeded(capturedPosition);
        return pieceService.getSideOfPieceAtPositionIfPresent(correctedPosition)
                .filter(this::isSideOfOpponent)
                .isPresent();
    }

    private Piece.Position getSquareBetweenFromAndTo(Piece.Position toSquare) {
        int columnBetween = avg(fromSquare.getColumn(), toSquare.getColumn());
        int rowBetween = avg(fromSquare.getRow(), toSquare.getRow());
        return new Piece.Position(columnBetween, rowBetween);
    }

    private int avg(int num0, int num1) {
        return ( num0 + num1 ) / 2;
    }

    private Piece.Position correctPositionIfNeeded(Piece.Position position) {
        return userSide == BoardSide.BLACK ? position.inverse() : position;
    }

    private boolean isSideOfOpponent(BoardSide side) {
        return !userSide.equals(side);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        //not used
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        //not used
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        //not used
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        //not used
    }

}