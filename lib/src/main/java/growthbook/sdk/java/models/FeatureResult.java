package growthbook.sdk.java.models;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import growthbook.sdk.java.services.GrowthBookJsonUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

@Data
@Builder
@AllArgsConstructor
public class FeatureResult<ValueType> {

    @Nullable
    @SerializedName("value")
    Object value;

    @Nullable
    FeatureResultSource source;

    @Nullable
    Experiment<ValueType> experiment;

    @Nullable
    ExperimentResult<ValueType> experimentResult;

    @Nullable
    String ruleId;

    public String toJson() {
        return FeatureResult.getJson(this).toString();
    }

    public Boolean isOn() {
        if (value == null) return false;

        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        if (value instanceof String) {
            return !((String) value).isEmpty();
        }

        if (value instanceof Integer) {
            return (Integer) value != 0;
        }

        if (value instanceof Float) {
            return (Float) value != 0.0f;
        }

        return false;
    }

    public Boolean isOff() {
        return !isOn();
    }

    public static <ValueType> JsonElement getJson(FeatureResult<ValueType> object) {
        JsonObject jsonObject = new JsonObject();
        JsonPrimitive isOn = new JsonPrimitive(object.isOn());
        JsonPrimitive isOff = new JsonPrimitive(object.isOff());
        jsonObject.add("on", isOn);
        jsonObject.add("off", isOff);

        Object value = object.getValue();
        JsonElement valueElement = GrowthBookJsonUtils.getInstance().gson.toJsonTree(value);
        jsonObject.add("value", valueElement);

        Experiment<ValueType> experiment = object.getExperiment();
        JsonElement experimentElement = GrowthBookJsonUtils.getInstance().gson.toJsonTree(experiment);
        jsonObject.add("experiment", experimentElement);

        ExperimentResult<ValueType> experimentResult = object.getExperimentResult();
        JsonElement experimentResultElement = GrowthBookJsonUtils.getInstance().gson.toJsonTree(experimentResult);
        jsonObject.add("experimentResult", experimentResultElement);

        FeatureResultSource source = object.getSource();
        if (source != null) {
            JsonPrimitive jsonSource = new JsonPrimitive(source.toString());
            jsonObject.add("source", jsonSource);
        }

        return jsonObject;
    }

    public static <ValueType> JsonSerializer<FeatureResult<ValueType>> getSerializer() {
        return new JsonSerializer<FeatureResult<ValueType>>() {
            @Override
            public JsonElement serialize(FeatureResult<ValueType> src, Type typeOfSrc, JsonSerializationContext context) {
                return FeatureResult.getJson(src);
            }
        };
    }
}
