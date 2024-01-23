package chess;

import java.util.Collection;

public class KnightCalculator extends MasterPieceCalculator{
    public KnightCalculator(ChessBoard board, ChessPosition pos, ChessGame.TeamColor color) {
        super(board, pos, color);
    }

    public Collection<ChessMove> checkMoves() {
        if(validMove(row + 2, col + 1, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row + 2, this.col + 1), null));
        }
        if(validMove(row + 1, col + 2, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row + 1, this.col + 2), null));
        }
        if(validMove(row - 1, col + 2, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row - 1, this.col + 2), null));
        }
        if(validMove(row - 2, col + 1, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row - 2, this.col + 1), null));
        }
        if(validMove(row - 2, col - 1, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row - 2, this.col - 1), null));
        }
        if(validMove(row - 1, col - 2, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row - 1, this.col - 2), null));
        }
        if(validMove(row + 1, col - 2, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row + 1, this.col - 2), null));
        }
        if(validMove(row + 2, col - 1, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row + 2, this.col - 1), null));
        }
        return(this.moves);
    }
}
