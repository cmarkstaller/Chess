package chess;
import java.util.ArrayList;
import java.util.Collection;
public class KingCalculator extends MasterPieceCalculator{

    public KingCalculator(ChessBoard board, ChessPosition pos, ChessGame.TeamColor color) {
        super(board, pos, color);
    }
    public Collection<ChessMove> checkMoves() {
        if(validMove(row + 1, col, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row + 1, this.col), null));
        }
        if(validMove(row + 1, col + 1, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row + 1, this.col + 1), null));
        }
        if(validMove(row, col + 1, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row, this.col + 1), null));
        }
        if(validMove(row - 1, col + 1, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row - 1, this.col + 1), null));
        }
        if(validMove(row - 1, col, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row - 1, this.col), null));
        }
        if(validMove(row - 1, col - 1, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row - 1, this.col - 1), null));
        }
        if(validMove(row, col - 1, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row, this.col - 1), null));
        }
        if(validMove(row + 1, col - 1, this.board)) {
            this.moves.add(new ChessMove(this.pos, new ChessPosition(this.row + 1, this.col - 1), null));
        }
        return(this.moves);
    }
}
