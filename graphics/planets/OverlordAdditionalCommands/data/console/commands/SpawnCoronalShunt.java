package data.console.commands;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomEntitySpecAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.CoronalTapParticleScript;
import com.fs.starfarer.api.impl.campaign.ids.Entities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.StarTypes;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator.StarSystemType;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.MiscellaneousThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.AddedEntity;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.EntityLocation;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;
import org.lwjgl.util.vector.Vector2f;

public class SpawnCoronalShunt implements BaseCommand
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

		String factionId = Factions.NEUTRAL;
		AddedEntity entity = null;
		if (system.getType() == StarSystemType.TRINARY_2CLOSE) {
			EntityLocation loc = new EntityLocation();
			loc.location = new Vector2f();
			entity = BaseThemeGenerator.addEntity(StarSystemGenerator.random, system, loc, Entities.CORONAL_TAP, factionId);
			if (entity != null) {
				system.addScript(new MiscellaneousThemeGenerator.MakeCoronalTapFaceNearestStar(entity.entity));
				Console.showMessage("Error: The system does not have a suitable star.");
				return CommandResult.ERROR;
			}
		} else {
			WeightedRandomPicker<PlanetAPI> picker = new WeightedRandomPicker<PlanetAPI>();
			WeightedRandomPicker<PlanetAPI> fallback = new WeightedRandomPicker<PlanetAPI>();
			for (PlanetAPI planet : system.getPlanets()) {
				if (!planet.isNormalStar()) continue;
				if (planet.getTypeId().equals(StarTypes.BLUE_GIANT) ||
						planet.getTypeId().equals(StarTypes.BLUE_SUPERGIANT)) {
					picker.add(planet);
				} else {
					fallback.add(planet);
				}
			}
			if (picker.isEmpty()) {
				picker.addAll(fallback);
			}
			
			PlanetAPI star = (PlanetAPI)picker.pick();
			if (star != null) {
				CustomEntitySpecAPI spec = Global.getSettings().getCustomEntitySpec(Entities.CORONAL_TAP);
				EntityLocation loc = new EntityLocation();
				float orbitRadius = star.getRadius() + spec.getDefaultRadius() + 100f;
				float orbitDays = orbitRadius / 20f;
				loc.orbit = Global.getFactory().createCircularOrbitPointingDown(star, StarSystemGenerator.random.nextFloat() * 360f, orbitRadius, orbitDays);
				entity = BaseThemeGenerator.addEntity(StarSystemGenerator.random, system, loc, Entities.CORONAL_TAP, factionId);
			}
			else {
				Console.showMessage("Error: The system does not have a suitable star.");
				return CommandResult.ERROR;
			}
		}
		
		
		if (entity != null) {
			system.addScript(new CoronalTapParticleScript(entity.entity));
			system.addTag(Tags.HAS_CORONAL_TAP);
			Console.showMessage("Coronal shunt successfully created in system " + system.getName());
			return CommandResult.SUCCESS;
		}

        Console.showMessage("Something went wrong spawning Coronal Shunt.");
        return CommandResult.ERROR;
    }
}