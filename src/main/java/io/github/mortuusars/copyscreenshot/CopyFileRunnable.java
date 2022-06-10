package io.github.mortuusars.copyscreenshot;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class CopyFileRunnable implements Runnable {

    private final File sourceFile;
    private final Path sourcePath;
    private final Path outputDirectory;

    private final int _tries = 20;
    private final int _tryDelayMs = 500;

    public CopyFileRunnable(File sourceFile, Path outputDirectory){
        this.sourceFile = sourceFile;
        this.sourcePath = sourceFile.toPath();
        this.outputDirectory = outputDirectory;
    }

    @Override
    public void run() {
        for (int i = 0; i < _tries; i++){
            try {
                Thread.sleep(_tryDelayMs);
            } catch (InterruptedException ignored) { }

            if (sourceFile.exists() && sourceFile.canWrite()){
                copy();
                return;
            }
        }

        CopyScreenshot.LOGGER.error("Screenshot was not copied: Source file was not suitable fo copying (can't access) or file does not exist.");
    }

    private void copy(){

        try {
            Files.createDirectories(outputDirectory);
        } catch (IOException e) {
            CopyScreenshot.LOGGER.error("Cannot create directories: " + e);
            return;
        }

        Path filePath = outputDirectory.resolve(sourcePath.getFileName());

        try {
            Files.copy(sourcePath, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            CopyScreenshot.LOGGER.error("Copying screenshot file failed: " + e);
        }

        Player player = Minecraft.getInstance().player;
        if (player != null)
            player.sendMessage(getChatMessageAsComponent(filePath.toString(), filePath.toString()), Util.NIL_UUID);
    }

    private Component getChatMessageAsComponent(String fileName, String filePath){
        Component component = new TextComponent(fileName)
                .withStyle(ChatFormatting.UNDERLINE)
                .withStyle(style ->
                        style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, filePath)));
        return new TranslatableComponent("screenshot.copied", component);
    }
}
