package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import org.example.common.Pos;
import org.example.common.TileKind;
import org.example.common.Dir;
import org.example.common.TileToUpdate;

public class RandomTestcase {

    public static class RandomMoveResult {

        public final Pos expectedPos;
        public final List<Dir> dirs;
        public final List<TileToUpdate> expectedTilesToUpdate;
        public final List<Boolean> foodTimingList;

        public RandomMoveResult(Pos expectedPos, List<Dir> dirs, List<TileToUpdate> expectedTilesToUpdate,
                List<Boolean> foodTimingList) {
            this.expectedPos = expectedPos;
            this.dirs = dirs;
            this.expectedTilesToUpdate = expectedTilesToUpdate;
            this.foodTimingList = foodTimingList;
        }
    }

    public static Random random = new Random();

    public static <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
        int x = random.nextInt(enumClass.getEnumConstants().length);
        return enumClass.getEnumConstants()[x];
    }

    public static RandomMoveResult randomMove(int count, Pos startPos, Predicate<Random> foodPredicate) {
        List<Dir> dirs = new ArrayList<>(count);
        List<TileToUpdate> expectedTilesToUpdate = new ArrayList<>(count);
        List<Boolean> foodTimingList = new ArrayList<>(count);

        LinkedList<Pos> tails = new LinkedList<>();
        Pos curPos = startPos;
        tails.add(curPos);
        for (int i = 0; i < count; i++) {
            Dir dir = randomEnum(Dir.class);
            dirs.add(dir);

            Pos nextPos = curPos.add(dir.getPos());
            Pos tailPos = tails.getLast();

            expectedTilesToUpdate.add(new TileToUpdate(curPos, TileKind.tail, dir));

            boolean isEating = foodPredicate.test(random);
            foodTimingList.add(isEating);
            if (!isEating) {
                expectedTilesToUpdate.add(new TileToUpdate(tailPos, TileKind.ground));
                tails.removeLast();
            }

            expectedTilesToUpdate.add(new TileToUpdate(nextPos, TileKind.player, dir));

            curPos = nextPos;
            tails.addFirst(curPos);
        }

        return new RandomMoveResult(curPos, dirs, expectedTilesToUpdate, foodTimingList);
    }
}
