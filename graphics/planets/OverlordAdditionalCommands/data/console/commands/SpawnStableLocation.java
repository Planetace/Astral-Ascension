package data.console.commands;

import java.util.LinkedHashMap;
import java.util.Random;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.AddedEntity;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.EntityLocation;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.LocationType;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;

public class SpawnStableLocation implements BaseCommand
{
    @Override
    public CommandResult runCommand(String args, CommandContext context) {
		if (!context.isInCampaign()) {
			Console.showMessage(CommonStrings.ERROR_CAMPAIGN_ONLY);
			return CommandResult.WRONG_CONTEXT;
		}

		final StarSystemAPI system = (StarSystemAPI) Global.getSector().getCurrentLocation();

		if (system.isHyperspace()) {
			Console.showMessage("Error: This command cannot be used in hyperspace.");
			return CommandResult.WRONG_CONTEXT;
		}

		EntityLocation loc = null;

		if (args.length() > 0) {
			final Random random = new Random();
			final LinkedHashMap<LocationType, Float> weights = new LinkedHashMap<LocationType, Float>();
			weights.put(LocationType.STAR_ORBIT, 10f);
			weights.put(LocationType.OUTER_SYSTEM, 10f);
			weights.put(LocationType.L_POINT, 10f);
			weights.put(LocationType.IN_SMALL_NEBULA, 2f);
			final WeightedRandomPicker<EntityLocation> locs = BaseThemeGenerator.getLocations(random, system, null, 100f, weights);

			if (locs.isEmpty()) {
				loc = BaseThemeGenerator.pickAnyLocation(random, system, 200f, null);
			} else {
				loc = (EntityLocation) locs.pick();
			}
		} else {
			loc = new EntityLocation();
			final CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
			loc.location = playerFleet.getLocation();
			loc.orbit = playerFleet.getOrbit();
		}

		final AddedEntity added = BaseThemeGenerator.addNonSalvageEntity(system, loc, Entities.STABLE_LOCATION, Factions.NEUTRAL);
		
		if (added != null) {
			BaseThemeGenerator.convertOrbitPointingDown(added.entity);
			
			Console.showMessage("Stable location spawned");
			return CommandResult.SUCCESS;
		}

        Console.showMessage("Something went wrong spawning stable location");
        return CommandResult.ERROR;
    }
}