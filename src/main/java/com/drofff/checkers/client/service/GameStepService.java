package com.drofff.checkers.client.service;

import com.drofff.checkers.client.document.Piece;

public interface GameStepService {

    boolean canMovePieceFromTo(Piece.Position from, Piece.Position to);

}