package me.mynameryan.timechanger.asm.hooks;

import me.mynameryan.timechanger.TimeChanger;
import net.minecraft.entity.player.EntityPlayer;

public class ActiveRenderInfoHook {
    public static float rotationYawHook(EntityPlayer entity) {
        return TimeChanger.instance.perspectiveToggled ? TimeChanger.instance.cameraYaw : entity.rotationYaw;
    }

    public static float rotationPitchHook(EntityPlayer entity) {
        return TimeChanger.instance.perspectiveToggled ? TimeChanger.instance.cameraPitch : entity.rotationPitch;
    }
}
