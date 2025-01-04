package io.dogsbean.game;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GameListeners implements Listener {
    private final GameManager gameManager;

    public GameListeners(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!gameManager.isGameRunning()) {
            return;
        }

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if (gameManager.isSeeker(damager) && gameManager.isHider(victim)) {
            event.setCancelled(true);
            gameManager.catchHider(victim);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!gameManager.isGameRunning()) {
            return;
        }

        Player player = event.getPlayer();
        if (gameManager.isSeeker(player) || gameManager.isHider(player)) {
            gameManager.endGame();
        }
    }
}
