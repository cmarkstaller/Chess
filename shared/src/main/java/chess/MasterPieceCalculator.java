
package chess;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class MasterPieceCalculator {
    protected final ChessPosition pos;
    protected final int row;
    protected final int col;
    protected final ChessBoard board;
    protected ArrayList<ChessMove> moves;
    protected final ChessGame.TeamColor color;

    public MasterPieceCalculator(ChessBoard board, ChessPosition pos, ChessGame.TeamColor color) {
        this.board = board;
        this.pos = pos;
        this.moves = new ArrayList<>();
        this.row = this.pos.getRow();
        this.col = this.pos.getColumn();
        this.color = color;
    }
    public boolean inBounds(int row, int col) {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }

    public boolean noCollision(int row, int col, ChessBoard board) {
        if(board.getPiece(new ChessPosition(row, col)) != null) {
            return (this.color != board.getPiece(new ChessPosition(row, col)).getTeamColor());
        }
        else return(true);
    }

    public boolean validMove(int row, int col, ChessBoard board) {
        if (inBounds(row, col)){
            return(noCollision(row, col, board));
        }
        else {
            return(false);
        }
    }
}



