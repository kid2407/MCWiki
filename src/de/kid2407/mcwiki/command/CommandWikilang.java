package de.kid2407.mcwiki.command;

import de.kid2407.mcwiki.main.MCWiki;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Tobias Franz on 11.03.2017.
 */
public class CommandWikilang implements CommandExecutor {

    private MCWiki plugin;
    private FileConfiguration config;

    public CommandWikilang(MCWiki plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] arguments) {
        if (arguments.length == 1) {
            if (config.isSet("lang." + arguments[0])) {
                config.set("general.lang", arguments[0]);
                plugin.saveConfig();
                commandSender.sendMessage("§2[MCWiki]§f " + config.getString("lang." + config.getString("general.lang") + ".newLang"));
            } else {
                commandSender.sendMessage("§2[MCWiki]§4 " + config.getString("lang." + config.getString("general.lang") + ".langError"));
            }
            return true;
        }

        return false;
    }
}
