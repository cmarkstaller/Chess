package chess;
import java.util.ArrayList;
import java.util.Collection;
public class KingCalculator {

    private final ChessPosition pos;
    private final int row;
    private final int col;
    private final ChessBoard board;
    private ArrayList<ChessMove> moves;

    private final ChessGame.TeamColor color;

    public KingCalculator(ChessBoard board, ChessPosition pos, ChessGame.TeamColor color) {
        this.board = board;
        this.pos = pos;
        this.moves = new ArrayList<>();
        this.row = this.pos.getRow();
        this.col = this.pos.getColumn();
        this.color = color;
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
