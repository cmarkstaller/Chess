package model;

import java.util.Collection;

public record GameListResponseMessage (Collection<ListGamesResponse> games) {}

