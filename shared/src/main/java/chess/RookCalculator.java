package chess;

import java.util.Collection;

public class RookCalculator extends MasterPieceCalculator{
    public RookCalculator(ChessBoard board, ChessPosition pos, ChessGame.TeamColor color) {
        super(board, pos, color);
    }

    public Collection<ChessMove> checkMoves() {
        boolean north = true;
        boolean east = true;
        boolean south = true;
        boolean west = true;

        for(int i = 1; i < 8; i += 1) {
            if (north) {
                if (validMove(row + i, col, this.board)) {
                    this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row + i, this.col), null));

                    if (capturePiece(row + i, col, this.board)) {
                        north = false;
                    }
                }
                else {
                    north = false;
                }
            }
            if (east) {
                if (validMove(row, col + i, this.board)) {
                    this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row, this.col + i), null));

                    if (capturePiece(row, col + i, this.board)) {
                        east = false;
                    }
                }
                else {
                    east = false;
                }
            }
            if (south) {
                if (validMove(row - i, col, this.board)) {
                    this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row - i, this.col), null));

                    if (capturePiece(row - i, col, this.board)) {
                        south = false;
                    }
                }
                else {
                    south = false;
                }
            }
            if (west) {
                if (validMove(row, col - i, this.board)) {
                    this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row, this.col - i), null));

                    if (capturePiece(row, col - i, this.board)) {
                        west = false;
                    }
                }
                else {
                    west = false;
                }
            }

        }
        return(this.moves);
    }
}
