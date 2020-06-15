package com.drofff.checkers.client.game;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.document.Step;
import com.drofff.checkers.client.enums.BoardSide;
import com.drofff.checkers.client.game.graphics.Board2D;
import com.drofff.checkers.client.service.GameStepService;
import com.drofff.checkers.client.service.KingStepService;
import com.drofff.checkers.client.service.ManStepService;
import com.drofff.checkers.client.service.PieceService;
import com.drofff.checkers.client.utils.BoardUtils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

import static com.drofff.checkers.client.constants.BoardConstants.BORDER_WIDTH;
import static com.drofff.checkers.client.utils.BoardUtils.adjustPositionToBoardSide;
import static java.util.Objects.nonNull;

public class MovementManager implements MouseListener {

    private final PieceService pieceService;
    private final GameStepService kingStepService;
    private final GameStepService manStepService;

    private final BoardSide userSide;
    private final Board2D gameBoard;

    private Piece.Position fromSquare;

    public MovementManager(PieceService pieceService, BoardSide userSide, Board2D gameBoard) {
        this.pieceService = pieceService;
        this.userSide = userSide;
        this.gameBoard = gameBoard;
        this.kingStepService = new KingStepService(userSide, pieceService);
        this.manStepService = new ManStepService(userSide, pieceService);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Piece.Position selectedSquare = getSelectedSquarePosition(mouseEvent);
        if(isSelectionOfPieceAtSquare(selectedSquare)) {
            selectSquare(selectedSquare);
        } else if(isSelectionOfSquareAsDestination(selectedSquare)) {
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
        Piece.Position correctPosition = adjustPositionToBoardSide(square, userSide);
        Optional<BoardSide> pieceSideOptional = pieceService.getSideOfPieceAtPositionIfPresent(correctPosition);
        return pieceSideOptional.isPresent() && pieceSideOptional.get() == userSide;
    }

    private void selectSquare(Piece.Position square) {
        unselectPreviousOptionIfNeeded();
        fromSquare = square;
        gameBoard.selectSquare(square);
    }

    private void unselectPreviousOptionIfNeeded() {
        if(fromSquare != null) {
            gameBoard.unselectSquareAtBoardSide(fromSquare, userSide);
        }
    }

    private boolean isSelectionOfSquareAsDestination(Piece.Position square) {
        Piece.Position correctSquare = adjustPositionToBoardSide(square, userSide);
        return pieceService.isTurnOfCurrentUser() && nonNull(fromSquare) && isEmptySquare(correctSquare);
    }

    private boolean isEmptySquare(Piece.Position square) {
        Optional<BoardSide> boardSideOptional = pieceService.getSideOfPieceAtPositionIfPresent(square);
        return !boardSideOptional.isPresent();
    }

    private void stepToSquare(Piece.Position square) {
        if(isPieceAtFromSquareKing()) {
            moveKingToSquare(square);
        } else {
            moveManToSquare(square);
        }
    }

    private boolean isPieceAtFromSquareKing() {
        Piece.Position correctFromPosition = adjustPositionToBoardSide(fromSquare, userSide);
        return pieceService.isPieceAtPositionKing(correctFromPosition);
    }

    private void moveKingToSquare(Piece.Position square) {
        if(kingStepService.canMovePieceFromTo(fromSquare, square)) {
            gameBoard.moveKingAtBoardSide(fromSquare, square, userSide);
            saveStepToSquare(square);
            fromSquare = null;
        }
    }

    private void moveManToSquare(Piece.Position square) {
        if(manStepService.canMovePieceFromTo(fromSquare, square)) {
            gameBoard.moveManAtBoardSide(fromSquare, square, userSide);
            saveStepToSquare(square);
            fromSquare = null;
        }
    }

    private void saveStepToSquare(Piece.Position square) {
        Piece.Position fromPosition = adjustPositionToBoardSide(fromSquare, userSide);
        Piece.Position toPosition = adjustPositionToBoardSide(square, userSide);
        pieceService.movePieceByStep(Step.of(fromPosition, toPosition));
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