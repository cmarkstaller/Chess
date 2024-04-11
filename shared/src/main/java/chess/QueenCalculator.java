package chess;

import java.util.Collection;

public class QueenCalculator extends MasterPieceCalculator{
    public QueenCalculator(ChessBoard board, ChessPosition pos, ChessGame.TeamColor color) {
        super(board, pos, color);
    }

    public Collection<ChessMove> checkMoves() {
        Collection<ChessMove> rookStyle = rookStyleMoves(this.row, this.col, this.board, this.pos);
        Collection<ChessMove> bishopStyle = bishopStyleMoves(this.row, this.col, this.board, this.pos);
        rookStyle.addAll(bishopStyle);

        return (rookStyle);
    }
}
