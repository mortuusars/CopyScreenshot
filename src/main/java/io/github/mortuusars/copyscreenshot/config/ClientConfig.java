package io.github.mortuusars.copyscreenshot.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<String> OUTPUT_DIRECTORY;
    public static final ForgeConfigSpec.BooleanValue CREATE_WORLD_SUBFOLDER;

    static{
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        OUTPUT_DIRECTORY = builder.define("OutputDirectory", "C:/MinecraftScreenshots/");
        builder.comment("If set to true - screenshots will be placed in a world name subfolder:");
        CREATE_WORLD_SUBFOLDER = builder.define("PlaceInSubfolders", true);

        SPEC = builder.build();
    }
}
