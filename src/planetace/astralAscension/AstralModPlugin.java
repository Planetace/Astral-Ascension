package planetace.astralAscension;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;

import planetace.astralAscension.AA_ColonyGraphicsSwitch;


public class AstralModPlugin extends BaseModPlugin {
    @Override
    public void onApplicationLoad() throws Exception {
        super.onApplicationLoad();
    }

    @Override
    public void onNewGame() {
        // PLEASE FOR THE LOVE OF GOD, DON'T FUCKING ENABLE THIS ON PUBLIC RELEASE - Past Planetace.
        System.out.println(">:3 I'm a silly billy - Planetace");
    }

    @Override
    public void onGameLoad(boolean newGame) {
        try {
            // Try and load in an Economy Update Listener, specifically the Colony Graphics Switch which detects colony size.
            // We need this to switch graphics for higher tier colonies.
            Global.getSector().getEconomy().addUpdateListener(new AA_ColonyGraphicsSwitch());
        } catch(Exception e) {
            // Exception handling.
            System.out.println("Couldn't activate Graphics Switch Class Successfully.");
        }
    }
}
