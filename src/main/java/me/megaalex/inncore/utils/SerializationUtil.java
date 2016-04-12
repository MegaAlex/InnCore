/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

public class SerializationUtil {

    public static List<Map<String, Object>> serializeItemList(List<ItemStack> items) {
        List<Map<String, Object>> returnVal = new ArrayList<>();
        for(ItemStack is : items) {
            if(is == null) {
                is = new ItemStack(Material.AIR);
            }
            returnVal.add(serialize(is));
        }
        return returnVal;
    }

    public static Map<String, Object> serialize(ConfigurationSerializable cs) {
        Map<String, Object> serialized = recreateMap(cs.serialize());
        for(Map.Entry<String, Object> entry : serialized.entrySet()) {
            if(entry.getValue() instanceof ConfigurationSerializable) {
                entry.setValue(serialize((ConfigurationSerializable) entry.getValue()));
            }
        }
        serialized.put(ConfigurationSerialization.SERIALIZED_TYPE_KEY,
                ConfigurationSerialization.getAlias(cs.getClass()));
        return serialized;
    }

    public static Map<String, Object> recreateMap(Map<String, Object> original) {
        Map<String, Object> map = new HashMap<>();
        for(Map.Entry<String, Object> entry : original.entrySet()) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static ConfigurationSerializable deserialize(Map<String, Object> map) {
        for(Map.Entry<String, Object> entry : map.entrySet()) {
            if(entry.getValue() instanceof Map && ( (Map) entry.getValue()).containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                entry.setValue(deserialize((Map) entry.getValue()));
            }
        }
        return ConfigurationSerialization.deserializeObject(map);
    }

    public static List<ConfigurationSerializable> deserializeItemList(List<Map<String, Object>> itemList) {
        List<ConfigurationSerializable> returnVal = new ArrayList<>();
        for(Map<String, Object> map : itemList) {
            returnVal.add(deserialize(map));
        }
        return returnVal;
    }

}
