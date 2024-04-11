package chess;

import java.util.Collection;

public class RookCalculator extends MasterPieceCalculator{
    public RookCalculator(ChessBoard board, ChessPosition pos, ChessGame.TeamColor color) {
        super(board, pos, color);
    }

    public Collection<ChessMove> checkMoves() {
        return(rookStyleMoves(this.row, this.col, this.board, this.pos));
    }
}
