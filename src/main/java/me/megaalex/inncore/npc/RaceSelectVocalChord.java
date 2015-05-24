package me.megaalex.inncore.npc;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.speech.SpeechContext;
import net.citizensnpcs.api.ai.speech.Talkable;
import net.citizensnpcs.api.ai.speech.VocalChord;
import net.citizensnpcs.api.npc.NPC;

public class RaceSelectVocalChord implements VocalChord {

    public static final String CHORD_NAME = "raceselectchord";

    @Override
    public String getName() {
        return CHORD_NAME;
    }

    @Override
    public void talk(SpeechContext context) {
        if (context.getTalker() == null)
            return;
        final NPC npc = CitizensAPI.getNPCRegistry().getNPC(context.getTalker().getEntity());
        if(npc == null)
            return;
        if(!npc.hasTrait(RaceSelectTrait.class)) {
            return;
        }

        final RaceSelectTrait trait = npc.getTrait(RaceSelectTrait.class);

        if(context.size() != 1) {
            return;
        }

        String text = trait.talkText.replace("<npc>", npc.getName())
                .replace("<text>", context.getMessage());

        for(Talkable entity : context) {
            entity.talkTo(context, text, this);
        }
    }
}
