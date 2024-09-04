package planetace.astralAscension.campaign.skills;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.MarketSkillEffect;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.intel.events.HostileActivityCause;


// A skill that increase things relating to "foresight", ground defences, stability, fleet size and what not.
// Only used by Omega Cores due to its Overpowered nature.
public class acumenious_oracle {
    public static float GROUND_DEFENCE_MULT = 2f;
    public static float BASE_FLEET_INCREASE = 50f;


    public acumenious_oracle() { }

    // Increase Colony Stability
    public static class Level1 implements MarketSkillEffect {
        public Level1() { }

        public void apply(MarketAPI market, String id, float level) {
            market.getStability().modifyFlat(id, 5, "Acumenious Oracle");
        }

        public void unapply(MarketAPI market, String id) {
            market.getStability().unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            return "+" + 5 + " Stability";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    // Increase Ground Defence Value.
    public static class Level2 implements MarketSkillEffect {
        public Level2() { }

        public void apply(MarketAPI market, String id, float level) {
            market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).modifyMult(id, GROUND_DEFENCE_MULT, "Acumenious Oracle");
        }

        public void unapply(MarketAPI market, String id) {
            market.getStats().getDynamic().getMod(Stats.GROUND_DEFENSES_MOD).unmodifyMult(id);
        }

        public String getEffectDescription(float level) {
            return (int)(GROUND_DEFENCE_MULT) + "x Ground Defence Score";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    // Fleet Scaling based on Market Size
    public static class Level3 implements MarketSkillEffect {
        public Level3() { }

        // Calculates Fleet Scaling, it's divided by 100 because 1.0f represents 100 "points" of fleet power.
        public float calculateFleetScale(MarketAPI market) {
            return 12.5f * market.getSize() / 100;
        }

        public void apply(MarketAPI market, String id, float level) {

            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat(id, BASE_FLEET_INCREASE / 100 + calculateFleetScale(market), "Acumenious Oracle");
        }

        public void unapply(MarketAPI market, String id) {
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)BASE_FLEET_INCREASE + "% base Fleet size, and increases Fleet size the large the Market is.";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }
}
