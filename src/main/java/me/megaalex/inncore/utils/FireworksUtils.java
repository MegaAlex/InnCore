/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.utils;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworksUtils {

    public static FireworkMeta generateRandomFirework() {

        FireworkMeta fireMeta = (FireworkMeta) (new ItemStack(Material.FIREWORK))
                .getItemMeta();

        Random random = new Random();

        FireworkEffect.Builder fireBuilder = FireworkEffect.builder();
        fireBuilder.flicker(random.nextBoolean());
        fireBuilder.trail(random.nextBoolean());
        fireBuilder.with(FireworkEffect.Type.values()[random.nextInt(FireworkEffect.Type.values().length)]);
        fireBuilder.withColor(Color.WHITE.setBlue(random.nextInt(256))
                .setGreen(random.nextInt(256)).setRed(random.nextInt(256)));
        if (random.nextBoolean()) {
            fireBuilder.withFade(Color.WHITE.setBlue(random.nextInt(256))
                    .setGreen(random.nextInt(256)).setRed(random.nextInt(256)));
        }

        fireMeta.addEffect(fireBuilder.build());
        fireMeta.setPower(1 + random.nextInt(2));

        return fireMeta;
    }
}
