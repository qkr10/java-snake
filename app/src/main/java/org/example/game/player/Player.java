package org.example.game.player;

import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import org.example.common.Pos;
import org.example.common.TileKind;
import org.example.common.Dir;
import org.example.common.TileToUpdate;

public class Player {

    @Getter
    private Dir dir;
    @Getter
    private LinkedList<Pos> tails = new LinkedList<>();

    public Player(Dir dir, Pos pos) {
        this.dir = dir;
        tails.add(new Pos(pos));
    }

    public List<TileToUpdate> move() {
        final Pos curPos = tails.getFirst();
        final Pos nextPos = curPos.add(dir.getPos());
        final Pos tailPos = tails.removeLast();
        tails.addFirst(nextPos);

        List<TileToUpdate> updates = new LinkedList<>();
        updates.add(new TileToUpdate(curPos, TileKind.tail, dir));
        if (tailPos != null) {
            updates.add(new TileToUpdate(tailPos, TileKind.ground));
        }
        updates.add(new TileToUpdate(nextPos, TileKind.player, dir));
        return updates;
    }

    public boolean isDirRedrawRequired = false;

    public void rotate(Dir dir) {
        this.dir = dir;
        isDirRedrawRequired = true;
    }

    public void eat() {
        tails.addLast(null);
    }

    public Pos getPos() {
        return tails.getFirst();
    }
}
