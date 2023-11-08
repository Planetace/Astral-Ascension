package planetace.astralAscension;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;

import java.util.Objects;
import java.awt.Color;

import com.fs.starfarer.loading.specs.PlanetSpec;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


// This Class is used for colony planet class changing and Graphics alteration.
// If colonies reach a certain size, their planet class and texture change with them.
// Size 11 = Ecumonopolis, size 13 = full Ecumonopolis, size 15 = Hyperspace Shrouded Ecumonopolis,
// size 18 = Hyperspatial Matrioshka World, size 21 = Mini Dyson Sphere, size 25 = Singularity World.
public class AA_ColonyGraphicsSwitch implements EconomyAPI.EconomyUpdateListener {

    // Logger
    private static final Logger log = Global.getLogger(AA_ColonyGraphicsSwitch.class);
    static { log.setLevel(Level.ALL); }

    @Override
    public void commodityUpdated(String commodityId) { }


    // Planet Changer function. Does all the heavy lifting in regard to changing a planet to something "spicier".
    public void planetChanger(PlanetAPI planet, String typeID, String typeName, String texFilePath,
                              String cloudFilePath, Color cloudCol, Float thick, Integer thickMin, Integer cloudRot, Color atmoCol, Color iconCol) {
        // Changes Planet Type.
        ((PlanetSpec) planet.getSpec()).planetType = typeID;
        ((PlanetSpec) planet.getSpec()).name = typeName;

        // Changes Planet Spec, which is its graphics.
        planet.getSpec().setTexture(texFilePath); // Texture Shit
        planet.getSpec().setCloudTexture(cloudFilePath); // Cloud Shit
        planet.getSpec().setCloudColor(cloudCol);
        planet.getSpec().setAtmosphereThickness(thick);
        planet.getSpec().setAtmosphereThicknessMin(thickMin);
        planet.getSpec().setCloudRotation(cloudRot);
        planet.getSpec().setAtmosphereColor(atmoCol);  // Atmosphere Shit
        planet.getSpec().setIconColor(iconCol);  // Icon Shit

        planet.applySpecChanges();  // Refresh Spec Changes.
    }

    // Update Script, checks when market economy updates to see if it's size has reaches a milestone.
    @Override
    public void economyUpdated() {

        // We loop through every Market in a play-session.
        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            // We assign the planet API to the market planet entity.
            PlanetAPI planet = market.getPlanetEntity();

            //PlanetSpecAPI planetSpec = planet.getSpec();

            // Lesser Ecumonopolis
            // Continent spanning cities, but not entirely planet covering.
            if (market.getSize() > 10 && market.getSize() < 13) {

                // We change the planet's appearance. Does a check before-hand.
                if (Objects.equals(planet.getSpec().getTexture(), "graphics/planets/AA_Ecumonopolis.jpg")) {
                    log.debug("Already has Lesser Ecumonopolis texture, not changing.");
                }
                else {
                    log.debug("Changing to lesser Ecumonopolis.");

                    // PlanetAPI, planetType ID, planet type name, Texture Path's for texture and clouds,
                    // Colour for clouds, Clouds thickness, Colour for Atmosphere, and Icon Colour.
                    planetChanger(planet,
                            "ecumonopolis",
                            "Lesser Ecumonopolis",
                            "graphics/planets/AA_Ecumonopolis.jpg",
                            "graphics/planets/clouds_banded01.png",
                            new Color(220,190,160,180), 0.1f, 40, -5,
                            new Color(150,150,145,130),
                            new Color(140,140,140,255));
                }
            }

            // Full Ecumonopolis
            // A city spanning the world, every trace of nature is paved over for human expansion.
            else if (market.getSize() > 12 && market.getSize() < 15) {
                if (Objects.equals(planet.getSpec().getTexture(), "graphics/planets/AA_fullEcumonopolis.jpg")) {
                    log.debug("Already has Ecumonopolis texture, not changing.");
                }
                else {
                    log.debug("Changing to Ecumonopolis");
                    planetChanger(planet,
                            "fullecumonopolis",
                            "Ecumonopolis",
                            "graphics/planets/AA_fullEcumonopolis.jpg",
                            "graphics/planets/clouds_banded01.png",
                            new Color(140,140,160,180),0.1f, 60, -5,
                            new Color(140,140,160,130),
                            new Color(140,120,170,255));
                }
            }

            // Hyperspacial Ecumonopolis
            // With so many people, this Ecumonopolis has begun to expand into Hyperspace to supply the needed living space.
            else if (market.getSize() > 14 && market.getSize() < 18) {
                if (Objects.equals(planet.getSpec().getTexture(), "graphics/planets/AA_hyperEcumonopolis.jpg")) {
                    log.debug("Already has Hyperspacial Ecumonopolis texture, not changing.");
                }
                else {
                    log.debug("Changing to Hyperspacial Ecumonopolis");
                    planetChanger(planet,
                            "hyperecumonopolis",
                            "Hyperspacial Ecumonopolis",
                            "graphics/planets/AA_hyperEcumonopolis.jpg",
                            "graphics/planets/clouds_banded01.png",
                            new Color(130, 55, 220,50),0.1f,80, -5,
                            new Color(130,0,255,200),
                            new Color(75,50,100,255));
                }
            }

            // Matrioshka World
            // A world of multiple layers, many never seeing the natural light of day.
            else if (market.getSize() > 17 && market.getSize() < 21) {
                if (Objects.equals(planet.getSpec().getTexture(), "graphics/planets/AA_matrioshka.jpg")) {
                    log.debug("Already has Matrioshka texture, not changing.");
                }
                else {
                    log.debug("Changing to Matrioshka World");
                    planetChanger(planet,
                            "matrioshka",
                            "Matrioshka World",
                            "graphics/planets/AA_matrioshka.jpg",
                            "graphics/planets/AA_matrioshkaLayer.png",
                            // Since the layer is ACTUALLY solid, it needs to have 255 in everything.
                            new Color(255, 255, 255,255),0.1f,80, -7,
                            new Color(20, 110, 230,200),
                            new Color(110, 90, 255,255));
                }
            }
            // Astral Singularity
            // And God said, Let there be light: and there was light.
            // And God saw the light, and it was good, and God divided the light from the darkness.
            else if (market.getSize() > 20) {
                if (Objects.equals(planet.getSpec().getTexture(), "graphics/planets/AA_astralSingularity.jpg")) {
                    log.debug("Already has Astral texture, not changing.");
                }
                else {
                    log.debug("Changing to AstralWorld");
                    planetChanger(planet,
                            "astral",
                            "Astral",
                            "graphics/planets/AA_astral.jpg",
                            "graphics/planets/asharu_sparse_glow.png",
                            new Color(255, 255, 255,255),0.2f,120, -7,
                            new Color(255, 255, 255,255),
                            new Color(110, 90, 255,255));
                }
            }
        }
    }

    @Override
    public boolean isEconomyListenerExpired() {
        return false;
    }
}