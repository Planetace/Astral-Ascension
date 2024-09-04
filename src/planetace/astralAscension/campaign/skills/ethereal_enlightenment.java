package planetace.astralAscension.campaign.skills;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.CharacterStatsSkillEffect;
import com.fs.starfarer.api.characters.MarketSkillEffect;
import com.fs.starfarer.api.characters.MutableCharacterStatsAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;


// A skill that improves colonies in some "general" ways, such as reduced hazard, fleet multiplier etc.
// Only used by Omega Cores due to its Overpowered nature.
public class ethereal_enlightenment {
    public static float HAZARD_REDUCTION = -25f;
    public static float PRODUCTION_QUALITY_BONUS = 25f;

    public ethereal_enlightenment() { }

    // Reduces Hazard
    public static class Level1 implements MarketSkillEffect {

        public void apply(MarketAPI market, String id, float level) {
            market.getHazard().modifyFlat(id, HAZARD_REDUCTION / 100f, "Ethereal Enlightenment");
        }

        public void unapply(MarketAPI market, String id) {
            market.getHazard().unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            return (int)(HAZARD_REDUCTION) + "% hazard rating";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    // Increase Ship Quality.
    public static class Level2 implements MarketSkillEffect {

        public void apply(MarketAPI market, String id, float level) {
            market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).modifyFlat(id, PRODUCTION_QUALITY_BONUS / 100f, "Ethereal Enlightenment");
        }

        public void unapply(MarketAPI market, String id) {
            market.getStats().getDynamic().getMod(Stats.PRODUCTION_QUALITY_MOD).unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)PRODUCTION_QUALITY_BONUS + "% Ship Quality";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    public static class Level3 implements CharacterStatsSkillEffect {

        public void apply(MutableCharacterStatsAPI stats, String id, float level) {
            stats.getDynamic().getMod(Stats.SUPPLY_BONUS_MOD).modifyFlat(id, 2);
            stats.getDynamic().getMod(Stats.DEMAND_REDUCTION_MOD).modifyFlat(id, 1);
        }

        public void unapply(MutableCharacterStatsAPI stats, String id) {
            stats.getDynamic().getMod(Stats.SUPPLY_BONUS_MOD).unmodifyFlat(id);
            stats.getDynamic().getMod(Stats.DEMAND_REDUCTION_MOD).unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            return "Increases Colony Production by 2 units, and reduces Commodity Demand by 1.";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    // Increase Max Industries.
    public static class Level4 implements MarketSkillEffect {

        public void apply(MarketAPI market, String id, float level) {
            market.getStats().getDynamic().getMod(Stats.MAX_INDUSTRIES).modifyFlat(id, 1, "Ethereal Enlightenment");
        }

        public void unapply(MarketAPI market, String id) {
            market.getStats().getDynamic().getMod(Stats.MAX_INDUSTRIES).unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            return "Increases the maximum amount of industries of this colony by 1.";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return null;
        }
    }
}
