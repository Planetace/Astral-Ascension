package data.console.commands;

import java.util.ArrayList;
import java.util.Collection;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.impl.campaign.procgen.PlanetGenDataSpec;
import com.fs.starfarer.api.impl.campaign.procgen.StarAge;
import com.fs.starfarer.api.impl.campaign.procgen.StarGenDataSpec;
import com.fs.starfarer.api.impl.campaign.procgen.StarSystemGenerator;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;
import org.lwjgl.util.vector.Vector2f;

public class SpawnPlanet extends StarSystemGenerator implements BaseCommand
{
	public SpawnPlanet() {
		super(new StarSystemGenerator.CustomConstellationParams(StarAge.ANY));
	}

	@Override
    public CommandResult runCommand(String args, CommandContext context) {
		if (!context.isInCampaign()) {
			Console.showMessage(CommonStrings.ERROR_CAMPAIGN_ONLY);
			return CommandResult.WRONG_CONTEXT;
		}

		system = (StarSystemAPI) Global.getSector().getCurrentLocation();

		if (system.isHyperspace()) {
			Console.showMessage("Error: This command cannot be used in hyperspace.");
			return CommandResult.WRONG_CONTEXT;
		}

		final ArrayList<String> tmp = parseArgs(args);
		String name = tmp.size() > 0 ? (String)tmp.get(0) : null;
		String id = tmp.size() > 1 ? (String)tmp.get(1) : null;
	   
		star = system.getStar();

		StarGenDataSpec starData = (StarGenDataSpec) Global.getSettings().getSpec(StarGenDataSpec.class, system.getStar().getSpec().getPlanetType(), false);
		StarSystemGenerator.GenContext genContext = new StarSystemGenerator.GenContext(
			this, system, system.getCenter(), starData, null, 0, StarAge.ANY.name(), getRadius(system.getStar()), StarSystemGenerator.MAX_ORBIT_RADIUS, null, -1);
			
		PlanetGenDataSpec planetSpec = getPlanetSpec(genContext, id);

		if (planetSpec == null) {
			Console.showMessage("Error: Could not find planet spec with id " + id);
			return CommandResult.ERROR;
		}

		GenResult addedPlanet = this.addPlanet(genContext, planetSpec, false, false);
		if (name != null && name.length() > 0) {
			((SectorEntityToken)addedPlanet.entities.get(0)).setName(name);
		}

		Console.showMessage("Planet spawned successfully.");
        return CommandResult.SUCCESS;
    }

	private PlanetGenDataSpec getPlanetSpec(StarSystemGenerator.GenContext context, String id) {
		int orbitIndex = context.orbitIndex;
		if (context.parentOrbitIndex >= 0) {
			orbitIndex = context.parentOrbitIndex;
		}
		int fromParentOrbitIndex = context.orbitIndex;
		String starType = null;
		String nebulaType = StarSystemGenerator.NEBULA_DEFAULT;
		if (context.center instanceof PlanetAPI) {
			PlanetAPI star = (PlanetAPI) context.center;
			if (star.isStar()) starType = star.getTypeId();
		}
		
		String parentCategory = context.parentCategory;
	
		WeightedRandomPicker<PlanetGenDataSpec> picker = new WeightedRandomPicker<PlanetGenDataSpec>(StarSystemGenerator.random);

		Collection<Object> planetDataSpecs = Global.getSettings().getAllSpecs(PlanetGenDataSpec.class);
		for (Object obj : planetDataSpecs) {
			PlanetGenDataSpec planetData = (PlanetGenDataSpec) obj;
			if (id != null && id.length() > 0) {			
				if (id.equals(planetData.getId())) {
					return planetData;
				}
				else {
					continue;
				}
			}

			float minIndex = context.starData.getHabZoneStart() + planetData.getHabOffsetMin();
			float maxIndex = context.starData.getHabZoneStart() + planetData.getHabOffsetMax();
			boolean inRightRange = orbitIndex >= minIndex && orbitIndex <= maxIndex;
			boolean giantMoonException = StarSystemGenerator.CAT_GIANT.equals(parentCategory) && 
						(planetData.hasTag(StarSystemGenerator.TAG_GIANT_MOON) && context.parent != null && context.parent.isGasGiant());
			if (!inRightRange && !giantMoonException) continue;
			
			boolean orbitIndexOk = fromParentOrbitIndex == 0 || !planetData.hasTag(StarSystemGenerator.TAG_FIRST_ORBIT_ONLY);
			if (!orbitIndexOk) continue;
			
			boolean lagrangeStatusOk = !planetData.hasTag(StarSystemGenerator.TAG_LAGRANGE_ONLY);
			if (!lagrangeStatusOk) continue;
			
			boolean nebulaStatusOk = StarSystemGenerator.NEBULA_NONE.equals(nebulaType) || !planetData.hasTag(StarSystemGenerator.TAG_NOT_IN_NEBULA);
			nebulaStatusOk &= !StarSystemGenerator.NEBULA_NONE.equals(nebulaType) || !planetData.hasTag(StarSystemGenerator.TAG_REQUIRES_NEBULA);
			nebulaStatusOk &= context.system.getType() != StarSystemType.NEBULA || !planetData.hasTag(StarSystemGenerator.TAG_NOT_NEBULA_UNLESS_MOON) || context.parent != null;
			if (!nebulaStatusOk) continue;
			
			float weight = planetData.getFrequency();
			if (context.age != null) weight *= planetData.getMultiplier(context.age);
			if (starType != null) weight *= planetData.getMultiplier(starType);
			if (parentCategory != null) weight *= planetData.getMultiplier(parentCategory);
			for (String col : context.multipliers) {
				weight *= planetData.getMultiplier(col);
			}
			if (weight > 0) picker.add(planetData, weight);
		}

		return (PlanetGenDataSpec) picker.pick();
	}

	private float getRadius(PlanetAPI star) {
		Vector2f starLoc = star.getLocation();
		Vector2f playerLoc = Global.getSector().getPlayerFleet().getLocation();
		return Misc.getDistance(starLoc, playerLoc);
	}

	private ArrayList<String> parseArgs(String args) {
		boolean quoteStarted = false;
		ArrayList<String> result = new ArrayList<String>();
		String currentString = "";
		for(int i = 0; i < args.length(); i++) {
			if (args.charAt(i) == ' ' && !quoteStarted) {
				result.add(currentString);
				currentString = "";
			}
			else if (args.charAt(i) == '"') {
				quoteStarted = !quoteStarted;
			}
			else {
				currentString = currentString + args.charAt(i);
			}
		}
		
		result.add(currentString);
		return result;
	}
}