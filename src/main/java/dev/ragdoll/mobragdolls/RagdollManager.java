package dev.ragdoll.mobragdolls;

import dev.ragdoll.mobragdolls.physics.Ragdoll;
import dev.ragdoll.mobragdolls.physics.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.*;

/**
 * Client-only lifecycle manager. This prototype tracks recently-dead living entities
 * and simulates their articulated physics state. A renderer/model adapter is a separate
 * integration layer and is not yet included in this first build.
 */
public final class RagdollManager {
    private static final Map<Integer, Ragdoll> RAGDOLLS = new LinkedHashMap<>();
    private static final Set<Integer> SEEN_DEATHS = new HashSet<>();
    private static final int MAX_RAGDOLLS = 32;
    private static final int MAX_AGE_TICKS = 20 * 20;
    private static final double GRAVITY = 0.018;
    private static final double DAMPING = 0.985;

    private RagdollManager() {}

    public static void tick(Minecraft client) {
        ClientLevel level = client.level;
        if (level == null) {
            RAGDOLLS.clear();
            SEEN_DEATHS.clear();
            return;
        }

        for (Entity entity : level.entitiesForRendering()) {
            if (!(entity instanceof LivingEntity living)) continue;
            if (living.getHealth() > 0.0F || living.isRemoved()) continue;
            if (SEEN_DEATHS.add(living.getId())) {
                double height = Math.max(0.6, Math.min(3.0, living.getBbHeight()));
                Ragdoll ragdoll = new Ragdoll(living.getId(),
                        new Vec3d(living.getX(), living.getY(), living.getZ()), height / 1.8);
                RAGDOLLS.put(living.getId(), ragdoll);
                while (RAGDOLLS.size() > MAX_RAGDOLLS) {
                    Integer first = RAGDOLLS.keySet().iterator().next();
                    RAGDOLLS.remove(first);
                }
            }
        }

        if (client.player != null) {
            AABB playerBox = client.player.getBoundingBox().inflate(0.18);
            for (Ragdoll ragdoll : RAGDOLLS.values()) {
                if (playerBox.contains(ragdoll.center().x(), ragdoll.center().y(), ragdoll.center().z())) {
                    double dx = ragdoll.center().x() - client.player.getX();
                    double dz = ragdoll.center().z() - client.player.getZ();
                    Vec3d push = new Vec3d(dx, 0.025, dz).normalized().multiply(0.025);
                    ragdoll.push(push);
                }
            }
        }

        Iterator<Ragdoll> iterator = RAGDOLLS.values().iterator();
        while (iterator.hasNext()) {
            Ragdoll ragdoll = iterator.next();
            ragdoll.tick(GRAVITY, DAMPING);
            if (ragdoll.age() > MAX_AGE_TICKS) iterator.remove();
        }

        // Bound the death-id cache as entities leave the render distance.
        if (SEEN_DEATHS.size() > 2048) {
            SEEN_DEATHS.clear();
        }
    }

    public static Collection<Ragdoll> getRagdolls() {
        return Collections.unmodifiableCollection(RAGDOLLS.values());
    }
}
