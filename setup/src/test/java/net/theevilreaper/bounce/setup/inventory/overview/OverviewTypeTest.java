package net.theevilreaper.bounce.setup.inventory.overview;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class OverviewTypeTest {

    @ParameterizedTest(name = "Test item get for type: {0}")
    @EnumSource(OverviewType.class)
    void testItemGet(@NotNull OverviewType type) {
        ItemStack item = type.getItem();
        assertNotNull(item, "Item should not be null for type: " + type);
        assertEquals(type.getMaterial(), item.material(), "Material should match for type: " + type);

        assertTrue(item.has(ItemComponent.CUSTOM_NAME), "Item should have a name component for type: " + type);

        Component nameComponent = item.get(ItemComponent.CUSTOM_NAME);
        assertNotNull(nameComponent, "Custom name component should not be null for type: " + type);

        String name = PlainTextComponentSerializer.plainText().serialize(nameComponent);
        assertTrue(name.contains(type.getName()), "Name should match for type: " + type);
    }
}