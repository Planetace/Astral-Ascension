package planetace.astralAscension;

// Vanilla stuff for the plugin.
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.BaseModPlugin;

// Import plugins from the rest of the mod.
import planetace.astralAscension.campaign.econ.ColonyRebuilder;
import planetace.astralAscension.campaign.AA_CampaignListener;
import planetace.astralAscension.campaign.AA_OmegaCoreCampaignImpl;


public class AstralModPlugin extends BaseModPlugin {

    @Override
    public void onApplicationLoad() {}

    // Only use for "Once and Done" things, like generating star systems.
    @Override
    public void onNewGame() { }

    // Loads Stuff after the game has generated the Sector and all markets. Useful for applying values to planets which need other values from themselves.
    @Override
    public void onNewGameAfterEconomyLoad() { }

    // Loads every time you open a save. THIS INCLUDES WHEN GENERATING A NEW GAME.
    @Override
    public void onGameLoad(boolean newGame) {
        Global.getSector().registerPlugin(new AA_OmegaCoreCampaignImpl());
        Global.getSector().getEconomy().addUpdateListener(new ColonyRebuilder());
        Global.getSector().addListener(new AA_CampaignListener());

        // Luna-lib Settings Support. Changes settings 'in-game' everytime a game is loaded.
        new AA_LunaLibSettings().AA_LunaLibMethod();
    }
}
