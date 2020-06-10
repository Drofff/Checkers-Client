package com.drofff.checkers.client.game;

import com.drofff.checkers.client.document.Board;
import com.drofff.checkers.client.document.Piece;
import com.drofff.checkers.client.document.Step;
import com.drofff.checkers.client.dto.SessionDto;
import com.drofff.checkers.client.enums.BoardSide;
import com.drofff.checkers.client.enums.MessageType;
import com.drofff.checkers.client.exception.ValidationException;
import com.drofff.checkers.client.game.graphics.Board2D;
import com.drofff.checkers.client.game.graphics.GraphicsContext;
import com.drofff.checkers.client.game.listener.PieceStepListener;
import com.drofff.checkers.client.message.SessionMessage;
import com.drofff.checkers.client.message.TextMessage;
import com.drofff.checkers.client.service.MessageProcessor;
import com.drofff.checkers.client.service.PieceService;
import com.drofff.checkers.client.service.PieceServiceImpl;
import com.drofff.checkers.client.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.drofff.checkers.client.enums.BoardSide.BLACK;
import static com.drofff.checkers.client.enums.BoardSide.oppositeSide;
import static com.drofff.checkers.client.enums.MessageType.INITIAL;
import static com.drofff.checkers.client.enums.MessageType.UPDATE;
import static reactor.core.publisher.Mono.error;

public class CheckersGame {

    private static final Logger LOG = LoggerFactory.getLogger(CheckersGame.class);

    private final RSocketRequester rSocketRequester;

    private final MessageProcessor messageProcessor;
    private final PieceService pieceService;

    private Board2D gameBoard;

    public CheckersGame(RSocketRequester rSocketRequester, MessageProcessor messageProcessor) {
        this.rSocketRequester = rSocketRequester;
        this.messageProcessor = messageProcessor;
        this.pieceService = new PieceServiceImpl(rSocketRequester);
    }

    public Flux<SessionMessage> start(String opponentNickname) {
        initBoard2D();
        return getIdOfSessionWithOpponentHavingNickname(opponentNickname)
                .flatMapMany(this::joinSession)
                .doOnNext(this::displaySessionMessage);
    }

    private void initBoard2D() {
        gameBoard = new Board2D();
        GraphicsContext.addComponent(gameBoard);
    }

    private Mono<String> getIdOfSessionWithOpponentHavingNickname(String nickname) {
        return getSessionWithOpponentHavingNickname(nickname)
                .onErrorResume(e -> initSessionWithOpponentHavingNickname(nickname));
    }

    private Mono<String> getSessionWithOpponentHavingNickname(String nickname) {
        return rSocketRequester.route("session.with.{nickname}", nickname)
                .retrieveMono(TextMessage.class)
                .flatMap(messageProcessor::processTextMessage)
                .switchIfEmpty(error(new ValidationException("No session exists")));
    }

    private Mono<String> initSessionWithOpponentHavingNickname(String nickname) {
        SessionDto sessionDto = new SessionDto(nickname);
        return rSocketRequester.route("session.init")
                .data(sessionDto)
                .retrieveMono(TextMessage.class)
                .flatMap(messageProcessor::processTextMessage);
    }

    private Flux<SessionMessage> joinSession(String sessionId) {
        LOG.info("Joining session with id=[{}]", sessionId);
        return rSocketRequester.route("session.join.{sessionId}", sessionId)
                .retrieveFlux(SessionMessage.class)
                .flatMap(ValidationUtils::validateNotErrorMessage);
    }

    private void displaySessionMessage(SessionMessage sessionMessage) {
        MessageType sessionMessageType = sessionMessage.getMessageType();
        LOG.info("Processing session message of type=[{}]", sessionMessageType);
        if(sessionMessageType == INITIAL) {
            initSession(sessionMessage);
        } else if(sessionMessageType == UPDATE) {
            displaySessionUpdate(sessionMessage);
        }
    }

    private void initSession(SessionMessage sessionMessage) {
        Board board = sessionMessage.getGameBoard();
        BoardSide userSide = sessionMessage.getUserSide();
        registerPieceStepListenerForBoardSide(userSide);
        displayPiecesOfUserAtSide(board.getPieces(), sessionMessage.getUserId(), userSide);
    }

    private void registerPieceStepListenerForBoardSide(BoardSide boardSide) {
        PieceStepListener pieceStepListener = new PieceStepListener(pieceService, boardSide, gameBoard);
        gameBoard.addMouseListener(pieceStepListener);
    }

    private void displayPiecesOfUserAtSide(List<Piece> pieces, String userId, BoardSide userSide) {
        if(userSide == BLACK) {
            invertPieces(pieces);
        }
        pieces.forEach(piece -> displayPieceOfUserAtSide(piece, userId, userSide));
    }

    private void invertPieces(List<Piece> pieces) {
        pieces.forEach(piece -> {
            Piece.Position invertedPosition = piece.getPosition().inverse();
            piece.setPosition(invertedPosition);
        });
    }

    private void displayPieceOfUserAtSide(Piece piece, String userId, BoardSide userSide) {
        BoardSide pieceSide = piece.ownerHasId(userId) ? userSide : oppositeSide(userSide);
        gameBoard.displayPieceAtBoardSide(piece, pieceSide);
    }

    private void displaySessionUpdate(SessionMessage sessionMessage) {
        Step step = getRelativeStepFromMessage(sessionMessage);
        gameBoard.movePieceAtBoardSide(step.getFromPosition(), step.getToPosition(), sessionMessage.getUserSide());
    }

    private Step getRelativeStepFromMessage(SessionMessage sessionMessage) {
        BoardSide userSide = sessionMessage.getUserSide();
        return userSide == BLACK ? sessionMessage.getStep() : sessionMessage.getStep().inverse();
    }

}