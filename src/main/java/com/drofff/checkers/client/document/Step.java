package com.drofff.checkers.client.document;

public class Step {

    private Piece.Position fromPosition;

    private Piece.Position toPosition;

    public static Step of(Piece.Position fromPosition, Piece.Position toPosition) {
        return new Step(fromPosition, toPosition);
    }

    public Step() {}

    private Step(Piece.Position fromPosition, Piece.Position toPosition) {
        this.fromPosition = fromPosition;
        this.toPosition = toPosition;
    }

    public Piece.Position getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(Piece.Position fromPosition) {
        this.fromPosition = fromPosition;
    }

    public Piece.Position getToPosition() {
        return toPosition;
    }

    public void setToPosition(Piece.Position toPosition) {
        this.toPosition = toPosition;
    }

    public Step inverse() {
        return new Step(fromPosition.inverse(), toPosition.inverse());
    }

    public boolean isRemoval() {
        return toPosition.getColumn() == -1 && toPosition.getRow() == -1;
    }

}