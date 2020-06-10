package com.drofff.checkers.client.document;

import static com.drofff.checkers.client.constants.BoardConstants.ROWS_COUNT;

public class Piece {

    private String uid;

    private Position position;

    private String ownerId;

    private boolean isKing;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isKing() {
        return isKing;
    }

    public void setKing(boolean king) {
        isKing = king;
    }

    public boolean ownerHasId(String userId) {
        return ownerId.equals(userId);
    }

    public static class Position {

        private int column;

        private int row;

        public Position() {}

        public Position(int column, int row) {
            this.column = column;
            this.row = row;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public Position inverse() {
            int invertedColumn = invertPosition(column);
            int invertedRow = invertPosition(row);
            return new Position(invertedColumn, invertedRow);
        }

        private int invertPosition(int pos) {
            int maxPos = ROWS_COUNT - 1;
            return maxPos - pos;
        }

    }

}