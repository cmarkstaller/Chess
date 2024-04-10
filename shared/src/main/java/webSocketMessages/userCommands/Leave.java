package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    private int gameID;

    public Leave(String authToken, int gameID) {
        super(authToken);
        this.commandType = UserGameCommand.CommandType.LEAVE;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}