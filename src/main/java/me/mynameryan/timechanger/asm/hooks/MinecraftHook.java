package me.mynameryan.timechanger.asm.hooks;

import me.mynameryan.timechanger.TimeChanger;
import net.minecraft.client.settings.GameSettings;

public class MinecraftHook {
    public static void thirdPersonHook(GameSettings gameSettings, int value) {
        if (TimeChanger.instance.perspectiveToggled) {
            TimeChanger.instance.resetPerspective();
        } else {
            gameSettings.thirdPersonView = value;
        }
    }
}
