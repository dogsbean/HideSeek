package io.dogsbean.hideseek;

import io.dogsbean.game.GameCommands;
import io.dogsbean.game.GameListeners;
import io.dogsbean.game.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HideAndSeek extends JavaPlugin {
    private GameManager gameManager;

    @Override
    public void onEnable() {
        gameManager = new GameManager(this);
        getCommand("hideandseek").setExecutor(new GameCommands(gameManager));
        getServer().getPluginManager().registerEvents(new GameListeners(gameManager), this);
        getLogger().info("Hide and Seek plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        gameManager.endGame();
        getLogger().info("Hide and Seek plugin has been disabled!");
    }
}
