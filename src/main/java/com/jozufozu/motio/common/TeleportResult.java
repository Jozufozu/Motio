package com.jozufozu.motio.common;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class TeleportResult
{
    public final boolean canTeleport;
    public final Vec3d teleportPos;
    public final Vec3d lookVec;
    
    public TeleportResult(boolean canTeleport, Vec3d teleportPos, Vec3d lookTrace)
    {
        this.canTeleport = canTeleport;
        this.teleportPos = teleportPos;
        this.lookVec = lookTrace;
    }
}
