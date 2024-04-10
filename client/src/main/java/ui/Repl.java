package ui;

import chess.*;
import model.ListGamesResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements GameHandler {
    private ServerFacade facade;
    private String username;
    private int port;
    private ChessGame currentGame;
    private ChessGame.TeamColor teamColor;

    public Repl(int port) {
        this.facade = new ServerFacade(port);
        this.username = "Logged Out";
        this.port = port;
    }

    public void updateGame(ChessGame chessGame) {
        this.currentGame = chessGame;
        System.out.print("\n");
        printBoard(chessGame.getBoard(), null);
        System.out.print("[" + this.username + "] >>> ");
    }

    public void printMessage(String message) {
        System.out.println(message);
        System.out.print("[" + this.username + "] >>> ");
    }
    public void preLogin() throws Exception {
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

    public void postLogin() throws Exception {
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
                if (userInput[1].equals("white")) {teamColor = ChessGame.TeamColor.WHITE;}
                else if (userInput[1].equals("black")) {teamColor = ChessGame.TeamColor.BLACK;}

                try {
                    int gameID = Integer.parseInt(userInput[2]);
                    String authToken = facade.getAuthToken();
                    facade.joinGame(teamColor, gameID);
                    System.out.println("joining game");
                    gamePlay(gameID, authToken);
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
                    int gameID = Integer.parseInt(userInput[1]);
                    String authToken = facade.getAuthToken();
                    facade.joinGame(null, gameID);
                    System.out.println("observing game");
                    teamColor = null;
                    gamePlay(gameID, authToken);

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

    public void gamePlay(int gameID, String authToken) throws Exception {
        WebSocketFacade webSocketFacade = new WebSocketFacade(this.port, this);
        if (teamColor == null) {
            webSocketFacade.joinObserver(authToken, gameID);
            teamColor = ChessGame.TeamColor.WHITE;
        }
        else {
            webSocketFacade.joinPlayer(authToken, gameID, teamColor);
        }

        while (true) {
            System.out.print("[" + this.username + "] >>> ");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            var userInput = line.split(" ");

            if (userInput[0].equals("help")) {
                System.out.println("\nCommands\n" +
                        ">>> redraw\n" +
                        ">>> leave\n" +
                        ">>> move <row> <col> <row> <col>\n" +
                        ">>> resign\n" +
                        ">>> highlight <row> <col>\n");
            }

            else if (userInput[0].equals("redraw")) {
                printBoard(currentGame.getBoard(), null);
            }

            else if (userInput[0].equals("leave")) {
                webSocketFacade.leave(authToken, gameID);
                return;
            }

            else if (userInput[0].equals("move")) {
                if (currentGame.getTeamTurn() != teamColor) {
                    System.out.println("It is not your turn");
                }
                else {
                    ChessPosition startPos = new ChessPosition(Integer.parseInt(userInput[1]), Integer.parseInt(userInput[2]));
                    ChessPosition endPos = new ChessPosition(Integer.parseInt(userInput[3]), Integer.parseInt(userInput[4]));
                    ChessMove move = new ChessMove(startPos, endPos, null);
                    webSocketFacade.makeMove(authToken, gameID, move);
                }
            }
            else if (userInput[0].equals("resign")) {
                webSocketFacade.resign(authToken, gameID);
            }

            else if (userInput[0].equals("highlight")) {
                ChessPosition piecePos = new ChessPosition(Integer.parseInt(userInput[1]), Integer.parseInt(userInput[2]));
                printBoard(currentGame.getBoard(), piecePos);
                // Collection<ChessMove> validMoves = currentGame.validMoves(piecePos);
            }
        }
    }

    public void printBoard(ChessBoard chessBoard, ChessPosition position) {
        if (teamColor == ChessGame.TeamColor.WHITE) {
            printBoardWhite(chessBoard, position);
        }
        else if (teamColor == ChessGame.TeamColor.BLACK) {
            printBoardBlack(chessBoard, position);
        }
    }

    public void printBoardBlack(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = null;
        if (position != null) {
             validMoves = currentGame.validMoves(position);
        }
        for (int i = 1; i <= 8; i += 1) {
            for (int j = 8; j >= 1; j -= 1) {
                if (validMoves != null) {
                    if (validMoves.contains(new ChessMove(position, new ChessPosition(i, j), null))) {
                        if ((i + j) % 2 == 0) {
                            System.out.print("\u001b[39;42;1m");
                        }
                        else {
                            System.out.print("\u001b[39;102;1m");
                        }
                    }
                    else {
                        if ((i + j) % 2 == 0) {
                            System.out.print("\u001b[39;44;1m");
                        } else {
                            System.out.print("\u001b[39;41;1m");
                        }
                    }
                }
                else {
                    if ((i + j) % 2 == 0) {
                        System.out.print("\u001b[39;44;1m");
                    } else {
                        System.out.print("\u001b[39;41;1m");
                    }
                }
                printPiece(board.getPiece(new ChessPosition(i ,j)));
            }
            System.out.print("\u001b[39;49;1m\n");
        }
    }

    public void printBoardWhite(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = null;
        if (position != null) {
            validMoves = currentGame.validMoves(position);
        }
        for (int i = 8; i >= 1; i -= 1) {
            for (int j = 1; j <= 8; j += 1) {
                if (validMoves != null) {
                    if (validMoves.contains(new ChessMove(position, new ChessPosition(i, j), null))) {
                        if ((i + j) % 2 == 0) {
                            System.out.print("\u001b[39;42;1m");
                        }
                        else {
                            System.out.print("\u001b[39;102;1m");
                        }
                    }
                    else {
                        if ((i + j) % 2 == 0) {
                            System.out.print("\u001b[39;44;1m");
                        } else {
                            System.out.print("\u001b[39;41;1m");
                        }
                    }
                }
                else {
                    if ((i + j) % 2 == 0) {
                        System.out.print("\u001b[39;44;1m");
                    } else {
                        System.out.print("\u001b[39;41;1m");
                    }
                }
                printPiece(board.getPiece(new ChessPosition(i ,j)));
            }
            System.out.print("\u001b[39;49;1m\n");
        }
    }

    public void printPiece(chess.ChessPiece piece) {
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
