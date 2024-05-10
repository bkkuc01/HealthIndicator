package pl.bkkuc.healthindicator.handlers;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import pl.bkkuc.healthindicator.manager.Indicator;
import pl.bkkuc.healthindicator.manager.impl.IndicatorType;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IndicatorSpawnEvent extends Event {

    private final HandlerList handlerList = new HandlerList();

    LivingEntity owner;
    IndicatorType indicatorType;
    double amount;
    Hologram indicator;

    public IndicatorSpawnEvent(Indicator indicator){
        this.owner = indicator.getOwner();
        this.indicatorType = indicator.getIndicatorType();
        this.amount = indicator.getAmount();
        this.indicator = indicator.getHologram();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }
}
