package dev.ragdoll.mobragdolls.physics;

/** Small mutable-free vector type used by the ragdoll solver. */
public record Vec3d(double x, double y, double z) {
    public static final Vec3d ZERO = new Vec3d(0, 0, 0);

    public Vec3d add(Vec3d other) {
        return new Vec3d(x + other.x, y + other.y, z + other.z);
    }

    public Vec3d subtract(Vec3d other) {
        return new Vec3d(x - other.x, y - other.y, z - other.z);
    }

    public Vec3d multiply(double scalar) {
        return new Vec3d(x * scalar, y * scalar, z * scalar);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3d normalized() {
        double length = length();
        return length < 1.0e-8 ? ZERO : multiply(1.0 / length);
    }

    public double distanceTo(Vec3d other) {
        return subtract(other).length();
    }
}
