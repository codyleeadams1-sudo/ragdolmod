package dev.ragdoll.mobragdolls.physics;

/** Verlet-integrated particle representing one ragdoll body segment anchor. */
public final class BodyPart {
    private Vec3d position;
    private Vec3d previousPosition;
    private final double radius;
    private final double inverseMass;

    public BodyPart(Vec3d position, double radius, double mass) {
        this.position = position;
        this.previousPosition = position;
        this.radius = radius;
        this.inverseMass = mass <= 0 ? 0 : 1.0 / mass;
    }

    public void integrate(double gravity, double damping) {
        Vec3d velocity = position.subtract(previousPosition).multiply(damping);
        previousPosition = position;
        position = position.add(velocity).add(new Vec3d(0, -gravity, 0));
    }

    public void translate(Vec3d delta) {
        if (inverseMass == 0) return;
        position = position.add(delta);
        previousPosition = previousPosition.add(delta);
    }

    public void addImpulse(Vec3d impulse) {
        if (inverseMass == 0) return;
        previousPosition = previousPosition.subtract(impulse.multiply(inverseMass));
    }

    public Vec3d position() { return position; }
    public double radius() { return radius; }
}
