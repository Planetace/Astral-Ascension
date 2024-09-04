package planetace.astralAscension.campaign.skills;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.MarketSkillEffect;

// A skill that improves all things logistical, such as colony income, production, upkeep etc.
// Only used by Omega Cores due to its Overpowered nature.
public class delphic_optimiser {

    public static float MARKET_MULT = 1.25f;
    public static float MARKET_UPKEEP = 0.75f;
    public static float BASE_ACCESS_ADDITION = 50f;


    // Increases Market Profit
    public static class Level1 implements MarketSkillEffect {
        public void apply(MarketAPI market, String id, float level) {
            market.getIncomeMult().modifyMult(id, MARKET_MULT, "Delphic Optimiser");
        }

        public void unapply(MarketAPI market, String id) {
            market.getIncomeMult().unmodifyMult(id);
        }

        public String getEffectDescription(float level) {
            return MARKET_MULT + "x Market Income";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    // Reduces Market Upkeep
    public static class Level2 implements MarketSkillEffect {
        public void apply(MarketAPI market, String id, float level) {
            market.getUpkeepMult().modifyMult(id, MARKET_UPKEEP, "Delphic Optimiser");
        }

        public void unapply(MarketAPI market, String id) {
            market.getUpkeepMult().unmodifyMult(id);
        }

        public String getEffectDescription(float level) {
            return MARKET_UPKEEP + "x Market Upkeep.";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    // Increase Market Production and Reduces Commodity Demands.
    public static class Level3 implements MarketSkillEffect {
        // Calculated Access Increase.
        public float AccessModifier(MarketAPI market) {
            return 5f * market.getSize() / 100;
        }


        public void apply(MarketAPI market, String id, float level) {
            market.getAccessibilityMod().modifyFlat(id, BASE_ACCESS_ADDITION / 100f + AccessModifier(market), "Delphic Optimiser");
        }

        public void unapply(MarketAPI market, String id) {
            market.getAccessibilityMod().unmodifyFlat(id);
        }

        public String getEffectDescription(float level) {
            return "+" + (int)BASE_ACCESS_ADDITION + "% base Accessibility, and increases Accessibility the larger the Market is.";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }

    // Omega Sub-market.
    public static class Level4 implements MarketSkillEffect {
        public void apply(MarketAPI market, String id, float level) {
            // Campaign Listener applies the Sub-market.
        }

        public void unapply(MarketAPI market, String id) {
            // Campaign Listener deletes the Sub-market.
        }

        public String getEffectDescription(float level) {
            return "Adds a special Omega Repository to the Market.";
        }

        public String getEffectPerLevelDescription() {
            return null;
        }

        public ScopeDescription getScopeDescription() {
            return ScopeDescription.GOVERNED_OUTPOST;
        }
    }
}
