package de.kid2407.mcwiki.command;

import de.kid2407.mcwiki.main.MCWiki;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tobias Franz on 10.03.2017.
 */
public class CommandWiki implements CommandExecutor {

    private FileConfiguration config;

    public CommandWiki(MCWiki plugin) {
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandText, String[] arguments) {

        if (arguments.length == 1) {
            String language = config.getString("general.lang");
            String name = arguments[0];
            String urlString = config.getString("lang." + language + ".wikiUrl") + name;

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Language", language);
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
                connection.connect();

                int statuscode = connection.getResponseCode();
                if (statuscode == 404) {
                    commandSender.sendMessage("§2[MCWiki] §f" + config.getString("lang." + language + ".notFound"));
                    return true;
                } else if (statuscode == 200) {
                    InputStream in = connection.getInputStream();
                    commandSender.sendMessage("§2[MCWiki] §FLink: " + connection.getURL());
                    in.close();
                    Document html = Jsoup.connect(connection.getURL().toString()).userAgent("Mozilla").get();
                    String kurztext = html.body().select("#mw-content-text > p").get(0).text();
                    commandSender.sendMessage("§2[MCWiki]§f " + kurztext);
                    return true;
                } else {
                    commandSender.sendMessage("§2[MCWiki]§f " + config.getString("lang." + language + ".noConnection"));
                    return true;
                }
            } catch (MalformedURLException e) {
                System.out.println(config.getString("lang." + language + ".urlError"));
            } catch (IOException e) {
                System.out.println(config.getString("lang." + language + ".ioError"));
            }

            return true;
        } else {
            return false;
        }
    }
}
