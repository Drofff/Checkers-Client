package com.drofff.checkers.client.service;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.document.Step;
import com.drofff.checkers.client.enums.BoardSide;

import java.util.Optional;

public interface PieceService {

    Optional<BoardSide> getSideOfPieceAtPositionIfPresent(Piece.Position position);

    Integer countOpponentPiecesBetweenPositions(Piece.Position fromPosition, Piece.Position toPosition);

    void movePieceByStep(Step step);

    Boolean isPieceAtPositionKing(Piece.Position position);

    Boolean isTurnOfCurrentUser();

}