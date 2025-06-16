package xyz.nullnetdev.enderscanaddon.mixin;

import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.themes.meteor.MeteorGuiTheme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.nullnetdev.enderscanaddon.Main;
import xyz.nullnetdev.enderscanaddon.screens.ScannerScreen;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
    @Shadow private ButtonWidget buttonEdit;

    @Shadow
    public abstract ServerList getServerList();

    @Shadow
    private ButtonWidget buttonJoin;

    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        ButtonWidget.Builder builder = ButtonWidget.builder(
            Text.literal("Enderscan"),
            button -> opengui()
        ).size(98, 20);
        ButtonWidget button = builder.build();

        ButtonWidget editButton = this.buttonJoin;

        if (editButton != null) {
            button.setPosition(editButton.getX() - button.getWidth() - 10, editButton.getY());
        }

        this.addDrawableChild(button);
    }

    @Unique
    private static void opengui() {
        MinecraftClient.getInstance().setScreen(new ScannerScreen(GuiThemes.get()));
    }
}
