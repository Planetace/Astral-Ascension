package data.console.commands;

import java.util.LinkedHashMap;
import java.util.Random;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.ids.Abilities;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.FleetTypes;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.ids.Skills;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator;
import com.fs.starfarer.api.impl.campaign.procgen.themes.RemnantStationFleetManager;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.EntityLocation;
import com.fs.starfarer.api.impl.campaign.procgen.themes.BaseThemeGenerator.LocationType;
import com.fs.starfarer.api.impl.campaign.procgen.themes.RemnantThemeGenerator.RemnantStationInteractionConfigGen;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;

public class SpawnRemnantNexus implements BaseCommand
{
    @Override
    public CommandResult runCommand(String args, CommandContext context)
    {
        if (!context.isInCampaign())
        {
            Console.showMessage(CommonStrings.ERROR_CAMPAIGN_ONLY);
            return CommandResult.WRONG_CONTEXT;
        }
		
		StarSystemAPI system = (StarSystemAPI)Global.getSector().getCurrentLocation();

		if (system.isHyperspace())
		{
            Console.showMessage("Error: This command cannot be used in hyperspace.");
            return CommandResult.WRONG_CONTEXT;
		}
		
		EntityLocation loc = null;
		CampaignFleetAPI fleet = null;
		Random random = new Random();

		if (args.length() > 0)
		{
			LinkedHashMap<LocationType, Float> weights = new LinkedHashMap<LocationType, Float>();
			weights.put(LocationType.PLANET_ORBIT, 10f);
			weights.put(LocationType.STAR_ORBIT, 10f);
			weights.put(LocationType.GAS_GIANT_ORBIT, 5f);
			WeightedRandomPicker<EntityLocation> locs = BaseThemeGenerator.getLocations(random, system, null, 200f, weights);

			if (locs.isEmpty()) {
				loc = BaseThemeGenerator.pickAnyLocation(random, system, 200f, null);
			}
			else 
			{
				loc = (EntityLocation)locs.pick();
			}
		}
		else
		{
			loc = new EntityLocation();
			CampaignFleetAPI playerFleet = Global.getSector().getPlayerFleet();
			loc.location = playerFleet.getLocation();
			loc.orbit = playerFleet.getOrbit();
		}
		
		String type = "remnant_station2_Standard";
		if (loc != null) {
			
			fleet = FleetFactoryV3.createEmptyFleet(Factions.REMNANTS, FleetTypes.BATTLESTATION, null);
			
			FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, type);
			fleet.getFleetData().addFleetMember(member);
			
			fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_MAKE_AGGRESSIVE, true);
			fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_NO_JUMP, true);
			fleet.getMemoryWithoutUpdate().set(MemFlags.MEMORY_KEY_MAKE_ALLOW_DISENGAGE, true);
			
			fleet.setStationMode(true);
			
			fleet.getMemoryWithoutUpdate().set(MemFlags.FLEET_INTERACTION_DIALOG_CONFIG_OVERRIDE_GEN, new RemnantStationInteractionConfigGen());	
			
			system.addEntity(fleet);
			
			fleet.clearAbilities();
			fleet.addAbility(Abilities.TRANSPONDER);
			fleet.getAbility(Abilities.TRANSPONDER).activate();
			fleet.getDetectedRangeMod().modifyFlat("gen", 1000f);
			
			fleet.setAI(null);
			
			BaseThemeGenerator.setEntityLocation(fleet, loc, null);
			BaseThemeGenerator.convertOrbitWithSpin(fleet, 5f);
			
			int level = 20;

			PersonAPI commander = OfficerManagerEvent.createOfficer(Global.getSector().getFaction(Factions.REMNANTS), level, true);
			commander.getStats().setSkillLevel(Skills.GUNNERY_IMPLANTS, 3);
			FleetFactoryV3.addCommanderSkills(commander, fleet, random);
			fleet.setCommander(commander);
			fleet.getFlagship().setCaptain(commander);
			
			member.getRepairTracker().setCR(member.getRepairTracker().getMaxCR());

			int maxFleets = 2 + random.nextInt(3);
			RemnantStationFleetManager activeFleets = new RemnantStationFleetManager(fleet, 1f, 0, maxFleets, 25f, 6, 12);
			system.addScript(activeFleets);

			Console.showMessage("Remnant Nexus spawned");
			return CommandResult.SUCCESS;
		}

        Console.showMessage("Something went wrong spawning Remnant Nexus");
        return CommandResult.ERROR;
    }
}