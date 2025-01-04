package io.dogsbean.game;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameCommands implements CommandExecutor {
    private final GameManager gameManager;

    public GameCommands(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This is player only command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "/hideandseek start - starts the game");
            player.sendMessage(ChatColor.YELLOW + "/hideandseek end - ends the game");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start":
                if (!player.hasPermission("hideandseek.admin")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
                    return true;
                }
                if (gameManager.isGameRunning()) {
                    player.sendMessage(ChatColor.RED + "The game is already running!");
                    return true;
                }
                gameManager.startGame();
                break;

            case "end":
                if (!player.hasPermission("hideandseek.admin")) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to run this command.");
                    return true;
                }
                if (!gameManager.isGameRunning()) {
                    player.sendMessage(ChatColor.RED + "There is no ongoing game.");
                    return true;
                }
                gameManager.endGame();
                break;

            default:
                break;
        }

        return true;
    }
}