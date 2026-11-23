package dev.ragdoll.mobragdolls;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class MobRagdollsClient implements ClientModInitializer {
    public static final String MOD_ID = "mob_ragdolls";
    private static KeyMapping toggleKey;
    private static boolean enabled = true;

    @Override
    public void onInitializeClient() {
        KeyMapping.Category category = KeyMapping.Category.register(
                Identifier.fromNamespaceAndPath(MOD_ID, "main")
        );

        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.mob_ragdolls.toggle",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                enabled = !enabled;
                if (client.player != null) {
                    client.player.displayClientMessage(
                            Component.literal("Mob Ragdolls: " + (enabled ? "ON" : "OFF")),
                            true
                    );
                }
            }

            if (enabled) {
                RagdollManager.tick(client);
            }
        });
    }

    public static boolean isEnabled() {
        return enabled;
    }
}
