package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import dataAccess.Exceptions.ClientExceptionWrapper;
import model.ListGamesResponse;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements GameHandler {
    private ServerFacade facade;
    private String username;

    private int port;
    public Repl(int port) {
        this.facade = new ServerFacade(port);
        this.username = "Logged Out";
        this.port = port;
    }

    public void updateGame(ChessGame chessGame) {
        System.out.println("imaginary chess game");
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
    public void preLogin() {
        while (true) {
            System.out.print("[" + this.username + "] >>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var userInput = line.split(" ");

            if (userInput[0].equals("help")) {
                System.out.println("\nCommands\n" +
                        ">>> register <username> <password> <email>\n" +
                        ">>> login <username> <password>\n" +
                        ">>> quit\n");
            }

            else if (userInput[0].equals("register")) {
                try {
                    this.facade.register(userInput[1], userInput[2], userInput[3]);
                    this.username = userInput[1];
                    System.out.println(" ");
                    postLogin();
                } catch (ClientExceptionWrapper e) {
                    System.out.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Inputs not sufficient for Register");
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            else if (userInput[0].equals("login")) {
                try {
                    this.facade.login(userInput[1], userInput[2]);
                    this.username = userInput[1];
                    System.out.println(" ");
                    postLogin();
                } catch (ClientExceptionWrapper e) {
                    System.out.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Inputs not sufficient for Register");
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            else if (userInput[0].equals("quit")) {
                System.out.println("-- Terminating Program -- \n Goodbye!");
                break;
            }
        }
    }

    public void postLogin() {
        while (true) {
            System.out.print("[" + this.username + "] >>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var userInput = line.split(" ");

            if (userInput[0].equals("help")) {
                System.out.println("\nCommands\n" +
                        ">>> create game <game name>\n" +
                        ">>> join <black / white> <game ID>\n" +
                        ">>> list games\n" +
                        ">>> observe <game ID>\n" +
                        ">>> logout\n");
            }

            else if (userInput[0].equals("create") && userInput[1].equals("game")) {
                try {
                    int gameID = facade.createGame(userInput[2]);
                    System.out.println("New Game created with ID: " + String.valueOf(gameID));
                } catch (ClientExceptionWrapper e) {
                    System.out.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Inputs not sufficient for Creating a game");
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            else if (userInput[0].equals("join")) {
                ChessGame.TeamColor teamColor = null;
                if (userInput[1].equals("white")) {teamColor = ChessGame.TeamColor.WHITE;}
                else if (userInput[1].equals("black")) {teamColor = ChessGame.TeamColor.BLACK;}

                try {
                    facade.joinGame(teamColor, Integer.parseInt(userInput[2]));
                    System.out.println("joining game");
                    gamePlay(teamColor);
                } catch (ClientExceptionWrapper e) {
                    System.out.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Inputs not sufficient for joining a game");
                } catch (NumberFormatException e) {
                    System.out.println("Game ID field needs to be an integer");
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            else if (userInput[0].equals("observe")) {
                try {
                    facade.joinGame(null, Integer.parseInt(userInput[1]));
                    System.out.println("observing game");
                    gamePlay(ChessGame.TeamColor.WHITE);

                } catch (ClientExceptionWrapper e) {
                    System.out.println(e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Inputs not sufficient for observing a game");
                } catch (NumberFormatException e) {
                    System.out.println("Game ID field needs to be an integer");
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            else if (userInput[0].equals("logout")) {
                try {
                    facade.logout();
                    this.username = "Logged Out";
                    System.out.println(" ");
                    preLogin();
                } catch (ClientExceptionWrapper e) {
                    System.out.println(e.getMessage());
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (userInput[0].equals("list") && userInput[1].equals("games")) {
                try {
                    Collection<ListGamesResponse> gamesList = facade.listGames();
                    for (ListGamesResponse gameResp : gamesList) {
                        System.out.println("#" + String.valueOf(gameResp.gameID()) + " " + gameResp.gameName() + "; WhiteUser: " +  gameResp.whiteUsername()
                        + "; BlackUser: " + gameResp.blackUsername());
                    }
                } catch (ClientExceptionWrapper e) {
                    System.out.println(e.getMessage());
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void gamePlay(ChessGame.TeamColor color) {
        try {
            WebSocketFacade webSocketFacade = new WebSocketFacade(this.port, this);
//            try {
//                webSocketFacade.joinPlayer(facade.);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        if (color == ChessGame.TeamColor.WHITE) {
            printBoardWhite(new ChessBoard());
        }
        else {
            printBoardBlack(new ChessBoard());
        }
    }

    public static void printBoardBlack(ChessBoard board) {
        board.resetBoard();
        for (int i = 8; i >= 1; i -= 1) {
            for (int j = 1; j <= 8; j += 1) {
                if ((i + j) % 2 == 0) {
                    System.out.print("\u001b[39;44;1m");
                }
                else {
                    System.out.print("\u001b[39;41;1m");
                }
                printPiece(board.getPiece(new ChessPosition(i ,j)));
            }
            System.out.print("\u001b[39;49;1m\n");
        }
        System.out.print("\u001b[39;49;1m\n");
        for (int i = 1; i <= 8; i += 1) {
            for (int j = 8; j >= 1; j -= 1) {
                if ((i + j) % 2 == 0) {
                    System.out.print("\u001b[39;44;1m");
                }
                else {
                    System.out.print("\u001b[39;41;1m");
                }
                printPiece(board.getPiece(new ChessPosition(i ,j)));
            }
            System.out.print("\u001b[39;49;1m\n");
        }
    }

    public static void printBoardWhite(ChessBoard board) {
        board.resetBoard();
        for (int i = 1; i <= 8; i += 1) {
            for (int j = 8; j >= 1; j -= 1) {
                if ((i + j) % 2 == 0) {
                    System.out.print("\u001b[39;44;1m");
                }
                else {
                    System.out.print("\u001b[39;41;1m");
                }
                printPiece(board.getPiece(new ChessPosition(i ,j)));
            }
            System.out.print("\u001b[39;49;1m\n");
        }
        System.out.print("\u001b[39;49;1m\n");
        for (int i = 8; i >= 1; i -= 1) {
            for (int j = 1; j <= 8; j += 1) {
                if ((i + j) % 2 == 0) {
                    System.out.print("\u001b[39;44;1m");
                }
                else {
                    System.out.print("\u001b[39;41;1m");
                }
                printPiece(board.getPiece(new ChessPosition(i ,j)));
            }
            System.out.print("\u001b[39;49;1m\n");
        }
    }

    public static void printPiece(chess.ChessPiece piece) {
        if (piece == null) {
            System.out.print(EMPTY);
        }
        else {
            ChessPiece.PieceType type = piece.getPieceType();
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (type == ChessPiece.PieceType.PAWN) {
                    System.out.print("\u001b[97m" + BLACK_PAWN);
                }
                else if (type == ChessPiece.PieceType.ROOK) {
                    System.out.print("\u001b[97m" + BLACK_ROOK);
                }
                else if (type == ChessPiece.PieceType.BISHOP) {
                    System.out.print("\u001b[97m" + BLACK_BISHOP);
                }
                else if (type == ChessPiece.PieceType.KNIGHT) {
                    System.out.print("\u001b[97m" + BLACK_KNIGHT);
                }
                else if (type == ChessPiece.PieceType.QUEEN) {
                    System.out.print("\u001b[97m" + BLACK_QUEEN);
                }
                else if (type == ChessPiece.PieceType.KING) {
                    System.out.print("\u001b[97m" + BLACK_KING);
                }
            }

            else if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (type == ChessPiece.PieceType.PAWN) {
                    System.out.print("\u001b[30m" + BLACK_PAWN);
                }
                else if (type == ChessPiece.PieceType.ROOK) {
                    System.out.print("\u001b[30m" + BLACK_ROOK);
                }
                else if (type == ChessPiece.PieceType.BISHOP) {
                    System.out.print("\u001b[30m" + BLACK_BISHOP);
                }
                else if (type == ChessPiece.PieceType.KNIGHT) {
                    System.out.print("\u001b[30m" + BLACK_KNIGHT);
                }
                else if (type == ChessPiece.PieceType.QUEEN) {
                    System.out.print("\u001b[30m" + BLACK_QUEEN);
                }
                else if (type == ChessPiece.PieceType.KING) {
                    System.out.print("\u001b[30m" + BLACK_KING);
                }
            }
        }
    }
}
