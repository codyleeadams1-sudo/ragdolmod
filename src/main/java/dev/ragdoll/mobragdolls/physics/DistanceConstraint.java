package dev.ragdoll.mobragdolls.physics;

/** Keeps two body anchors near their rest distance, approximating a soft joint. */
public record DistanceConstraint(BodyPart a, BodyPart b, double restLength, double stiffness) {
    public void solve() {
        Vec3d delta = b.position().subtract(a.position());
        double length = delta.length();
        if (length < 1.0e-8) return;
        double error = (length - restLength) / length;
        Vec3d correction = delta.multiply(error * stiffness * 0.5);
        a.translate(correction);
        b.translate(correction.multiply(-1));
    }
}
