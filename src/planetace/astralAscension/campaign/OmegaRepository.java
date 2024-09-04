package planetace.astralAscension.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.submarkets.BaseSubmarketPlugin;
import com.fs.starfarer.api.util.Highlights;


// Omega Repository
// A Submarket that allows players to buy Omega Weaponry and Vessels.
public class OmegaRepository extends BaseSubmarketPlugin {

    // Initialises it.
    public void init(SubmarketAPI submarket) {
        super.init(submarket);

        // Checks game memory, if the market has had a previous Omega Administrator it'll set the time till next market update to be a full month.
        // This prevents players from "gaming" the Omega Core market, via un-assignment of administrators.
        if (market.getMemoryWithoutUpdate().get("$OmegaRepository") == null) {
            // Otherwise, we add the memory key in.
            market.getMemoryWithoutUpdate().set("$OmegaRepository", true);
            this.sinceSWUpdate = 31.0F;
        } else {
            // Set Cargo to the same as the previous market.
            // KNOWN BUG - Won't store and load market ships for whatever reason.
            this.getCargo().addAll((CargoAPI) market.getMemoryWithoutUpdate().get("$OmegaRepository_Cargo"));

            this.sinceSWUpdate = 0.0F;
        }
    }

    // Updates the Market.
    public void updateCargoPrePlayerInteraction() {
        float seconds = Global.getSector().getClock().convertToSeconds(sinceLastCargoUpdate);
        addAndRemoveStockpiledResources(seconds, false, true, true);
        sinceLastCargoUpdate = 0f;

        if (this.okToUpdateShipsAndWeapons()) {
            this.sinceSWUpdate = 0.0F;

            // Prunes Weapons and Ships.
            this.pruneWeapons(0.0F);
            getCargo().getMothballedShips().clear();

            // Adds Omega Weapons
            int weapons = 50;
            addWeapons(weapons, weapons + weapons / 2, 100, "omega");

            // We need this to find doctrineOverride.
            FactionDoctrineAPI doctrineOverride = this.submarket.getFaction().getDoctrine().clone();
            doctrineOverride.setShipSize(5);

            // Adds Omega Ships
            this.addShips("omega",
                    40.0f,
                    0.0F,
                    0.0F,
                    0.0F,
                    0.0F,
                    0.0F,
                    2.0F,
                    0.0F,
                    FactionAPI.ShipPickMode.PRIORITY_THEN_ALL, doctrineOverride);

            // Stores Cargo
            market.getMemoryWithoutUpdate().set("$OmegaRepository_Cargo",getCargo().createCopy());
        }
        // Sort dat Cargo.
        getCargo().sort();
    }

    // Prevents player's from selling stuff to the Omega.
    // After all, the Omega is a super-intelligence, It's like trying to sell random throwing-pebbles to the industrial military complex.
    public boolean isIllegalOnSubmarket(CargoStackAPI stack, SubmarketPlugin.TransferAction action) {
        return action == TransferAction.PLAYER_SELL;
    }

    // No clue what this does but if it prunes stuff it must be necessary.
    protected Object writeReplace() {
        if (okToUpdateShipsAndWeapons()) {
            pruneWeapons(0f);
            getCargo().getMothballedShips().clear();
        }
        return this;
    }

    // How the player can impact the market.
    public PlayerEconomyImpactMode getPlayerEconomyImpactMode() {
        return PlayerEconomyImpactMode.PLAYER_BUY_ONLY;
    }


    public boolean isOpenMarket() {
        return true;
    }


    // Description stuff
    @Override
    public String getTooltipAppendix(CoreUIAPI ui) {
        return super.getTooltipAppendix(ui);
    }


    @Override
    public Highlights getTooltipAppendixHighlights(CoreUIAPI ui) {
        return super.getTooltipAppendixHighlights(ui);
    }
}
