package dev.ragdoll.mobragdolls.physics;

import java.util.ArrayList;
import java.util.List;

/**
 * Prototype articulated ragdoll. Segments are connected by distance constraints.
 * Rendering/vanilla-model replacement is intentionally kept separate from physics.
 */
public final class Ragdoll {
    public enum Part { PELVIS, CHEST, HEAD, LEFT_ARM, RIGHT_ARM, LEFT_LEG, RIGHT_LEG }

    private final int entityId;
    private final Part[] partOrder = Part.values();
    private final BodyPart[] parts = new BodyPart[partOrder.length];
    private final List<DistanceConstraint> joints = new ArrayList<>();
    private int age;

    public Ragdoll(int entityId, Vec3d origin, double scale) {
        this.entityId = entityId;
        double s = Math.max(0.35, Math.min(2.5, scale));
        parts[Part.PELVIS.ordinal()] = body(origin.add(new Vec3d(0, 0.55*s, 0)), 0.23*s, 3.0*s);
        parts[Part.CHEST.ordinal()] = body(origin.add(new Vec3d(0, 1.05*s, 0)), 0.25*s, 2.4*s);
        parts[Part.HEAD.ordinal()] = body(origin.add(new Vec3d(0, 1.48*s, 0)), 0.19*s, 1.0*s);
        parts[Part.LEFT_ARM.ordinal()] = body(origin.add(new Vec3d(-0.36*s, 1.00*s, 0)), 0.13*s, 0.8*s);
        parts[Part.RIGHT_ARM.ordinal()] = body(origin.add(new Vec3d(0.36*s, 1.00*s, 0)), 0.13*s, 0.8*s);
        parts[Part.LEFT_LEG.ordinal()] = body(origin.add(new Vec3d(-0.14*s, 0.20*s, 0)), 0.15*s, 1.2*s);
        parts[Part.RIGHT_LEG.ordinal()] = body(origin.add(new Vec3d(0.14*s, 0.20*s, 0)), 0.15*s, 1.2*s);

        joint(Part.PELVIS, Part.CHEST, 0.50*s, 0.72);
        joint(Part.CHEST, Part.HEAD, 0.43*s, 0.68);
        joint(Part.CHEST, Part.LEFT_ARM, 0.42*s, 0.48);
        joint(Part.CHEST, Part.RIGHT_ARM, 0.42*s, 0.48);
        joint(Part.PELVIS, Part.LEFT_LEG, 0.43*s, 0.52);
        joint(Part.PELVIS, Part.RIGHT_LEG, 0.43*s, 0.52);
    }

    private BodyPart body(Vec3d p, double radius, double mass) {
        return new BodyPart(p, radius, mass);
    }

    private void joint(Part a, Part b, double length, double stiffness) {
        joints.add(new DistanceConstraint(parts[a.ordinal()], parts[b.ordinal()], length, stiffness));
    }

    public void tick(double gravity, double damping) {
        age++;
        for (BodyPart part : parts) part.integrate(gravity, damping);
        // Iterative constraint solving reduces visible stretching.
        for (int i = 0; i < 6; i++) for (DistanceConstraint joint : joints) joint.solve();
    }

    public void push(Vec3d impulse) {
        for (BodyPart part : parts) part.addImpulse(impulse);
    }

    public int entityId() { return entityId; }
    public int age() { return age; }
    public BodyPart[] parts() { return parts.clone(); }
    public Vec3d center() {
        Vec3d sum = Vec3d.ZERO;
        for (BodyPart part : parts) sum = sum.add(part.position());
        return sum.multiply(1.0 / parts.length);
    }
}
