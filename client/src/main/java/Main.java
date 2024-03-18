import chess.*;
import server.Server;
import ui.Repl;
import ui.ServerFacade;

public class Main {
    public static void main(String[] args) {
        int port = 8080;

        Server myServer = new Server();
        myServer.run(port);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        Repl repl = new Repl(port);
        repl.preLogin();

    }
}