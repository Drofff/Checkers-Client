package com.drofff.checkers.client.service;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.document.Step;
import com.drofff.checkers.client.enums.BoardSide;

import java.util.Optional;

public interface PieceService {

    Optional<BoardSide> getSideOfPieceAtPositionIfPresent(Piece.Position position);

    void movePieceByStep(Step step);

}