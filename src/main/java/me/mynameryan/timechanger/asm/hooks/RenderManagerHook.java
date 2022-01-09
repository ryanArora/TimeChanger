package me.mynameryan.timechanger.asm.hooks;

import me.mynameryan.timechanger.TimeChanger;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderManagerHook {
    public static void playerViewXHook(RenderManager renderManager, float value) {
        renderManager.playerViewX = TimeChanger.instance.perspectiveToggled ? TimeChanger.instance.cameraPitch : value;
    }

    public static void playerViewYHook(RenderManager renderManager, float value) {
        renderManager.playerViewY = TimeChanger.instance.perspectiveToggled ? TimeChanger.instance.cameraYaw : value;
    }
}
