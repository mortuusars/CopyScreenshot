package io.github.mortuusars.copyscreenshot;

import io.github.mortuusars.copyscreenshot.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.Executors;

public class ScreenshotHandler {

    public static void startHandling(){
        MinecraftForge.EVENT_BUS.addListener(ScreenshotHandler::handleScreenshot);
    }

    public static void handleScreenshot(ScreenshotEvent screenshotEvent){
        if (screenshotEvent.isCanceled()){
            CopyScreenshot.LOGGER.warn("Screenshot event was cancelled.");
            return;
        }

        File file = screenshotEvent.getScreenshotFile();

        Path directoryPath;

        if (ClientConfig.CREATE_WORLD_SUBFOLDER.get()){
            String levelName = getLevelName();
            directoryPath = Path.of(ClientConfig.OUTPUT_DIRECTORY.get(), levelName);
        }
        else
            directoryPath = Path.of(ClientConfig.OUTPUT_DIRECTORY.get());

        CopyScreenshot.LOGGER.info("Copying screenshot to: " + directoryPath);
        Executors.newCachedThreadPool().execute(new CopyFileRunnable(file, directoryPath));
    }

    private static String getLevelName(){
        try{
            if (Minecraft.getInstance().getCurrentServer() == null){
                String gameDirectory = Minecraft.getInstance().gameDirectory.getAbsolutePath();
                Path savesDir = Path.of(gameDirectory, "/saves");

                File[] dirs = savesDir.toFile().listFiles((dir, name) -> new File(dir, name).isDirectory());

                if (dirs == null || dirs.length == 0)
                    return "";

                File lastModified = dirs[0];

                for (File dir : dirs) {
                    if (dir.lastModified() > lastModified.lastModified())
                        lastModified = dir;
                }

                return lastModified.getName();
            }
            else {
                return Minecraft.getInstance().getCurrentServer().name;
            }
        }
        catch (Exception e){
            CopyScreenshot.LOGGER.error("Getting level name failed: " + e);
            return "";
        }
    }
}
