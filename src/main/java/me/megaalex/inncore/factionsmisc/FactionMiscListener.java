/*
 * Copyright (c) Aleksandar Ivanov
 * All rights reserved
 */

package me.megaalex.inncore.factionsmisc;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.RegionQuery;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.config.FactionMiscConfig;
import me.megaalex.inncore.messages.Message;
import me.megaalex.inncore.messages.MessageUtils;
import me.megaalex.inncore.utils.PlayerUtils;
import me.megaalex.innmerchant.util.InvUtils;

public class FactionMiscListener implements Listener {

    private final FactionMiscConfig config;
    private final FactionsMiscManager manager;

    public FactionMiscListener(FactionMiscConfig config, FactionsMiscManager manager) {
        this.config = config;
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if(!config.miningEnable) {
            return;
        }
        if(e.getTo().getWorld().getName().equalsIgnoreCase("mining") && !e.getPlayer().hasPermission(config.miningPerm)) {
            e.getPlayer().sendMessage(ChatColor.RED + "You don't have permission to enter the mining world! " +
                    "Select a race first.");
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPotionDrink(PlayerItemConsumeEvent e) {
        if(!config.disableStrPotions) {
            return;
        }
        if(e.getItem().getType() != Material.POTION) {
            return;
        }
        Potion potion = Potion.fromItemStack(e.getItem());
        for(PotionEffect potionEffect : potion.getEffects()) {
            if(potionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE) && potionEffect.getAmplifier() >= 1) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "Strength II potions are disabled!");
                return;
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionThrow(PotionSplashEvent e) {
        if(!config.disableStrPotions) {
            return;
        }
        ThrownPotion potion = e.getPotion();
        for(PotionEffect potionEffect : potion.getEffects()) {
            if(potionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE) && potionEffect.getAmplifier() >= 1) {
                e.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if(config.disableNaturalObsidian && e.getBlock().getType() == Material.OBSIDIAN) {
            List<MetadataValue> metaData = e.getBlock().getMetadata("innCoreObsidian");
            if(metaData == null || metaData.isEmpty()) {
                PlayerUtils.sendMessage(e.getPlayer(), config.obsidianNoticeMessage);
                e.getBlock().setType(Material.AIR);
                e.setCancelled(true);
            }
        }

        if(config.disableWorldMining && e.getBlock().getLocation().getWorld().getName().equalsIgnoreCase(config.mainWorldName)) {
            Material type = e.getBlock().getType();
            if(type == Material.IRON_ORE || type == Material.COAL_ORE
                    || type == Material.GOLD_ORE || type == Material.REDSTONE_ORE
                    || type == Material.GLOWING_REDSTONE_ORE || type == Material.DIAMOND_ORE
                    || type == Material.EMERALD_ORE || type == Material.LAPIS_ORE || type == Material.SAND) {
                String block = type == Material.SAND ? "pqsuk" : "rudi";
                e.getPlayer().sendMessage(ChatColor.RED + "Molq kopite " + block + " samo v mining sveta!"
                        +  ChatColor.GOLD + "/mining" + ChatColor.RED + " za da otidete do nego!");
                e.setCancelled(true);
                e.getBlock().setType(Material.AIR);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        if(!config.disableNaturalObsidian || e.getBlock().getType() != Material.OBSIDIAN) {
            return;
        }

        e.getBlock().setMetadata("innCoreObsidian", new FixedMetadataValue(InnCore.getInstance(), true));
    }

    @EventHandler
    public void onCmdPreProcess(PlayerCommandPreprocessEvent e) {
        if(!config.fixReportRtsCmd) {
            return;
        }
        if(e.getMessage().toLowerCase().startsWith("/ticket help")
                || e.getMessage().equalsIgnoreCase("/ticket")) {
            Player player = e.getPlayer();
            String args = e.getMessage().substring(7);
            if(args.isEmpty()) {
                args = " help";
            }
            String cmd = "reportrts" + args;
            Bukkit.getServer().dispatchCommand(player, cmd);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteractEntityEvent(PlayerInteractEntityEvent e) {
        if(!config.disableVillagerTrading) {
            return;
        }

        if(e.getRightClicked().getType() == EntityType.VILLAGER) {
            e.setCancelled(true);
            MessageUtils.sendMsg(e.getPlayer(), Message.VILLAGER_DISABLE);
        }
    }

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent e) {
        if (!config.rightClickIronDoors || !manager.hasWorldGuard()
                || e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if(e.getClickedBlock().getType() == Material.IRON_DOOR_BLOCK) {
            Block block = e.getClickedBlock();
            Block downBlock = block.getRelative(BlockFace.DOWN);
            if(downBlock.getType() == Material.IRON_DOOR_BLOCK) {
                block = downBlock;
            }
            LocalPlayer localPlayer = WGBukkit.getPlugin().wrapPlayer(e.getPlayer());
            RegionContainer container = WGBukkit.getPlugin().getRegionContainer();
            RegionQuery query = container.createQuery();
            if(e.getPlayer().hasPermission("inncore.factions.irondoor")
                    || query.testState(block.getLocation(), localPlayer, DefaultFlag.USE)) {
                BlockState state = block.getState();
                Openable data = (Openable) state.getData();
                data.setOpen(!data.isOpen());
                state.setData((MaterialData) data);
                state.update();
            }
        }
    }

    /*@EventHandler
    public void onMobKill(EntityDeathEvent e) {
        if(!config.nerfXP) {
            return;
        }

        e.setDroppedExp(getNerfedExp(e.getDroppedExp()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMinedOre(BlockBreakEvent e) {
        if(!config.nerfXP) {
            return;
        }

        e.setExpToDrop(getNerfedExp(e.getExpToDrop()));
    }*/

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerExpPickup(PlayerExpChangeEvent e) {
        if(e.getAmount() <= 0) {
            return;
        }
        if(!config.nerfXP) {
            return;
        }

        e.setAmount(getNerfedExp(e.getAmount()));
    }

    private int getNerfedExp(int xp) {
        int xpDrop = xp/ 2;
        if(xpDrop == 0) {
            xpDrop++;
        }
        return xpDrop;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onInvOpen(InventoryOpenEvent e) {

        boolean updated = processInventory(e.getView().getBottomInventory());
        if(!InvUtils.isValidInventory(e.getView().getTopInventory())) {
            updated = updated | processInventory(e.getView().getTopInventory());
        }
        if(updated) {
            ((Player) e.getPlayer()).updateInventory();
            System.out.println("[CIS] " + e.getPlayer().getName() + " opened a inv with cheated items!");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!config.removeCheatedItems) {
            return;
        }
        boolean updatedInv = processInventory(e.getPlayer().getInventory());
        if(updatedInv) {
            System.out.println("[CIS] " + e.getPlayer().getName() + " had cheated items in his inventory!");
            e.getPlayer().updateInventory();
        }
        boolean updatedEnder = processInventory(e.getPlayer().getEnderChest());
        if(updatedEnder) {
            System.out.println("[CIS] " + e.getPlayer().getName() + " had cheated items in his ender chest!");
        }
    }

    private boolean processInventory(Inventory inv) {
        boolean updated = false;
        for (ItemStack is : inv) {
            if (isCheated(is)) {
                System.out.println("[CIS] " + "Removed " + is.getAmount() + " of " + is.getType().name() + ".");
                inv.remove(is);
                updated = true;
            }
        }
        return updated;
    }


    private boolean isCheated(ItemStack is) {
        if(is == null || is.getType() == Material.AIR) {
            return false;
        }
        ItemMeta meta = is.getItemMeta();
        if(meta == null || !meta.hasLore() || meta.getLore() == null) {
            return false;
        }
        for(String loreLine : meta.getLore()) {
            if(loreLine.contains("item(s)") || loreLine.contains("quantity"))
                return true;
        }
        return false;
    }
}
