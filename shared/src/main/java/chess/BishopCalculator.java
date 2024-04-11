package chess;

import java.util.Collection;

public class BishopCalculator extends MasterPieceCalculator {
    public BishopCalculator(ChessBoard board, ChessPosition pos, ChessGame.TeamColor color) {
        super(board, pos, color);
    }

    public Collection<ChessMove> checkMoves() {
        return (bishopStyleMoves(this.row, this.col, this.board, this.pos));
    }
}
