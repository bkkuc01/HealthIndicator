package pl.bkkuc.healthindicator.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.jetbrains.annotations.Nullable;
import pl.bkkuc.healthindicator.Plugin;
import pl.bkkuc.healthindicator.manager.impl.IndicatorType;

import java.util.ArrayList;
import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Layout {

    final String name;

    String display;
    int duration;

    List<EntityType> entities;

    @Nullable List<EntityRegainHealthEvent.RegainReason> regainReasons;
    @Nullable List<EntityDamageEvent.DamageCause> damageCauses;

    public Layout(IndicatorType indicatorType){
        this.name = indicatorType.name().toLowerCase();

        ConfigurationSection section = Plugin.getInstance().getConfig().getConfigurationSection(name);
        if(section == null) return;

        this.duration = section.getInt("duration", 3);
        this.display = section.getString("display");
        entities = new ArrayList<>();

        for(String entityTypeName: section.getStringList("entities")) {
            entityTypeName = entityTypeName.toUpperCase();
            EntityType entityType;
            try {
                entityType = EntityType.valueOf(entityTypeName);
            } catch (IllegalArgumentException e){
                Plugin.getInstance().getLogger().warning("Entity type '" + entityTypeName + "' is not found.");
                continue;
            }
            entities.add(entityType);
        }

        switch (indicatorType) {
            case POSITIVE: {
                regainReasons = new ArrayList<>();
                damageCauses = null;

                for(String regainReasonName: section.getStringList("cause")) {
                    regainReasonName = regainReasonName.toUpperCase();
                    EntityRegainHealthEvent.RegainReason regainReason;
                    try {
                        regainReason = EntityRegainHealthEvent.RegainReason.valueOf(regainReasonName);
                    } catch (IllegalArgumentException e){
                        Plugin.getInstance().getLogger().warning("Health reason '" + regainReasonName + "' is not found!");
                        continue;
                    }
                    regainReasons.add(regainReason);
                }
                break;
            }
            case NEGATIVE: {
                damageCauses = new ArrayList<>();
                regainReasons = null;

                for(String damageCauseName: section.getStringList("cause")) {
                    damageCauseName = damageCauseName.toUpperCase();
                    EntityDamageEvent.DamageCause damageCause;
                    try {
                        damageCause = EntityDamageEvent.DamageCause.valueOf(damageCauseName);
                    } catch (IllegalArgumentException e){
                        Plugin.getInstance().getLogger().warning("Damage cause '" + damageCauseName + "' is not found!");
                        continue;
                    }
                    damageCauses.add(damageCause);
                }
                break;
            }
        }
    }
}
