package io.github.mortuusars.copyscreenshot;

import com.mojang.logging.LogUtils;
import io.github.mortuusars.copyscreenshot.config.ClientConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CopyScreenshot.MOD_ID)
public class CopyScreenshot
{
    public static final String MOD_ID = "copyscreenshot";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CopyScreenshot()
    {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);

        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (str, ret) -> true));

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ScreenshotHandler::startHandling);
    }
}
