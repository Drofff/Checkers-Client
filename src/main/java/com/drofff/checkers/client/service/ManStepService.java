package com.drofff.checkers.client.service;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.enums.BoardSide;

import static com.drofff.checkers.client.utils.BoardUtils.adjustPositionToBoardSide;
import static com.drofff.checkers.client.utils.MathUtils.avg;
import static com.drofff.checkers.client.utils.MathUtils.diff;

public class ManStepService extends AbstractGameStepService {

    private final PieceService pieceService;

    public ManStepService(BoardSide userSide, PieceService pieceService) {
        super(userSide);
        this.pieceService = pieceService;
    }

    @Override
    protected boolean isValidStepFromTo(Piece.Position from, Piece.Position to) {
        return isValidStepFromToByRow(from, to) && isValidStepFromToByColumn(from, to);
    }

    private boolean isValidStepFromToByRow(Piece.Position from, Piece.Position to) {
        int allowedRow = from.getRow() - 1;
        return allowedRow == to.getRow();
    }

    private boolean isValidStepFromToByColumn(Piece.Position from, Piece.Position to) {
        int columnDiff = diff(from.getColumn(), to.getColumn());
        return columnDiff == 1;
    }

    @Override
    protected boolean isValidCaptureFromTo(Piece.Position from, Piece.Position to) {
        return isValidDirectionForCaptureFromTo(from, to) && moveFromToCapturesOpponentPiece(from, to);
    }

    private boolean isValidDirectionForCaptureFromTo(Piece.Position from, Piece.Position to) {
        int rowDiff = diff(from.getRow(), to.getRow());
        int columnDiff = diff(from.getColumn(), to.getColumn());
        return rowDiff == columnDiff && rowDiff == 2;
    }

    private boolean moveFromToCapturesOpponentPiece(Piece.Position from, Piece.Position to) {
        Piece.Position capturedPosition = getSquareBetweenFromAndTo(from, to);
        Piece.Position correctCapturedPosition = adjustPositionToBoardSide(capturedPosition, userSide);
        return pieceService.getSideOfPieceAtPositionIfPresent(correctCapturedPosition)
                .filter(this::isSideOfOpponent)
                .isPresent();
    }

    private Piece.Position getSquareBetweenFromAndTo(Piece.Position from, Piece.Position to) {
        int columnBetween = avg(from.getColumn(), to.getColumn());
        int rowBetween = avg(from.getRow(), to.getRow());
        return new Piece.Position(columnBetween, rowBetween);
    }

    private boolean isSideOfOpponent(BoardSide side) {
        return !userSide.equals(side);
    }

}