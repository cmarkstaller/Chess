
package chess;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    public boolean noCollision(int row, int col, ChessBoard board) {
        if(board.getPiece(new ChessPosition(row, col)) != null) {
            return (this.color != board.getPiece(new ChessPosition(row, col)).getTeamColor());
        }
        else return(true);
    }

    public boolean capturePiece(int row, int col, ChessBoard board) {
        if(board.getPiece(new ChessPosition(row, col)) != null) {
            return this.color != board.getPiece(new ChessPosition(row, col)).getTeamColor();
        }
        return(false);
    }

    public boolean validMove(int row, int col, ChessBoard board) {
        if (inBounds(row, col)){
            return(noCollision(row, col, board));
        }
        else {
            return(false);
        }
    }

    public Collection<ChessMove> rookStyleMoves(int rookRow, int rookCol, ChessBoard rookBoard, ChessPosition rookPos) {
        Collection<ChessMove> rookMoves = new ArrayList<>();
        boolean north = true;
        boolean east = true;
        boolean south = true;
        boolean west = true;

        for(int i = 1; i < 8; i += 1) {
            if (north) {
                if (validMove(rookRow + i, rookCol, rookBoard)) {
                    rookMoves.add(new ChessMove(rookPos, new ChessPosition(rookRow + i, rookCol), null));

                    if (capturePiece(rookRow + i, rookCol, rookBoard)) {
                        north = false;
                    }
                }
                else {
                    north = false;
                }
            }
            if (east) {
                if (validMove(rookRow, rookCol + i, rookBoard)) {
                    rookMoves.add(new ChessMove(rookPos, new ChessPosition(rookRow, rookCol + i), null));

                    if (capturePiece(rookRow, rookCol + i, rookBoard)) {
                        east = false;
                    }
                }
                else {
                    east = false;
                }
            }
            if (south) {
                if (validMove(rookRow - i, rookCol, rookBoard)) {
                    rookMoves.add(new ChessMove(rookPos, new ChessPosition(rookRow - i, rookCol), null));

                    if (capturePiece(rookRow - i, rookCol, rookBoard)) {
                        south = false;
                    }
                }
                else {
                    south = false;
                }
            }
            if (west) {
                if (validMove(rookRow, rookCol - i, rookBoard)) {
                    rookMoves.add(new ChessMove(rookPos, new ChessPosition(rookRow, rookCol - i), null));

                    if (capturePiece(rookRow, rookCol - i, rookBoard)) {
                        west = false;
                    }
                }
                else {
                    west = false;
                }
            }

        }
        return(rookMoves);
    }

    public Collection<ChessMove> bishopStyleMoves(int bishopRow, int bishopCol, ChessBoard bishopBoard, ChessPosition bishopPos) {
        Collection<ChessMove> bishopMoves = new ArrayList<>();
        boolean ne = true;
        boolean se = true;
        boolean sw = true;
        boolean nw = true;

        for(int i = 1; i < 8; i += 1) {
            if (ne) {
                if (validMove(bishopRow + i, bishopCol + i, bishopBoard)) {
                    bishopMoves.add(new ChessMove(bishopPos, new ChessPosition(bishopRow + i, bishopCol + i), null));

                    if (capturePiece(bishopRow + i, bishopCol + i, bishopBoard)) {
                        ne = false;
                    }
                }
                else {
                    ne = false;
                }
            }
            if (se) {
                if (validMove(bishopRow - i, bishopCol + i, bishopBoard)) {
                    bishopMoves.add(new ChessMove(bishopPos, new ChessPosition(bishopRow - i, bishopCol + i), null));

                    if (capturePiece(bishopRow - i, bishopCol + i, bishopBoard)) {
                        se = false;
                    }
                }
                else {
                    se = false;
                }
            }
            if (sw) {
                if (validMove(bishopRow - i, bishopCol - i, bishopBoard)) {
                    bishopMoves.add(new ChessMove(bishopPos, new ChessPosition(bishopRow - i, bishopCol - i), null));

                    if (capturePiece(bishopRow - i, bishopCol - i, bishopBoard)) {
                        sw = false;
                    }
                }
                else {
                    sw = false;
                }
            }
            if (nw) {
                if (validMove(bishopRow + i, bishopCol - i, bishopBoard)) {
                    bishopMoves.add(new ChessMove(bishopPos, new ChessPosition(bishopRow + i, bishopCol - i), null));

                    if (capturePiece(bishopRow + i, bishopCol - i, bishopBoard)) {
                        nw = false;
                    }
                }
                else {
                    nw = false;
                }
            }

        }
        return(bishopMoves);
    }
}



