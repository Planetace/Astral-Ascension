package planetace.astralAscension.campaign;

import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.AICoreAdminPlugin;
import com.fs.starfarer.api.campaign.BaseCampaignPlugin;


// Campaign Implentation of Omega Core Administrators
// Thanks to Kaysaar for showing me the way to implement it!
public class AA_OmegaCoreCampaignImpl extends BaseCampaignPlugin {

    // Generates Omega Core Administrator.
    @Override
    public PluginPick<AICoreAdminPlugin> pickAICoreAdminPlugin(String commodityId) {
        if(commodityId.equals("omega_core")){
            return new PluginPick<AICoreAdminPlugin>(new AA_OmegaCoreAdmin(),PickPriority.MOD_SET);
        }
        return null;
    }

    public String getId() {
        return "AstralModPlugin";
    }
}
