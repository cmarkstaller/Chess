//import chess.ChessBoard;
//import chess.ChessMove;
//import chess.ChessPosition;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//public Collection<ChessMove> bishopStyleMoves(int bishopRow, int bishopCol, ChessBoard bishopBoard, ChessPosition bishopPos) {
//    Collection<ChessMove> bishopMoves = new ArrayList<>();
//    boolean ne = true;
//    boolean se = true;
//    boolean sw = true;
//    boolean nw = true;
//
//    for(int i = 1; i < 8; i += 1) {
//        if (ne) {
//            if (validMove(bishopRow + i, bishopCol + i, bishopBoard)) {
//                bishopMoves.add(new ChessMove(bishopPos, new ChessPosition(bishopRow + i, bishopCol), null));
//
//                if (capturePiece(bishopRow + i, bishopCol + i, bishopBoard)) {
//                    ne = false;
//                }
//            }
//            else {
//                ne = false;
//            }
//        }
//        if (se) {
//            if (validMove(bishopRow - i, bishopCol + i, bishopBoard)) {
//                bishopMoves.add(new ChessMove(bishopPos, new ChessPosition(bishopRow, bishopCol + i), null));
//
//                if (capturePiece(bishopRow - i, bishopCol + i, bishopBoard)) {
//                    se = false;
//                }
//            }
//            else {
//                se = false;
//            }
//        }
//        if (sw) {
//            if (validMove(bishopRow - i, bishopCol - i, bishopBoard)) {
//                bishopMoves.add(new ChessMove(bishopPos, new ChessPosition(bishopRow - i, bishopCol), null));
//
//                if (capturePiece(bishopRow - i, bishopCol - i, bishopBoard)) {
//                    sw = false;
//                }
//            }
//            else {
//                sw = false;
//            }
//        }
//        if (nw) {
//            if (validMove(bishopRow + i, bishopCol - i, bishopBoard)) {
//                bishopMoves.add(new ChessMove(bishopPos, new ChessPosition(bishopRow, bishopCol - i), null));
//
//                if (capturePiece(bishopRow + i, bishopCol - i, bishopBoard)) {
//                    nw = false;
//                }
//            }
//            else {
//                nw = false;
//            }
//        }
//
//    }
//    return(bishopMoves);
//}