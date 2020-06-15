package com.drofff.checkers.client.service;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.enums.BoardSide;

public abstract class AbstractGameStepService implements GameStepService {

    protected final BoardSide userSide;

    protected AbstractGameStepService(BoardSide userSide) {
        this.userSide = userSide;
    }

    @Override
    public boolean canMovePieceFromTo(Piece.Position from, Piece.Position to) {
        return isValidStepFromTo(from, to) || isValidCaptureFromTo(from, to);
    }

    protected abstract boolean isValidStepFromTo(Piece.Position from, Piece.Position to);

    protected abstract boolean isValidCaptureFromTo(Piece.Position from, Piece.Position to);

}