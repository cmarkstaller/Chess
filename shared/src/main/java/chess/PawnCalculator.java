package chess;

import java.util.Collection;

public class PawnCalculator extends MasterPieceCalculator{
    public PawnCalculator(ChessBoard board, ChessPosition pos, ChessGame.TeamColor color) {
        super(board, pos, color);
    }
    public Collection<ChessMove> checkMoves() {
        if (this.color == ChessGame.TeamColor.WHITE) {
            if (this.row == 2) {
                if (validMove(this.row + 1, this.col, this.board)) {
                    if (validMove(this.row + 2, this.col, this.board) && !capturePiece(this.row + 2, this.col, this.board)) {
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 2, col), null));
                    }
                }
            }
            if (validMove(this.row + 1, this.col, this.board) && !capturePiece(this.row + 1, this.col, this.board)) {
                if (this.row == 7) {
                    moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col), ChessPiece.PieceType.QUEEN));
                }
                else {
                    moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col), null));
                }
            }
            if (inBounds(this.row + 1, col)) {
                if (capturePiece(this.row + 1, this.col + 1, this.board)) {
                    if (this.row == 7) {
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.QUEEN));
                    }
                    else {
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col + 1), null));
                    }
                }
                if (capturePiece(this.row + 1, this.col - 1, this.board)) {
                    if (this.row == 7) {
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.QUEEN));
                    }
                    else {
                        moves.add(new ChessMove(this.pos, new ChessPosition(row + 1, col - 1), null));
                    }
                }
            }
        }
        else if (this.color == ChessGame.TeamColor.BLACK) {
            if (this.row == 7) {
                if (validMove(this.row - 1, this.col, this.board)) {
                    if (validMove(this.row - 2, this.col, this.board) && !capturePiece(this.row - 2, this.col, this.board)) {
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 2, col), null));
                    }
                }
            }
            if (validMove(this.row - 1, this.col, this.board) && !capturePiece(this.row - 1, this.col, this.board)) {
                if (this.row == 2) {
                    moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col), ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col), ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col), ChessPiece.PieceType.BISHOP));
                    moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col), ChessPiece.PieceType.QUEEN));
                }
                else {
                    moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col), null));
                }
            }
            if (inBounds(this.row - 1, col)) {
                if (capturePiece(this.row - 1, this.col + 1, this.board)) {
                    if (this.row == 2) {
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.QUEEN));
                    }
                    else {
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col + 1), null));
                    }
                }
                if (capturePiece(this.row - 1, this.col - 1, this.board)) {
                    if (this.row == 2) {
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.QUEEN));
                    }
                    else {
                        moves.add(new ChessMove(this.pos, new ChessPosition(row - 1, col - 1), null));
                    }
                }
            }
        }
        return(this.moves);
    }
}
