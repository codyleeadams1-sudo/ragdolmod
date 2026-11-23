# Mob Ragdolls — Fabric 1.21.11

A **client-side prototype project** for an articulated Minecraft mob-ragdoll system.

## Important status note

This first project ZIP contains the Fabric mod scaffold, a Verlet-style particle solver,
soft distance-joint constraints, death detection, a bounded ragdoll manager, and a basic
player-proximity impulse. **It does not yet replace mob models with rendered ragdoll parts.**
The physics state is simulated internally but is not currently visible in-game as moving
limbs. Ground/block collision resolution, mob-specific rigs (spiders, quadrupeds, wings),
and multiplayer synchronization are also not implemented yet. Those need a dedicated
render/model adapter and additional testing; this ZIP does not claim they are finished.

The code intentionally keeps the physics core separate so the next implementation pass
can connect each mob's model parts to the simulated body segments.

## Target

- Minecraft Java Edition 1.21.11
- Fabric Loader 0.18.1
- Fabric API 0.141.1+1.21.11
- Java 21
- Official Mojang mappings

These versions follow the Fabric 1.21.11 documentation and reference build configuration.

## Build on GitHub

1. Create a new empty GitHub repository.
2. Extract this ZIP and upload the **contents** of the folder (not the enclosing folder).
3. Open the repository's **Actions** tab.
4. Run **Build Fabric Mod** (or push a commit to start it automatically).
5. Download the `mob-ragdolls-jar` artifact from the completed workflow run.

## Build locally

Install a Java 21 JDK and Gradle 8.14+.

From this folder run:

```bash
gradle build
```

The remapped mod JAR will be in `build/libs/`. Use the JAR without `-dev` in its filename.

## In-game toggle

The mod registers a key mapping named **Mob Ragdolls: Toggle** under Controls → Miscellaneous.
It is unbound by default; assign a key before using it.

## Next engineering milestones

1. Add a render extraction hook for 1.21.11 and draw the physics segments.
2. Replace vanilla death pose rendering while a ragdoll is active.
3. Build model adapters for humanoids, quadrupeds, birds, spiders, and custom entities.
4. Add block/world collision contacts and friction.
5. Improve player-body collision from proximity impulses to segment-vs-player contacts.
6. Add optional server packets for shared ragdolls in multiplayer.

## License

MIT. See `LICENSE`.
