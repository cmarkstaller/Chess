package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private int gameID;
    private ChessMove chessMove;

    public MakeMove(String authToken, int gameID, ChessMove chessMove) {
        super(authToken);
        this.commandType = UserGameCommand.CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.chessMove = chessMove;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getChessMove() {
        return chessMove;
    }
}