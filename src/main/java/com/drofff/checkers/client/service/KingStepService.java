package com.drofff.checkers.client.service;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.enums.BoardSide;

import static com.drofff.checkers.client.utils.BoardUtils.adjustPositionToBoardSide;
import static com.drofff.checkers.client.utils.MathUtils.diff;

public class KingStepService extends AbstractGameStepService {

    private final PieceService pieceService;

    public KingStepService(BoardSide userSide, PieceService pieceService) {
        super(userSide);
        this.pieceService = pieceService;
    }

    @Override
    protected boolean isValidStepFromTo(Piece.Position from, Piece.Position to) {
        return validDirectionFromTo(from, to) && countOpponentPiecesBetweenPositions(from, to) == 0;
    }

    @Override
    protected boolean isValidCaptureFromTo(Piece.Position from, Piece.Position to) {
        return validDirectionFromTo(from, to) && countOpponentPiecesBetweenPositions(from, to) == 1;
    }

    private boolean validDirectionFromTo(Piece.Position from, Piece.Position to) {
        int rowDiff = diff(from.getRow(), to.getRow());
        int columnDiff = diff(from.getColumn(), to.getColumn());
        return rowDiff == columnDiff && rowDiff > 0;
    }

    private int countOpponentPiecesBetweenPositions(Piece.Position from, Piece.Position to) {
        Piece.Position correctFromPosition = adjustPositionToBoardSide(from, userSide);
        Piece.Position correctToPosition = adjustPositionToBoardSide(to, userSide);
        return pieceService.countOpponentPiecesBetweenPositions(correctFromPosition, correctToPosition);
    }

}