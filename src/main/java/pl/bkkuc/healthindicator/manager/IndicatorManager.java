package pl.bkkuc.healthindicator.manager;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IndicatorManager {

    List<Indicator> indicators;

    public IndicatorManager(){
        this.indicators = new ArrayList<>();
    }

    public void removeIndicators(){
        indicators.forEach(Indicator::destroy);
    }

    public List<Indicator> getNearbyIndicators(Location location, double radius){
        return indicators.stream().filter(indicator -> indicator.getHologram().getLocation().distance(location) <= radius).collect(Collectors.toList());
    }
}
