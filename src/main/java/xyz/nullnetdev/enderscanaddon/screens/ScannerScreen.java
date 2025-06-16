package xyz.nullnetdev.enderscanaddon.screens;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.WLabel;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.input.WSlider;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import xyz.nullnetdev.enderscanaddon.mixin.MultiplayerScreenMixin;
import xyz.nullnetdev.enderscanaddon.utils.API;
import xyz.nullnetdev.enderscanaddon.utils.objects.FoundServer;

import java.util.ArrayList;
import java.util.List;

public class ScannerScreen extends WindowScreen {
    private WTable table;
    private List<FoundServer> servers = new ArrayList<>();

    public ScannerScreen(GuiTheme theme) {
        super(theme, "Enderscan");
    }

    @Override
    public void initWidgets() {
        try {
            add(theme.horizontalSeparator("Utility")).expandX();
            WButton clear = add(theme.button("Clear found servers")).widget();
            add(theme.label("Removes all servers that contain word 'Enderscan' in their name.").color(theme.textSecondaryColor()));
            clear.action = () -> {
                clear.set("Clearing...");
                MinecraftClient client = MinecraftClient.getInstance();
                ServerList list = new ServerList(client);
                list.loadFile();

                for (int i = list.size() - 1; i >= 0; i--) {
                    ServerInfo info = list.get(i);
                    if (info.name.startsWith("Enderscan - ")) {
                        list.remove(info);
                    }
                }
                list.saveFile();
                if (client.currentScreen instanceof MultiplayerScreen) {
                    client.setScreen(new MultiplayerScreen(client.currentScreen));
                }

                clear.set("Cleared!");
            };

            WButton addAll = add(theme.button("Add all")).widget();
            add(theme.label("Use this to add all of servers that were found").color(theme.textSecondaryColor()));
            addAll.action = () -> {
                addAll.set("Adding...");
                MinecraftClient client = MinecraftClient.getInstance();
                ServerList list = new ServerList(client);
                list.loadFile();

                for (FoundServer server : servers) {
                    ServerInfo info = new ServerInfo(
                        "Enderscan - " + server.ip(),
                        server.ip(),
                        ServerInfo.ServerType.OTHER
                    );
                    list.add(info, false);
                }

                list.saveFile();
                addAll.set("Added!");
            };


            add(theme.horizontalSeparator("Amount")).expandX();
            WTextBox amount = add(theme.textBox("10", "10")).minWidth(100).widget();
            add(theme.label("Amount of servers to retrieve.").color(theme.textSecondaryColor()));
            add(theme.horizontalSeparator("Version")).expandX();
            WTextBox version = add(theme.textBox("", "1.21.5")).minWidth(60).widget();
            add(theme.label("Version of servers to find. (leave blank for any)").color(theme.textSecondaryColor()));
            add(theme.horizontalSeparator("Min. players online")).expandX();
            WTextBox minOnline = add(theme.textBox("0", "10")).minWidth(100).widget();
            add(theme.label("Minimum number of players online.").color(theme.textSecondaryColor()));
            add(theme.horizontalSeparator("MOTD")).expandX();
            WTextBox motd = add(theme.textBox("", "A Minecraft Server")).expandX().widget();
            add(theme.label("Text that should appear in MOTD of found servers.").color(theme.textSecondaryColor()));
            add(theme.horizontalSeparator()).expandX();
            WButton submit = add(theme.button("Search")).widget();
            submit.action = () -> {
                search(Integer.parseInt(amount.get()), version.get(), Integer.parseInt(minOnline.get()), motd.get());
            };

            add(theme.horizontalSeparator("Result")).expandX();

            table = add(theme.table()).expandX().minWidth(400).widget();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search(int limit, String version, int min_online, String motdFilter) {
        table.clear();
        servers = API.getServers(limit, version, min_online, motdFilter);
        if (servers == null) {
            table.add(theme.label("Network error"));
            return;
        }
        if (servers.isEmpty()) {
            table.add(theme.label("No servers found"));
            return;
        }

        for (FoundServer server : servers) {
            String rawMotd = server.motd();
            if (rawMotd == null) {
                rawMotd = "null";
            }
            String motd = rawMotd.length() > 30
                ? rawMotd.substring(0, 27) + "..."
                : rawMotd;

            WButton addBtn = table.add(theme.button("Add")).widget();
            addBtn.action = () -> {
                addBtn.set("Adding...");
                MinecraftClient client = MinecraftClient.getInstance();

                ServerList list = new ServerList(client);
                list.loadFile();
                list.add(new ServerInfo(
                    "Enderscan - " + server.ip(),
                    server.ip(),
                    ServerInfo.ServerType.OTHER
                ), false);
                list.saveFile();

                if (client.currentScreen instanceof MultiplayerScreen) {
                    client.setScreen(new MultiplayerScreen(client.currentScreen));
                }

                addBtn.set("Done!");
            };


            table.add(theme.label(server.ip() + " - " + motd))
                .minWidth(400)
                .expandX();

            table.row();
        }
    }

}
