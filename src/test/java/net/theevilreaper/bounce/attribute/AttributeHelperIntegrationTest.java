package net.theevilreaper.bounce.attribute;

import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MicrotusExtension.class)
class AttributeHelperIntegrationTest {

    @Test
    void testJumpStrengthAttributeChange(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);

        AttributeHelper.disableJumpStrength(player);

        assertEquals(0, player.getAttribute(Attribute.JUMP_STRENGTH).getBaseValue(),
                "Jump strength should be set to zero after disabling");

        AttributeHelper.resetJumpStrength(player);

        assertEquals(0.41999998688697815, player.getAttribute(Attribute.JUMP_STRENGTH).getBaseValue(),
                "Jump strength should be reset to default value");

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should be empty after destruction");
    }

    @Test
    void testGravityFlipAttributeChange(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        double defaultGravity = player.getAttributeValue(Attribute.GRAVITY);

        AttributeHelper.flipGravity(player);
        AttributeHelper.flipGravity(player);

        assertEquals(defaultGravity - 0.1, player.getAttributeValue(Attribute.GRAVITY), 1e-9,
                "Flipping the gravity twice must not stack the modifier");

        AttributeHelper.resetGravity(player);

        assertEquals(defaultGravity, player.getAttributeValue(Attribute.GRAVITY),
                "Gravity should be reset to default value");

        env.destroyInstance(instance, true);
    }
}
