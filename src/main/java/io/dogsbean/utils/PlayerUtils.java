package io.dogsbean.utils;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketTitle;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.github.paperspigot.Title;

public class PlayerUtils {
    public static void sendTitle(Player player, Title title) {
        if (title.getTitle() != null) {
            LunarClientAPI.getInstance().sendPacket(player, new LCPacketTitle("title", BaseComponent.toLegacyText(title.getTitle()), (long)title.getStay() * 50L, (long)title.getFadeIn() * 50L, (long)title.getFadeOut() * 50L));
        }

        if (title.getSubtitle() != null) {
            LunarClientAPI.getInstance().sendPacket(player, new LCPacketTitle("subtitle", BaseComponent.toLegacyText(title.getSubtitle()), (long)title.getStay() * 50L, (long)title.getFadeIn() * 50L, (long)title.getFadeOut() * 50L));
        }
    }
}
