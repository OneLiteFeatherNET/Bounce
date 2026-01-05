package net.theevilreaper.bounce.block.type.lantern;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.Contract;

public interface LanternBlockFactory {

    /**
     * Creates a new {@link LanternBlockHandler} for the specified block type.
     *
     * @param type the type of block to create a handler for
     * @return a new {@link LanternBlockHandler} for the specified block type
     */
    @Contract("_ -> new")
    static LanternBlockHandler create(Type type) {
        return new LanternBlockHandler(type.getBlock());
    }

    enum Type {

        LANTERN(Block.LANTERN),
        SOUL_LANTERN(Block.SOUL_LANTERN);

        private final Block block;

        /**
         * Constructs a new enumeration entry with the specified block.
         *
         * @param block the block associated with this type
         */
        Type(Block block) {
            this.block = block;
        }

        /**
         * Gets the block associated with this type.
         *
         * @return the block
         */
        public Block getBlock() {
            return this.block;
        }
    }
}
