package net.minecraft.server;

import org.bukkit.craftbukkit.event.CraftPortalEvent; // CraftBukkit

public class ShapeDetectorShape {

    public final Vec3D position;
    public final Vec3D velocity;
    public final float yaw;
    public final float pitch;
    // CraftBukkit start
    public final WorldServer world;
    public final CraftPortalEvent portalEventInfo;

    public ShapeDetectorShape(Vec3D vec3d, Vec3D vec3d1, float f, float f1, WorldServer world, CraftPortalEvent portalEventInfo) {
        this.world = world;
        this.portalEventInfo = portalEventInfo;
        // CraftBukkit end
        this.position = vec3d;
        this.velocity = vec3d1;
        this.yaw = f;
        this.pitch = f1;
    }
}
