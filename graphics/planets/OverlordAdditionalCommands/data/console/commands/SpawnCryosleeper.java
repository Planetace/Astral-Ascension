package data.console.commands;

import java.util.LinkedHashMap;
import java.util.Random;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.AddedEntity;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.EntityLocation;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.LocationType;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;

public class SpawnCryosleeper implements BaseCommand
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

		WeightedRandomPicker<EntityLocation> locs = null;
		if (args.length() > 0) {
			final Random random = new Random();
			final LinkedHashMap<LocationType, Float> weights = new LinkedHashMap<LocationType, Float>();
			weights.put(LocationType.PLANET_ORBIT, 10f);
			weights.put(LocationType.JUMP_ORBIT, 1f);
			weights.put(LocationType.NEAR_STAR, 1f);
			weights.put(LocationType.OUTER_SYSTEM, 5f);
			weights.put(LocationType.IN_ASTEROID_BELT, 5f);
			weights.put(LocationType.IN_RING, 5f);
			weights.put(LocationType.IN_ASTEROID_FIELD, 5f);
			weights.put(LocationType.STAR_ORBIT, 5f);
			weights.put(LocationType.IN_SMALL_NEBULA, 5f);
			weights.put(LocationType.L_POINT, 10f);
			locs = BaseThemeGenerator.getLocations(random, system, 100f, weights);
		} else {
			locs = new WeightedRandomPicker<EntityLocation>();
			EntityLocation loc = new EntityLocation();
			final CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
			loc.location = playerFleet.getLocation();
			loc.orbit = playerFleet.getOrbit();
			locs.add(loc);
		}

		final AddedEntity added = BaseThemeGenerator.addEntity(StarSystemGenerator.random, system, locs, Entities.DERELICT_CRYOSLEEPER, Factions.DERELICT);
		
		if (added != null) {
			system.addTag(Tags.THEME_INTERESTING);
			system.addTag(Tags.THEME_DERELICT);
			system.addTag(Tags.THEME_DERELICT_CRYOSLEEPER);
			
			Console.showMessage("Cryosleeper spawned");
			return CommandResult.SUCCESS;
		}

        Console.showMessage("Something went wrong spawning Cryosleeper");
        return CommandResult.ERROR;
    }
}