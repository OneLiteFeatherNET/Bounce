package net.theevilreaper.bounce.setup.preview;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.metadata.other.FallingBlockMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.theevilreaper.bounce.common.push.PushEntry;
import net.theevilreaper.bounce.common.push.PushPhysics;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * The {@link PushPreviewEntity} is a falling block which bounces the same way as a player on a push block.
 * It allows a setup player to see how high a push value lifts a player without starting a game.
 * <p>
 * The movement is simulated by the entity itself instead of the Minestom physics, because the client
 * predicts falling blocks with a different gravity than players which would let the preview jitter.
 * The entity is only visible to the player which requested the preview and removes itself after the last bounce.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 0.10.0
 */
public final class PushPreviewEntity extends Entity {

    /**
     * The amount of bounces each preview performs before it is removed.
     */
    public static final int BOUNCES = 3;

    private static final int SPAWN_DISTANCE = 4;
    private static final int SPACING = 2;

    private final Player viewer;
    private final UUID viewerId;
    private final double launchVelocity;
    private double velocityPerTick;
    private int remainingBounces;
    private double startY;

    /**
     * Creates a new preview entity for the given player.
     *
     * @param viewer    the player which is allowed to see the preview
     * @param block     the block which should be displayed
     * @param pushValue the push value which should be previewed
     */
    public PushPreviewEntity(Player viewer, Block block, int pushValue) {
        super(EntityType.FALLING_BLOCK);
        this.viewer = viewer;
        this.viewerId = viewer.getUuid();
        this.launchVelocity = PushPhysics.velocityPerTick(pushValue);
        this.velocityPerTick = this.launchVelocity;
        this.remainingBounces = BOUNCES;
        editEntityMeta(FallingBlockMeta.class, meta -> meta.setBlock(block));
        setNoGravity(true);
        set(DataComponents.CUSTOM_NAME, Component.text(
                String.format(Locale.ROOT, "%d: ~%.1f blocks", pushValue, PushPhysics.peakHeight(pushValue)),
                NamedTextColor.YELLOW
        ));
        setCustomNameVisible(true);
        updateViewableRule(player -> player.getUuid().equals(this.viewerId));
    }

    /**
     * Checks if the given player still has a preview which is bouncing.
     *
     * @param viewer the player to check
     * @return true if a preview of the player is still running
     */
    public static boolean isRunning(Player viewer) {
        Instance instance = viewer.getInstance();
        if (instance == null) return false;
        UUID viewerId = viewer.getUuid();
        return instance.getEntities().stream()
                .anyMatch(entity -> entity instanceof PushPreviewEntity preview && preview.viewerId.equals(viewerId));
    }

    /**
     * Spawns a preview for each given entry in a row in front of the viewer at the height of the viewer's feet.
     *
     * @param viewer  the player which requested the preview
     * @param entries the entries which should be previewed
     */
    public static void launchAll(Player viewer, List<PushEntry> entries) {
        Instance instance = viewer.getInstance();
        if (instance == null || entries.isEmpty()) return;

        Pos position = viewer.getPosition();
        Vec forward = position.withPitch(0).direction();
        Vec side = new Vec(-forward.z(), 0, forward.x());
        Vec center = position.asVec().add(forward.mul(SPAWN_DISTANCE));
        double offset = (entries.size() - 1) * SPACING / 2D;

        for (int i = 0; i < entries.size(); i++) {
            PushEntry entry = entries.get(i);
            Vec point = center.add(side.mul(i * SPACING - offset));
            Pos spawn = new Pos(Math.floor(point.x()) + 0.5, Math.floor(position.y()), Math.floor(point.z()) + 0.5);
            new PushPreviewEntity(viewer, entry.getBlock(), entry.getValue()).launch(instance, spawn);
        }
    }

    /**
     * Spawns the preview at the given position.
     *
     * @param instance the instance to spawn the preview in
     * @param spawn    the position where the preview starts to bounce
     */
    public void launch(Instance instance, Pos spawn) {
        this.startY = spawn.y();
        setInstance(instance, spawn);
    }

    @Override
    public void update(long time) {
        if (!this.viewer.isOnline()) {
            remove();
            return;
        }

        Pos position = getPosition();
        double nextY = position.y() + this.velocityPerTick;

        if (this.velocityPerTick < 0 && nextY <= this.startY) {
            teleport(position.withY(this.startY));
            this.remainingBounces--;
            if (this.remainingBounces <= 0 || this.launchVelocity <= 0) {
                remove();
                return;
            }
            this.velocityPerTick = this.launchVelocity;
            return;
        }

        teleport(position.withY(nextY));
        this.velocityPerTick = PushPhysics.nextVelocityPerTick(this.velocityPerTick);
    }
}
