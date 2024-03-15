package cc.rcbb.influxdb.demo.util;

import lombok.experimental.UtilityClass;
import org.influxdb.dto.Point;

import java.util.Map;

/**
 * <p>
 * InfluxdbUtils
 * </p>
 *
 * @author rcbb.cc
 * @date 2024/3/15
 */
@UtilityClass
public class InfluxdbUtils {

    private static void setFieldValue(Point.Builder pointBuilder, String entryKey, Object value) {
        if (value instanceof Boolean) {
            pointBuilder.addField(entryKey, (Boolean) value);
        } else if (value instanceof Long) {
            pointBuilder.addField(entryKey, (Long) value);
        } else if (value instanceof Double) {
            pointBuilder.addField(entryKey, (Double) value);
        } else if (value instanceof Integer) {
            pointBuilder.addField(entryKey, (Integer) value);
        } else if (value instanceof Float) {
            pointBuilder.addField(entryKey, (Float) value);
        } else if (value instanceof Short) {
            pointBuilder.addField(entryKey, (Short) value);
        } else if (value instanceof Number) {
            pointBuilder.addField(entryKey, (Number) value);
        } else if (value instanceof String) {
            pointBuilder.addField(entryKey, (String) value);
        } else if (value != null) {
            pointBuilder.addField(entryKey, value.toString());
        }
    }

    private static Long getValueTime(Map<String, Object> valueMap) {
        Object time = valueMap.get("time");
        if (time == null) {
            return null;
        } else if (time instanceof String) {
            return Long.parseLong((String) time);
        } else if (time instanceof Number) {
            return ((Number) time).longValue();
        } else {
            return Long.parseLong(time.toString());
        }
    }

}
