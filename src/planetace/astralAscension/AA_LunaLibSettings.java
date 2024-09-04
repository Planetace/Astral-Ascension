package planetace.astralAscension;

import com.fs.starfarer.api.Global;
import static com.fs.starfarer.api.util.Misc.MAX_COLONY_SIZE;

import lunalib.lunaSettings.LunaSettings;


// Lunalib Support.
public class AA_LunaLibSettings  {
    public void AA_LunaLibMethod() {

        // Checks if Lunalib is enabled (very important).
        if (Global.getSettings().getModManager().isModEnabled("lunalib")) {

            // Assign the settings.
            MAX_COLONY_SIZE = LunaSettings.getInt("Planetace_AstralAscension","maxColonySize");
            Global.getSettings().setFloat("drop_prob_officer_omega_core",
                    LunaSettings.getFloat("Planetace_AstralAscension","drop_prob_officer_omega_core"));
        }
    }
}
