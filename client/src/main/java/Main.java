import chess.*;
import server.Server;
import ui.ServerFacade;

public class Main {
    public static void main(String[] args) {
        Server myServer = new Server();
        myServer.run(8080);
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        ServerFacade facade = new ServerFacade(8080);
        try {
            System.out.println(facade.register("username", "mypassword", "myemail"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}