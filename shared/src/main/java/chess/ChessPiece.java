package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return(this.pieceColor);
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return(this.type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.getPieceType() == PieceType.KING) {
            KingCalculator moveKing = new KingCalculator(board, myPosition, this.getTeamColor());
            return(moveKing.checkMoves());
        }
        else if (this.getPieceType() == PieceType.KNIGHT) {
            KnightCalculator moveKnight = new KnightCalculator(board, myPosition, this.getTeamColor());
            return(moveKnight.checkMoves());
        }
        else if (this.getPieceType() == PieceType.ROOK) {
            RookCalculator moveRook = new RookCalculator(board, myPosition, this.getTeamColor());
            return(moveRook.checkMoves());
        }
        else if (this.getPieceType() == PieceType.BISHOP) {
            BishopCalculator moveBishop = new BishopCalculator(board, myPosition, this.getTeamColor());
            return(moveBishop.checkMoves());
        }
        else if (this.getPieceType() == PieceType.QUEEN) {
            QueenCalculator moveQueen = new QueenCalculator(board, myPosition, this.getTeamColor());
            return(moveQueen.checkMoves());
        }
        else if (this.getPieceType() == PieceType.PAWN) {
            PawnCalculator movePawn = new PawnCalculator(board, myPosition, this.getTeamColor());
            return(movePawn.checkMoves());
        }
        return(new ArrayList<>());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}


