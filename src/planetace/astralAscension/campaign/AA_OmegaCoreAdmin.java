package planetace.astralAscension.campaign;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.AICoreAdminPlugin;
import com.fs.starfarer.api.campaign.PersonImportance;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.FullName.Gender;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.Ranks;

import planetace.astralAscension.campaign.skills.AstralSkills;


// Gives Omega Cores administrative functionality.
public class AA_OmegaCoreAdmin implements AICoreAdminPlugin {
    public PersonAPI createPerson(String aiCoreId, String factionId, long seed) {
        // Create the "person", assigns name and allegiance, graphics, all that.
        PersonAPI person = Global.getFactory().createPerson();
        person.setFaction(factionId);
        person.setAICoreId(aiCoreId);
        person.setName(new FullName("Omega Core", "", Gender.ANY));
        person.setPortraitSprite("graphics/portraits/characters/omega.png");

        // Sets rank ID, what post they are in, and their importance.
        person.setRankId(null);
        person.setPostId(Ranks.POST_ADMINISTRATOR);
        person.setImportance(PersonImportance.VERY_HIGH);

        // Set Skills for the Omega Core
        person.getStats().setSkillLevel(AstralSkills.ETHEREAL_ENLIGHTENMENT, 1);
        person.getStats().setSkillLevel(AstralSkills.ACUMENIOUS_ORACLE, 1);
        person.getStats().setSkillLevel(AstralSkills.DELPHIC_OPTIMISER, 1);

        // Returns the Omega Core as a 'Person'.
        return person;
    }
}
