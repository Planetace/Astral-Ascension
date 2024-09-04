package planetace.astralAscension.campaign;

import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.AbilityPlugin;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;

import java.util.Objects;

// Frankly, most of this is necessary. We just need to check if the Market has an Omega Core Admin.
// If we have future campaign stuff in need of a listener, we'll just stuff them all into this.
public class AA_CampaignListener implements CampaignEventListener {

    // Check if Market has an Omega Core. We don't apply the repository if there already is one.
    // But if the market has no omega core admin, yet has the repository, we remove it.
    public void OmegaAdminCheck(MarketAPI market) {
        if (Objects.equals(market.getAdmin().getName().getFirst(), "Omega Core") && !market.hasSubmarket("omega_repository")) {
            market.addSubmarket("omega_repository");
        } else if (!Objects.equals(market.getAdmin().getName().getFirst(), "Omega Core") && market.hasSubmarket("omega_repository")) {
            market.removeSubmarket("omega_repository");
        }
    }

    @Override
    public void reportPlayerOpenedMarket(MarketAPI market) {
        OmegaAdminCheck(market);
    }

    @Override
    public void reportPlayerClosedMarket(MarketAPI market) {
        OmegaAdminCheck(market);
    }

    @Override
    public void reportPlayerOpenedMarketAndCargoUpdated(MarketAPI market) {
        OmegaAdminCheck(market);
    }

    @Override
    public void reportEncounterLootGenerated(FleetEncounterContextPlugin plugin, CargoAPI loot) {

    }

    @Override
    public void reportPlayerMarketTransaction(PlayerMarketTransaction transaction) {

    }

    @Override
    public void reportBattleOccurred(CampaignFleetAPI primaryWinner, BattleAPI battle) {

    }

    @Override
    public void reportBattleFinished(CampaignFleetAPI primaryWinner, BattleAPI battle) {

    }

    @Override
    public void reportPlayerEngagement(EngagementResultAPI result) {

    }

    @Override
    public void reportFleetDespawned(CampaignFleetAPI fleet, FleetDespawnReason reason, Object param) {

    }

    @Override
    public void reportFleetSpawned(CampaignFleetAPI fleet) {

    }

    @Override
    public void reportFleetReachedEntity(CampaignFleetAPI fleet, SectorEntityToken entity) {

    }

    @Override
    public void reportFleetJumped(CampaignFleetAPI fleet, SectorEntityToken from, JumpPointAPI.JumpDestination to) {

    }

    @Override
    public void reportShownInteractionDialog(InteractionDialogAPI dialog) {

    }

    @Override
    public void reportPlayerReputationChange(String faction, float delta) {

    }

    @Override
    public void reportPlayerReputationChange(PersonAPI person, float delta) {

    }

    @Override
    public void reportPlayerActivatedAbility(AbilityPlugin ability, Object param) {

    }

    @Override
    public void reportPlayerDeactivatedAbility(AbilityPlugin ability, Object param) {

    }

    @Override
    public void reportPlayerDumpedCargo(CargoAPI cargo) {

    }

    @Override
    public void reportPlayerDidNotTakeCargo(CargoAPI cargo) {

    }

    @Override
    public void reportEconomyTick(int iterIndex) {

    }

    @Override
    public void reportEconomyMonthEnd() {

    }
}
