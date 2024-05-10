package pl.bkkuc.healthindicator.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import pl.bkkuc.healthindicator.Plugin;
import pl.bkkuc.healthindicator.data.ConfigData;
import pl.bkkuc.healthindicator.manager.Indicator;
import pl.bkkuc.healthindicator.manager.impl.IndicatorType;

public class PlayerListener implements Listener {

    private final ConfigData configData = Plugin.getInstance().getConfigData();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityRegain(EntityRegainHealthEvent e){
        if(!(e.getEntity() instanceof LivingEntity)) return;
        if(e.getAmount() < 1) {
            e.setAmount(0.5);
        }
        LivingEntity livingEntity = (LivingEntity) e.getEntity();
        if(!configData.getPositive().getEntities().contains(livingEntity.getType())) return;

        EntityRegainHealthEvent.RegainReason regainReason = e.getRegainReason();
        if(!configData.getPositive().getRegainReasons().contains(regainReason)) return;

        Indicator indicator = new Indicator(livingEntity, IndicatorType.POSITIVE, e.getAmount());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    private void onEntityDamage(EntityDamageEvent e) {
        if(!(e.getEntity() instanceof LivingEntity)) return;
        LivingEntity livingEntity = (LivingEntity) e.getEntity();
        if(!configData.getNegative().getEntities().contains(livingEntity.getType())) return;

        EntityDamageEvent.DamageCause damageCause = e.getCause();
        if(!configData.getNegative().getDamageCauses().contains(damageCause)) return;

        Indicator indicator = new Indicator(livingEntity, IndicatorType.NEGATIVE, e.getDamage());
    }
}
