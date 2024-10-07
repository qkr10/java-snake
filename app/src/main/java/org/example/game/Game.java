package org.example.game;

import java.util.List;
import java.util.Random;
import lombok.Getter;
import org.example.common.Dir;
import org.example.common.Pos;
import org.example.common.Tile;
import org.example.common.TileKind;
import org.example.common.TileToUpdate;
import org.example.game.player.Player;

public class Game implements Runnable {

    public static final int mapSize = 12;
    public final List<TileToUpdate> tileUpdateQueue;
    
    @Getter
    private Player player;
    private Tile[][] map;
    private final Random random;

    public Game(Random random, List<TileToUpdate> tileUpdateQueue) {
        this.random = random;
        this.tileUpdateQueue = tileUpdateQueue;

        initMap();
        initPlayer();
        initFood();
    }

    @Override
    public void run() {
        if (!playerMove()) {
            System.exit(0);
        }
    }

    private void initMap() {
        map = new Tile[mapSize][mapSize];
        for (int i = 0; i < mapSize-1; i++) {
            Pos[] temps = new Pos[]{
                    new Pos(i, 0),
                    new Pos(i+1, mapSize-1),
                    new Pos(0, i+1),
                    new Pos(mapSize-1, i)
            };

            for (Pos temp : temps) {
                map[temp.x][temp.y] = TileKind.wall.getTile();
                tileUpdateQueue.add(new TileToUpdate(temp, TileKind.wall));
            }
        }
        for (int x = 1; x < mapSize - 1; x++) {
            for (int y = 1; y < mapSize - 1; y++) {
                map[x][y] = TileKind.ground.getTile();
            }
        }
    }
    
    private void initPlayer() {
        final Pos firstPos = new Pos(mapSize/2);
        final Dir firstDir = Dir.Right;
        player = new Player(firstDir, firstPos);
        map[firstPos.x][firstPos.y] = TileKind.food.getTile();
        tileUpdateQueue.add(new TileToUpdate(firstPos, TileKind.food, firstDir));
    }

    // 게임 오버이면 false 반환
    // 게임 오버 조건 1. 새로운 음식을 생성할 공간이 없음. 2. 머리가 꼬리/벽에 부딪힘.
    private boolean playerMove() {
        var tileUpdates = player.move();
        tileUpdateQueue.addAll(tileUpdates);

        boolean isGameover = isTailHit(tileUpdates);
        boolean isEatingFood = isFoodHit();

        for (TileToUpdate tile : tileUpdates) {
            map[tile.pos.x][tile.pos.y] = new Tile(tile.kind, tile.dir);
        }

        if (isEatingFood) {
            player.eat();
            isGameover = isGameover || !initFood();
        }

        isGameover = isGameover || isWallHit();
        return !isGameover;
    }

    private boolean isWallHit() {
        boolean xMin = player.getPos().x == 0;
        boolean yMin = player.getPos().y == 0;
        boolean xMax = player.getPos().x == mapSize - 1;
        boolean yMax = player.getPos().y == mapSize - 1;
        return xMin || yMin || xMax || yMax;
    }

    private boolean isTailHit(List<TileToUpdate> tileUpdates) {
        Pos head = player.getPos();
        boolean result = map[head.x][head.y].tileKind == TileKind.tail;

        if (result) {
            for (TileToUpdate tile : tileUpdates) {
                boolean isLastTailAtTile = tile.kind == TileKind.ground;
                boolean isHeadAtTile = head.equals(tile.pos);
                if (isLastTailAtTile && isHeadAtTile) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    private boolean isFoodHit() {
        Pos head = player.getPos();
        return map[head.x][head.y].tileKind == TileKind.food;
    }

    // 새로운 음식을 생성할수 없으면 false
    private boolean initFood() {
        Pos newFoodPos = getNewFoodPos();
        if (newFoodPos == null)
            return false;

        map[newFoodPos.x][newFoodPos.y] = TileKind.food.getTile();
        tileUpdateQueue.add(new TileToUpdate(newFoodPos, TileKind.food));
        return true;
    }

    // 새로운 음식을 생성할수 없으면 null
    private Pos getNewFoodPos() {
        List<Pos> tails = player.getTails();
        int tailCount = tails.size();
        int availableSpace = (mapSize - 2) * (mapSize - 2) - tailCount;
        if (availableSpace <= 0) {
            return null;
        }

        int newFoodPosNum = random.nextInt(1, availableSpace);
        for (int x = 1; x < mapSize - 1; x++) {
            for (int y = 1; y < mapSize - 1; y++) {
                if (map[x][y].tileKind == TileKind.ground)
                    newFoodPosNum--;
                if (newFoodPosNum == 0)
                    return new Pos(x, y);
            }
        }
        return null;
    }
}
