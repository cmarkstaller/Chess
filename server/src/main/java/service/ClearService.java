package service;

import dataAccess.AuthDao;
import dataAccess.Exceptions.DataAccessException;
import dataAccess.GameDao;
import dataAccess.UserDao;

public class ClearService {
    private final GameDao game;
    private final UserDao user;
    private final AuthDao auth;

    public ClearService(AuthDao auth, GameDao game, UserDao user) {
        this.auth = auth;
        this.game = game;
        this.user = user;
    }

    public void clear() throws DataAccessException {
        this.auth.clear();
        this.game.clear();
        this.user.clear();
    }
}
