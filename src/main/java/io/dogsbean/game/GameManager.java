package io.dogsbean.game;

import io.dogsbean.hideseek.HideAndSeek;
import io.dogsbean.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.github.paperspigot.Title;

import java.util.*;

public class GameManager {
    private final HideAndSeek plugin;
    private boolean isGameRunning = false;
    private Player seeker;
    private Set<Player> hiders = new HashSet<>();
    private int seekerReleaseTime = 30;
    private int gameTime = 3000;

    public GameManager(HideAndSeek plugin) {
        this.plugin = plugin;
    }

    public void startGame() {
        if (isGameRunning) {
            return;
        }

        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if (players.size() < 2) {
            Bukkit.broadcastMessage(ChatColor.RED + "At least 2 players are required. Failed to start the game.");
            return;
        }

        isGameRunning = true;
        Random random = new Random();
        seeker = players.get(random.nextInt(players.size()));
        players.remove(seeker);
        hiders.addAll(players);

        Bukkit.broadcastMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "Hide And Seek");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "Seeker: " + seeker.getName());

        seeker.setGameMode(GameMode.ADVENTURE);
        seeker.teleport(seeker.getWorld().getSpawnLocation());

        for (Player hider : hiders) {
            hider.setGameMode(GameMode.ADVENTURE);
        }

        new BukkitRunnable() {
            int timeLeft = seekerReleaseTime;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    Bukkit.broadcastMessage(ChatColor.RED + "The seeker has been released. Run!");
                    startGameTimer();
                    cancel();
                    return;
                }

                PlayerUtils.sendTitle(seeker, Title.builder().title(ChatColor.RED + Integer.toString(timeLeft)).stay(100).build());

                timeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    private void startGameTimer() {
        new BukkitRunnable() {
            int timeLeft = gameTime;

            @Override
            public void run() {
                if (timeLeft <= 0 || !isGameRunning) {
                    endGame();
                    cancel();
                    return;
                }

                if (timeLeft == 60 || timeLeft == 30 || timeLeft == 10) {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "There are " + timeLeft + " seconds remaining until the game ends.");
                }

                timeLeft--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public void endGame() {
        if (!isGameRunning) {
            return;
        }

        isGameRunning = false;
        Bukkit.broadcastMessage(ChatColor.GREEN + "Game Ended.");

        if (hiders.isEmpty()) {
            Bukkit.broadcastMessage(ChatColor.GOLD + "Seeker has won the game!");
        } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + "Hider has won the game!");
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
        }

        seeker = null;
        hiders.clear();
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public boolean isSeeker(Player player) {
        return seeker != null && seeker.equals(player);
    }

    public boolean isHider(Player player) {
        return hiders.contains(player);
    }

    public void catchHider(Player hider) {
        if (!isGameRunning || !hiders.contains(hider)) {
            return;
        }

        hiders.remove(hider);
        Bukkit.broadcastMessage(ChatColor.RED + hider.getName() + " has been caught!");

        if (hiders.isEmpty()) {
            endGame();
        }
    }
}