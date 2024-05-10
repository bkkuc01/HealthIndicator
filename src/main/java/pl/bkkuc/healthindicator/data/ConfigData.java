package pl.bkkuc.healthindicator.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pl.bkkuc.healthindicator.manager.impl.IndicatorType;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigData {

    Layout positive;
    Layout negative;

    public ConfigData(){
        positive = new Layout(IndicatorType.POSITIVE);
        negative = new Layout(IndicatorType.NEGATIVE);
    }
}
