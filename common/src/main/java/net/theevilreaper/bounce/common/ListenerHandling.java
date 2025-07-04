package net.theevilreaper.bounce.common;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerSwapItemEvent;
import net.theevilreaper.aves.inventory.util.InventoryConstants;
import org.jetbrains.annotations.NotNull;

/**
 * The interface provides a default method to register some listeners to cancel specific events.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ListenerHandling {

    /**
     * Registers some {@link Event} listener to cancel specific default events.
     *
     * @param eventNode the event node to register the listeners
     */
    default void registerCancelListener(@NotNull EventNode<Event> eventNode) {
        eventNode.addListener(PlayerBlockBreakEvent.class, InventoryConstants.CANCELLABLE_EVENT::accept);
        eventNode.addListener(PlayerBlockPlaceEvent.class, InventoryConstants.CANCELLABLE_EVENT::accept);
        eventNode.addListener(ItemDropEvent.class, InventoryConstants.CANCELLABLE_EVENT::accept);
        eventNode.addListener(PlayerSwapItemEvent.class, InventoryConstants.CANCELLABLE_EVENT::accept);
        eventNode.addListener(PlayerBlockInteractEvent.class, InventoryConstants.CANCELLABLE_EVENT::accept);
    }
}
