package de.kid2407.mcwiki.main;

import de.kid2407.mcwiki.command.CommandWiki;
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
        this.getCommand("wiki").setExecutor(new CommandWiki());

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
