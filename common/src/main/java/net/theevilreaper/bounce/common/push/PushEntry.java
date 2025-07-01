package net.theevilreaper.bounce.common.push;

import net.minestom.server.instance.block.Block;

import java.util.Objects;

public class PushEntry {

    private Block block;
    private int value;

    public PushEntry(Block block, int value) {
        this.block = block;
        this.value = value;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PushEntry pushEntry)) return false;
        return value == pushEntry.value && Objects.equals(block, pushEntry.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(block, value);
    }
}
