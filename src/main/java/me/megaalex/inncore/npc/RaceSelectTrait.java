package me.megaalex.inncore.npc;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.megaalex.inncore.InnCore;
import me.megaalex.inncore.VaultManager;
import me.megaalex.inncore.utils.FireworksUtils;
import me.megaalex.inncore.utils.PlayerUtils;
import net.citizensnpcs.api.ai.speech.Talkable;
import net.citizensnpcs.api.ai.speech.event.SpeechBystanderEvent;
import net.citizensnpcs.api.ai.speech.event.SpeechTargetedEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;

public class RaceSelectTrait extends Trait {

    public final static String TRAIT_NAME = "raceselect";

    private HashMap<UUID, Long> clicks;

    // Persistent variables
    @Persist
    public String clickAgain = "&cCukni otnovo ako iskash da izberesh {1} rasa.";

    @Persist
    public String selectText = "&cTi se prisuedini kum nas! Welcome!";

    @Persist
    public String groupName = "";

    @Persist
    public String raceName = "";

    @Persist
    public String talkText = "<text>";

    @Persist
    public String alreadyJoined = "Ti veche imash rasa!";

    @Persist
    public String joinAnnounceText;

    @Persist
    public String leftClickText;

    public RaceSelectTrait() {
        super(TRAIT_NAME);
        this.clicks = new HashMap<>();
    }


    @EventHandler
    public void onSpeakEvent(final SpeechBystanderEvent e) {
        if(e.getContext().getTalker().getEntity() != this.npc.getEntity()) {
            return;
        }

        //System.out.println(e.getContext().size());

        //if(e.getContext().size() > 1) {
            e.setCancelled(true);
        //}

       /* final SpeechContext context = e.getContext();
        final Iterator<Talkable> recipients = context.iterator();

        while(recipients.hasNext()) {
            final Talkable recipient = recipients.next();
            if(recipient.getEntity() instanceof Player) {
                System.out.println(recipient.getEntity());
                if(RaceUtils.hasRace((Player) recipient.getEntity())) {
                    System.out.println("Removing!");
                    recipients.remove();
                }
            }
        }*/

    }

    @EventHandler
    public void onTargetSpeakEvent(final SpeechTargetedEvent e) {
        if(e.getContext().getTalker().getEntity() != this.npc.getEntity()) {
            return;
        }
        for (Talkable talkable : e.getContext()) {
            if (!(talkable.getEntity() instanceof Player)) {
                continue;
            }
            Player player = (Player) talkable.getEntity();
            if (player.hasPermission("inncore.race")) {
                e.setCancelled(true);
            }
            return;
        }
        /*for(Talkable talkable : e.getContext()) {
            if(!(talkable.getEntity() instanceof Player)) {
                continue;
            }
            Player player = (Player) talkable.getEntity();
            if(player.hasPermission("inncore.race")) {
                e.setCancelled(true);
                return;
            }
        }*/
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRightClick(final NPCRightClickEvent e) {
        final Player player = e.getClicker();

        if(e.getNPC() != this.npc) {
            return;
        }

        if("".equals(groupName)) {
            return;
        }

        if(player.hasPermission("inncore.race")) {
            if(alreadyJoined != null) {
                PlayerUtils.sendMessage(player, alreadyJoined);
            }
            return;
        }

        final Long lastClick = clicks.get(player.getUniqueId());
        final Long currTime = System.currentTimeMillis();


        // Check for second click
        if(lastClick != null && currTime - lastClick <= 1500L) {
            clicks.remove(player.getUniqueId());
            final VaultManager vaultManager = InnCore.getInstance().getVaultManager();
            try {
                final boolean result = vaultManager.addPlayerToGroup(player, groupName);
                if(!result) {
                    player.sendMessage(ChatColor.RED + "An error occurred! Try again later!");
                }
                PlayerUtils.sendMessage(player, selectText);
                postJoin(player);
            } catch (IllegalArgumentException ignored) {
                InnCore.getInstance().getLogger().warning("Invalid group " + groupName + " while trait-ing");
                player.sendMessage(ChatColor.RED + "An error occurred! Try again later!");
            }
        } else {
            clicks.put(player.getUniqueId(), System.currentTimeMillis());
            PlayerUtils.sendMessage(player, clickAgain, raceName);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onLeftClick(NPCLeftClickEvent e) {
        final Player player = e.getClicker();

        if (e.getNPC() != this.npc || "".equals(groupName)) {
            return;
        }

        if(player.hasPermission("inncore.race")) {
            PlayerUtils.sendMessage(player, leftClickText);
        } else {
            PlayerUtils.sendMessage(player, alreadyJoined);
        }
    }

    private void postJoin(final Player player) {
        new BukkitRunnable() {
            private int runs = 3;
            @Override
            public void run() {
                if(player == null) {
                    return;
                }
                Location location = player.getLocation();
                FireworkMeta fireworkMeta = FireworksUtils.generateRandomFirework();
                Firework firework = location.getWorld().spawn(location, Firework.class);
                firework.setFireworkMeta(fireworkMeta);
                if(runs-- <= 0) {
                    this.cancel();
                }
            }
        }.runTaskTimer(InnCore.getInstance(), 0L, 10L);
        PlayerUtils.sendMessageAllWithPermExcept(player.getUniqueId(),
                "inncore.race." + groupName, joinAnnounceText, player.getName());
    }
}
