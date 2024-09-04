package planetace.astralAscension.campaign.conditions;

import com.fs.starfarer.api.impl.campaign.econ.BaseMarketConditionPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;


// Matrioshka World
// Increases Hazard, increases Access, Market Income, Fleet Size by both a flat and multiplicative bonus, and Defence Combat Effectiveness.
public class AA_condition_matrioshka_world extends BaseMarketConditionPlugin {
    public static float HAZARD_REDUCTION = -25f;
    public static float ACCESS_BONUS = 200f;
    public static float INCOME_MULTIPLIER = 2f;
    public static float FLEET_ADDITION = 200f;
    public static float FLEET_MULTIPLIER = 1.5f;
    public static float GROUND_ADDITION = 4f;


    public void apply(String id) {
        super.apply(id);
        // Modify Market Aspects
        market.getHazard().modifyFlat(id,HAZARD_REDUCTION/100f,"Matrioshka World");
        market.getAccessibilityMod().modifyFlat(id,ACCESS_BONUS/100f, "Matrioshka World");
        market.getIncomeMult().modifyMult(id,INCOME_MULTIPLIER, "Matrioshka World");
        // Modify Military Aspects
        market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat(id, FLEET_ADDITION/100f, "Matrioshka World");
        market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyMult(id, FLEET_MULTIPLIER, "Matrioshka World");
        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).modifyMult(id, GROUND_ADDITION,"Matrioshka World");
    }

    public void unapply(String id) {
        super.unapply(id);
        // Unapply Market Aspects.
        market.getAccessibilityMod().unmodifyFlat(id);
        market.getHazard().unmodifyFlat(id);
        market.getIncomeMult().unmodifyMult(id);
        // Unassign Military Aspects
        market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyFlat(id);
        market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyMult(id);
        market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyMult(id);
    }

    protected void createTooltipAfterDescription(TooltipMakerAPI tooltip, boolean expanded) {
        super.createTooltipAfterDescription(tooltip, expanded);
        // Where we add the actual Tooltips.
        tooltip.addPara("%s Hazard Rating", 10f, Misc.getHighlightColor(),(int)HAZARD_REDUCTION + "%");
        tooltip.addPara("%s Accessibility", 10f, Misc.getHighlightColor(), "+" + (int)ACCESS_BONUS+ "%");
        tooltip.addPara("%s Colony Income", 10f, Misc.getHighlightColor(), INCOME_MULTIPLIER + "x");
        tooltip.addPara("%s Fleet Size", 10f, Misc.getHighlightColor(), "+" + (int)FLEET_ADDITION + "%");
        tooltip.addPara("%s Fleet Size Multiplier", 10f, Misc.getHighlightColor(), FLEET_MULTIPLIER + "x");
        tooltip.addPara("%s Ground Combat Rating", 10f, Misc.getHighlightColor(), GROUND_ADDITION + "x");
    }
}