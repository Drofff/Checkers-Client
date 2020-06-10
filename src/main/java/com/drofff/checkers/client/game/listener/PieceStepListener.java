package com.drofff.checkers.client.game.listener;

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
import static com.drofff.checkers.client.enums.BoardSide.BLACK;
import static java.util.Objects.nonNull;

public class PieceStepListener implements MouseListener {

    private final PieceService pieceService;
    private final BoardSide userSide;
    private final Board2D gameBoard;

    private Piece.Position fromSquare;

    public PieceStepListener(PieceService pieceService, BoardSide userSide, Board2D gameBoard) {
        this.pieceService = pieceService;
        this.userSide = userSide;
        this.gameBoard = gameBoard;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Piece.Position selectedSquare = getSelectedSquarePosition(mouseEvent);
        if(isSelectionOfPieceAtSquare(selectedSquare)) {
            unselectPreviousOptionIfNeeded();
            fromSquare = selectedSquare;
            gameBoard.selectSquareAtBoardSide(selectedSquare, userSide);
        } else if(isStepToSquare(selectedSquare)) {
            pieceService.movePieceByStep(Step.of(fromSquare, selectedSquare));
            gameBoard.movePieceAtBoardSide(fromSquare, selectedSquare, userSide);
            fromSquare = null;
        }
    }

    private Piece.Position getSelectedSquarePosition(MouseEvent mouseEvent) {
        int column = getPositionByCoordinate(mouseEvent.getX());
        int row = getPositionByCoordinate(mouseEvent.getY());
        Piece.Position selectedSquarePosition = new Piece.Position(column, row);
        return userSide == BLACK ? selectedSquarePosition.inverse() : selectedSquarePosition;
    }

    private int getPositionByCoordinate(int coordinate) {
        int squareSize = BoardUtils.getSizeOfSquare();
        return (coordinate + BORDER_WIDTH) / squareSize;
    }

    private boolean isSelectionOfPieceAtSquare(Piece.Position square) {
        Optional<BoardSide> pieceSideOptional = pieceService.getSideOfPieceAtPositionIfPresent(square);
        return pieceSideOptional.isPresent() && pieceSideOptional.get() == userSide;
    }

    private void unselectPreviousOptionIfNeeded() {
        if(fromSquare != null) {
            gameBoard.unselectSquareAtBoardSide(fromSquare, userSide);
        }
    }

    private boolean isStepToSquare(Piece.Position square) {
        Optional<BoardSide> pieceSideOptional = pieceService.getSideOfPieceAtPositionIfPresent(square);
        return !pieceSideOptional.isPresent() && nonNull(fromSquare) && isAllowedStepTo(square);
    }

    private boolean isAllowedStepTo(Piece.Position destinationSquare) {
        return hasAllowedRow(destinationSquare) && hasAllowedColumn(destinationSquare) &&
                pieceService.isTurnOfCurrentUser();
    }

    private boolean hasAllowedRow(Piece.Position destinationSquare) {
        int allowedRow = fromSquare.getRow() - 1;
        return allowedRow == destinationSquare.getRow();
    }

    private boolean hasAllowedColumn(Piece.Position destinationSquare) {
        int columnDiff = destinationSquare.getColumn() - fromSquare.getColumn();
        return Math.abs(columnDiff) == 1;
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