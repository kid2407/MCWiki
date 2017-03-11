package de.kid2407.mcwiki.main;

import de.kid2407.mcwiki.command.CommandWiki;
import de.kid2407.mcwiki.command.CommandWikilang;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Tobias Franz on 10.03.2017.
 */
public class MCWiki extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Enabling MCWiki-Plugin...");
        createConfig();
        this.getCommand("wiki").setExecutor(new CommandWiki(this));
        this.getCommand("wikilang").setExecutor(new CommandWikilang(this));

        try {
            final File[] libs = new File[]{
                    new File(getDataFolder(), "jsoup-1.10.2.jar")};
            for (final File lib : libs) {
                if (!lib.exists()) {
                    JarUtils.extractFromJar(lib.getName(),
                            lib.getAbsolutePath());
                }
            }
            for (final File lib : libs) {
                if (!lib.exists()) {
                    getLogger().warning(
                            "There was a critical error loading My plugin! Could not find lib: "
                                    + lib.getName());
                    Bukkit.getServer().getPluginManager().disablePlugin(this);
                    return;
                }
                addClassPath(JarUtils.getJarUrl(lib));
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
    }

    private void createConfig() {
        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdirs();
                saveDefaultConfig();
            }
            File configFile = new File(getDataFolder(), "config.yml");
            if (!configFile.exists()) {
                getLogger().info("Config.yml not found, creating!");
                getConfig().set("general.lang", "en-EN");
                // German config
                getConfig().set("lang.de-DE.wikiUrl", "http://minecraft-de.gamepedia.com/");
                getConfig().set("lang.de-DE.notFound", "Die gewuenschte Seite konnte nicht gefunden werden.");
                getConfig().set("lang.de-DE.noConnection", "Das Wiki konnte nicht erreicht werden.");
                getConfig().set("lang.de-DE.urlError", "Fehlerhafte URL.");
                getConfig().set("lang.de-DE.ioException", "IOExcetion beim laden/erzeugen der Konfigurationsdateien");
                getConfig().set("lang.de-DE.newLang", "Nun ist Deutsch als Sprache eingestellt.");
                getConfig().set("lang.de-DE.langError", "Die angegebene Sprache konnte nicht gefunden werden.");
                // English config
                getConfig().set("lang.en-EN.wikiUrl", "http://minecraft.gamepedia.com/");
                getConfig().set("lang.en-EN.notFound", "The requested page could not be found.");
                getConfig().set("lang.en-EN.noConnection", "Error while connecting to the Wiki.");
                getConfig().set("lang.en-EN.urlError", "Misformed URL.");
                getConfig().set("lang.en-EN.ioException", "IOException while reading/creating config files.");
                getConfig().set("lang.en-EN.newLang", "You've selected English as your language.");
                getConfig().set("lang.en-EN.langError", "The specified Language doesn't exist.");
                // Save default config
                saveConfig();
            } else {
                getLogger().info("Config.yml found, loading!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void addClassPath(final URL url) throws IOException {
        final URLClassLoader sysloader = (URLClassLoader) ClassLoader
                .getSystemClassLoader();
        final Class<URLClassLoader> sysclass = URLClassLoader.class;
        try {
            final Method method = sysclass.getDeclaredMethod("addURL",
                    new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{url});
        } catch (final Throwable t) {
            t.printStackTrace();
            throw new IOException("Error adding " + url
                    + " to system classloader");
        }
    }
}
