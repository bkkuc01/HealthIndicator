package pl.bkkuc.healthindicator;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bkkuc.healthindicator.data.ConfigData;
import pl.bkkuc.healthindicator.listeners.PlayerListener;
import pl.bkkuc.healthindicator.manager.IndicatorManager;

@Getter
public final class Plugin extends JavaPlugin {

    @Getter
    private static Plugin instance;

    private ConfigData configData;

    private boolean dependFound;

    private IndicatorManager indicatorManager;

    @Override
    public void onEnable() {
        instance = this;

        if(!Bukkit.getPluginManager().isPluginEnabled("DecentHolograms")){
            getLogger().warning("Plugin 'DecentHolograms' is not enabled or found.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        configData = new ConfigData();

        dependFound = Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");

        indicatorManager = new IndicatorManager();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        if(indicatorManager != null) indicatorManager.removeIndicators();
    }
}
