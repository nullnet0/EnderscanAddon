package xyz.nullnetdev.enderscanaddon;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class Main extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Enderscan");
    public static final HudGroup HUD_GROUP = new HudGroup("Enderscan");
    private static Main instance;

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "xyz.nullnetdev.enderscanaddon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("nullnet0", "enderscanaddon");
    }
}
