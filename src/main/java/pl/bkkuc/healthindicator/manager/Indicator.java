package pl.bkkuc.healthindicator.manager;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import pl.bkkuc.healthindicator.Plugin;
import pl.bkkuc.healthindicator.data.ConfigData;
import pl.bkkuc.healthindicator.handlers.IndicatorSpawnEvent;
import pl.bkkuc.healthindicator.manager.impl.IndicatorType;
import pl.bkkuc.purutils.ColorUtility;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Indicator {

    LivingEntity owner;

    @NonFinal
    Hologram hologram;

    IndicatorType indicatorType;

    double amount;

    @NonFinal
    BukkitTask task;

    public Indicator(LivingEntity owner, IndicatorType indicatorType, double amount){
        this.owner = owner;
        this.indicatorType = indicatorType;
        this.amount = Double.parseDouble(new DecimalFormat("#.#", DecimalFormatSymbols.getInstance(Locale.ENGLISH)).format(amount));

        ConfigData configData = Plugin.getInstance().getConfigData();

        String customName = ColorUtility.colorize(indicatorType == IndicatorType.POSITIVE ? configData.getPositive().getDisplay().replace("$amount", String.valueOf(amount)) : configData.getNegative().getDisplay().replace("$amount", String.valueOf(amount)));

        if(Plugin.getInstance().isDependFound()){
            customName = PlaceholderAPI.setPlaceholders(null, customName);
        }

        boolean x = ThreadLocalRandom.current().nextBoolean();
        boolean multiply = ThreadLocalRandom.current().nextBoolean();

        Location location = owner.getLocation().add(x ? 0.7 : 0, 1, x ? 0 : 0.7).add(owner.getLocation().getDirection().multiply(multiply ? -1.6 : 0));

        List<String> list = new ArrayList<>();
        list.add(customName);
        hologram = DHAPI.createHologram(UUID.randomUUID().toString(), location, list);

        Plugin.getInstance().getIndicatorManager().getIndicators().add(this);
        Bukkit.getPluginManager().callEvent(new IndicatorSpawnEvent(this));

        task = new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    destroy();
                } catch (Exception ignored) {}
            }
        }.runTaskLater(Plugin.getInstance(), 20L * (indicatorType == IndicatorType.POSITIVE ? configData.getPositive().getDuration() : configData.getNegative().getDuration()));
    }

    public void destroy(){
        if(hologram != null) hologram.delete();
        hologram = null;
        if(task != null) task.cancel();
        task = null;
    }
}
