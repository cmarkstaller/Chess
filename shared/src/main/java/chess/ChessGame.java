package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor color;
    public ChessGame() {
        this.board = new ChessBoard();
        //this.board.resetBoard();
        this.color = TeamColor.WHITE;
    }

    public ChessGame(ChessGame original) {
        this.board = new ChessBoard(original.getBoard());
        this.color = original.color;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return(color);
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.color = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    // Runs piece moves on position it passes in, Don't check for team Color.
    // Take care of the case that the given space is null.
    public Collection<ChessMove> allValidMoves(TeamColor color) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i += 1) {
            //Col
            for (int j = 1; j <= 8; j += 1) {
                if (this.board.getPiece(new ChessPosition(i, j)) != null) {
                    if (this.board.getPiece(new ChessPosition(i, j)).getTeamColor() == color) {
                        allMoves.addAll(this.validMoves(new ChessPosition(i, j)));
                    }
                }
            }
        }
        return(allMoves);

    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> moves = this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
        Collection<ChessMove> legalMoves = new ArrayList<>();
        //validMoves should then call is in check and check mate.
        for (ChessMove move : moves) {
            ChessGame tempGame = new ChessGame(this);
            tempGame.movePiece(new ChessMove(startPosition, move.getEndPosition(), move.getPromotionPiece()), tempGame.getBoard());
            if (!tempGame.isInCheck(this.board.getPiece(startPosition).getTeamColor())) {
                legalMoves.add(move);
            }
        }
        return(legalMoves);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    // Here is when you care about color. Make move calls valid Moves
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()).getTeamColor() != this.color) {
            throw new InvalidMoveException();
        }
        if (this.validMoves(move.getStartPosition()).contains(move)) {
            movePiece(move, this.board);
        }
        else {
            throw new InvalidMoveException();
        }
        if (this.color == TeamColor.WHITE) {
            this.color = TeamColor.BLACK;
        }
        else {
            this.color = TeamColor.WHITE;
        }
    }

    public void movePiece(ChessMove move, ChessBoard board) {
        if (move.getPromotionPiece() != null) {
            board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
        }
        else {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        }
        board.addPiece(move.getStartPosition(), null);
    }



    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = kingPosition(teamColor);
        //Row
        for (int i = 1; i <= 8; i += 1) {
            //Col
            for (int j = 1; j <= 8; j += 1) {
                ChessPosition pos = new ChessPosition(i, j);
                if (this.board.getPiece(pos) != null) {
                    if (this.board.getPiece(pos).getTeamColor() != teamColor) {
                        for (ChessMove move : this.board.getPiece(pos).pieceMoves(this.board, pos)) {
                            if (move.getEndPosition().equals(kingPos)) {
                                return (true);
                            }
                        }
                    }
                }
            }
        }
        return(false);
    }
    public ChessPosition kingPosition(TeamColor teamColor) {
        for (int i = 1; i <= 8; i += 1) {
            //Col
            for (int j = 1; j <= 8; j += 1) {
                if (this.board.getPiece(new ChessPosition(i, j)) != null) {
                    if (this.board.getPiece(new ChessPosition(i, j)).getPieceType() == ChessPiece.PieceType.KING) {
                        if (this.board.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                            return (new ChessPosition(i, j));
                        }
                    }
                }
            }
        }
        return(null);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return(this.isInCheck(teamColor) && this.isInStalemate(teamColor));

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (this.allValidMoves(teamColor).isEmpty()) {
            return(true);
        }
        else {
            return(false);
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return(this.board);
    }
}
