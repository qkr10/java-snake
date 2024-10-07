package org.example.game.player;

import static org.example.Equal.*;
import static org.example.RandomTestcase.*;
import static org.example.ToString.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import org.example.common.Pos;
import org.example.common.Dir;
import org.example.common.TileToUpdate;
import org.junit.jupiter.api.*;

class PlayerTest {

    @RepeatedTest(10)
    void playerTest() {
        //given
        final int moveCount = random.nextInt(5, 100);
        final Pos pos = new Pos(random.nextInt(99), random.nextInt(99));
        final Predicate<Random> foodPredicate = r -> r.nextInt(4) == 1;

        final RandomMoveResult randomMoveResult = randomMove(moveCount, pos, foodPredicate);
        final Player player = new Player(Dir.Right, pos);
        final List<TileToUpdate> tileUpdateQueue = new ArrayList<>();

        //when
        for (int i = 0; i < moveCount; i++) {
            Dir dir = randomMoveResult.dirs.get(i);

            player.rotate(dir);
            if (randomMoveResult.foodTimingList.get(i)) {
                player.eat();
            }
            tileUpdateQueue.addAll(player.move());
        }

        //then
        var expectedQueue = randomMoveResult.expectedTilesToUpdate;
        var isQueueCorrect = true;
        for (int i = 0; i < moveCount; i++) {
            var expected = expectedQueue.get(i);
            var actual = tileUpdateQueue.get(i);
            isQueueCorrect = isQueueCorrect && tileToUpdateEquals.equals(actual, expected);
        }

        var lastMoveActual = tileToUpdate2String(tileUpdateQueue.get(tileUpdateQueue.size() - 1));
        var lastMoveExpected = tileToUpdate2String(expectedQueue.get(expectedQueue.size() - 1));
        var msg = "isQueueCorrect: %b\n".formatted(isQueueCorrect);
        msg += "last move actual: %s\nlast move expected: %s\n".formatted(lastMoveActual, lastMoveExpected);
        msg += "actual queue size: %d, expected queue size: %d".formatted(tileUpdateQueue.size(), expectedQueue.size());
        assertTrue(isQueueCorrect, msg);
    }
}