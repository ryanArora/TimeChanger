package me.mynameryan.timechanger;

import me.mynameryan.timechanger.commands.TimeChangerCommand;
import me.mynameryan.timechanger.util.TimeChangerConfig;
import me.mynameryan.timechanger.handlers.TimeChangeNetHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

@Mod(name = "TimeChanger", modid = "timechanger", version = "1.2.1", clientSideOnly = true)
public class TimeChanger {
    @Mod.Instance
    public static TimeChanger instance;
    private static Configuration config;
    private final Minecraft mc = Minecraft.getMinecraft();

    public boolean perspectiveToggled = false;
    public float cameraYaw = 0F;
    public float cameraPitch = 0F;
    private int previousPerspective = 0;
    private boolean prevState = false;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        config = new Configuration(event.getSuggestedConfigurationFile());
        if (TimeChangerConfig.getTime().equalsIgnoreCase("day") || TimeChangerConfig.getTime().equalsIgnoreCase("night") || TimeChangerConfig.getTime().equalsIgnoreCase("sunset")) { // Replace legacy config with new config system added in 1.2
            System.out.println("Detected usage of legacy configuration system. Resetting config");
            TimeChangerConfig.setTime("vanilla");
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new TimeChangerCommand());
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().theWorld != null) {
            final INetHandler parent = Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().getNetHandler();
            if (!(parent instanceof TimeChangeNetHandler)) {
                Minecraft.getMinecraft().thePlayer.sendQueue.getNetworkManager().setNetHandler((INetHandler) new TimeChangeNetHandler((NetHandlerPlayClient) parent));
            }
            if (TimeChangerConfig.getTime().equalsIgnoreCase("fast")) {
                Minecraft.getMinecraft().theWorld.setWorldTime((long) (System.currentTimeMillis() * TimeChangerConfig.getFastMultiplier() % 24000.0));
            }
        }

        boolean down = Keyboard.isKeyDown(Keyboard.KEY_GRAVE);
        if (down != prevState && mc.currentScreen == null && mc.theWorld != null && mc.thePlayer != null) {
            prevState = down;
            onPressed(down);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui != null && perspectiveToggled) {
            resetPerspective();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (perspectiveToggled) {
            resetPerspective();
        }
    }

    public void onPressed(boolean state) {
        if (state) {
            cameraYaw = mc.thePlayer.rotationYaw;
            cameraPitch = mc.thePlayer.rotationPitch;

            if (perspectiveToggled) {
                resetPerspective();
            } else {
                enterPerspective();
            }
            mc.renderGlobal.setDisplayListEntitiesDirty();
        } else {
            resetPerspective();
        }
    }

    public void enterPerspective() {
        perspectiveToggled = true;
        previousPerspective = mc.gameSettings.thirdPersonView;
        mc.gameSettings.thirdPersonView = 1;
    }

    public void resetPerspective() {
        perspectiveToggled = false;
        mc.gameSettings.thirdPersonView = previousPerspective;
        mc.renderGlobal.setDisplayListEntitiesDirty();
    }

    public boolean overrideMouse() {
        if (mc.inGameHasFocus && Display.isActive()) {
            if (!perspectiveToggled) {
                return true;
            }

            // CODE
            mc.mouseHelper.mouseXYChange();
            float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f2 = f1 * f1 * f1 * 8.0F;
            float f3 = (float) mc.mouseHelper.deltaX * f2;
            float f4 = (float) mc.mouseHelper.deltaY * f2;

            cameraYaw += f3 * 0.15F;
            cameraPitch += f4 * 0.15F;

            if (cameraPitch > 90) cameraPitch = 90;
            if (cameraPitch < -90) cameraPitch = -90;
            mc.renderGlobal.setDisplayListEntitiesDirty();
        }

        return false;
    }

    public static Configuration getConfig() {
        return config;
    }
}
