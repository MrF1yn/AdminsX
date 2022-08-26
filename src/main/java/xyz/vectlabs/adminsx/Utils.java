package xyz.vectlabs.adminsx;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Utils {

        public static void parseAndExecCommand(Player p, String cmd){
            if(p==null)return;
            if(cmd.startsWith("@p ")){
                String command = cmd.substring(3);
                command = PlaceholderAPI.setPlaceholders(p, command);
//                command = ChatColor.translateAlternateColorCodes('&', command);
                Bukkit.dispatchCommand(p, command);
                return;
            }
            if (cmd.startsWith("@c ")) {
                String command = cmd.substring(3);
                command = PlaceholderAPI.setPlaceholders(p, command);
//                command = ChatColor.translateAlternateColorCodes('&', command);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                return;
            }
            String command = PlaceholderAPI.setPlaceholders(p, cmd);
//            command = ChatColor.translateAlternateColorCodes('&', command);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }

        public static String color(String s){
            return ChatColor.translateAlternateColorCodes('&',s);
        }

}
