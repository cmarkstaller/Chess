package chess;

import java.util.Collection;

public class BishopCalculator extends MasterPieceCalculator {
    public BishopCalculator(ChessBoard board, ChessPosition pos, ChessGame.TeamColor color) {
        super(board, pos, color);
    }

    public Collection<ChessMove> checkMoves() {
        boolean ne = true;
        boolean se = true;
        boolean sw = true;
        boolean nw = true;

        for(int i = 1; i < 8; i += 1) {
            if (ne) {
                if (validMove(row + i, col + i, this.board)) {
                    this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row + i, this.col + i), null));

                    if (capturePiece(row + i, col + i, this.board)) {
                        ne = false;
                    }
                }
                else {
                    ne = false;
                }
            }
            if (se) {
                if (validMove(row - i, col + i, this.board)) {
                    this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row - i, this.col + i), null));

                    if (capturePiece(row - i, col + i, this.board)) {
                        se = false;
                    }
                }
                else {
                    se = false;
                }
            }
            if (sw) {
                if (validMove(row - i, col - i, this.board)) {
                    this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row - i, this.col - i), null));

                    if (capturePiece(row - i, col - i, this.board)) {
                        sw = false;
                    }
                }
                else {
                    sw = false;
                }
            }
            if (nw) {
                if (validMove(row + i, col - i, this.board)) {
                    this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row + i, this.col - i), null));

                    if (capturePiece(row + i, col - i, this.board)) {
                        nw = false;
                    }
                }
                else {
                    nw = false;
                }
            }

        }
        return(this.moves);
    }
}
