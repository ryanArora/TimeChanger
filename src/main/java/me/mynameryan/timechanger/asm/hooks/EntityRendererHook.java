package me.mynameryan.timechanger.asm.hooks;

import me.mynameryan.timechanger.TimeChanger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;

public class EntityRendererHook {
    public static float rotationYawHook(Entity entity) {
        return TimeChanger.instance.perspectiveToggled ? TimeChanger.instance.cameraYaw : entity.rotationYaw;
    }

    public static float prevRotationYawHook(Entity entity) {
        return TimeChanger.instance.perspectiveToggled ? TimeChanger.instance.cameraYaw : entity.prevRotationYaw;
    }

    public static float rotationPitchHook(Entity entity) {
        return TimeChanger.instance.perspectiveToggled ? TimeChanger.instance.cameraPitch : entity.rotationPitch;
    }

    public static float prevRotationPitchHook(Entity entity) {
        return TimeChanger.instance.perspectiveToggled ? TimeChanger.instance.cameraPitch : entity.prevRotationPitch;
    }

    public static boolean mouseHook(Minecraft minecraft) {
        return TimeChanger.instance == null ? minecraft.inGameHasFocus : TimeChanger.instance.overrideMouse();
    }

    public static double distanceHook(double value) {
        return value;
    }
}
