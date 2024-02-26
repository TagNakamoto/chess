package dataAccess;

import model.GameData;

import java.util.HashSet;

public record GamesList(HashSet<GameData> games) {
}
