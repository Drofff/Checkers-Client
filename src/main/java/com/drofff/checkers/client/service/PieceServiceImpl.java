package com.drofff.checkers.client.service;

import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.document.Step;
import com.drofff.checkers.client.enums.BoardSide;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.util.Optional;

public class PieceServiceImpl implements PieceService {

    private final RSocketRequester rSocketRequester;

    public PieceServiceImpl(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @Override
    public Optional<BoardSide> getSideOfPieceAtPositionIfPresent(Piece.Position position) {
        return rSocketRequester.route("piece.sideByPosition")
                .data(position)
                .retrieveMono(BoardSide.class)
                .blockOptional();
    }

    @Override
    public void movePieceByStep(Step step) {
        rSocketRequester.route("piece.step").data(step)
                .send().subscribe();
    }

    @Override
    public Boolean isTurnOfCurrentUser() {
        return rSocketRequester.route("isTurn")
                .retrieveMono(Boolean.class)
                .block();
    }

}