
package planetace.astralAscension;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.loading.specs.PlanetSpec;

import java.util.Objects;
import java.awt.Color;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;


// This Class is used for changing colonies to new planet types and stuff.
// Will be removed in 1.3 for a more sophisticated system.
public class AA_ColonyRebuilder implements EconomyAPI.EconomyUpdateListener {

    // Logger
    private static final Logger log = Global.getLogger(AA_ColonyRebuilder.class);
    static { log.setLevel(Level.ALL); }

    @Override
    public void commodityUpdated(String commodityId) { }

    // Planet Changer function. Does all the heavy lifting in regard to changing a planet to something "spicier".
    public void planetChanger(PlanetAPI planet, String typeID, String typeName, String texFilePath,
                              String glowFilePath, Color glowCol,
                              String cloudFilePath, Color cloudCol, Float thick, Integer thickMin, Integer cloudRot,
                              Color atmoCol, String shieldFilePath, Float shieldThick, Color shieldCol, Color iconCol) {

        // Changes Planet Type, Name, Description, and remove Gas Giant classification (If it was).
        ((PlanetSpec) planet.getSpec()).isGasGiant = false;
        ((PlanetSpec) planet.getSpec()).planetType = typeID;
        ((PlanetSpec) planet.getSpec()).name = typeName;
        ((PlanetSpec) planet.getSpec()).descriptionId = typeID;

        // Changes Glow Texture.
        ((PlanetSpec) planet.getSpec()).glowTexture = glowFilePath;
        ((PlanetSpec) planet.getSpec()).glowColor = glowCol;

        // Changes Planet Spec, which is its graphics.
        planet.getSpec().setTexture(texFilePath); // Texture Shit

        planet.getSpec().setCloudTexture(cloudFilePath); // Cloud Shit
        planet.getSpec().setCloudColor(cloudCol);
        planet.getSpec().setCloudRotation(cloudRot);

        planet.getSpec().setAtmosphereColor(atmoCol);  // Atmosphere Shit
        planet.getSpec().setAtmosphereThickness(thick);
        planet.getSpec().setAtmosphereThicknessMin(thickMin);


        // Will ignore shield application if the file-path is null.
        if (shieldFilePath != null){
            planet.getSpec().setShieldTexture2(shieldFilePath);  // Shield Shit
            planet.getSpec().setShieldThickness2(shieldThick);
            planet.getSpec().setShieldColor2(shieldCol);
        } // But it WILL change the shield texture to null if it is.
        else { planet.getSpec().setShieldTexture2(shieldFilePath); }


        planet.getSpec().setIconColor(iconCol);  // Icon Shit

        planet.applySpecChanges();  // Refreshes Planet.
    }

    // Just removes all of the planet identical conditions before re-applying.
    public void conditionsRemove(MarketAPI market) {
        market.removeCondition("pollution");
        market.removeCondition("AA_partial_ecumenopolis");
        market.removeCondition("AA_ecumenopolis");
        market.removeCondition("AA_hyperspacial_ecumenopolis");
        market.removeCondition("AA_matrioshka_world");
        market.removeCondition("AA_astral_world");
    }


    // Update Script, checks when market economy updates to see if it's size has reaches a milestone.
    @Override
    public void economyUpdated() {

        // We loop through every Market in a play-session.
        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            // We assign the planet API to the market planet entity.
            PlanetAPI planet = market.getPlanetEntity();

            // Lesser Ecumonopolis
            // Continent spanning cities, but not entirely planet covering.
            if (market.getSize() >= 11 && market.getSize() <= 12) {

                // We change the planet's appearance. Does a check before-hand.
                if (Objects.equals(planet.getSpec().getTexture(), "graphics/planets/AA_ecumenopolis.jpg")) {
                    //log.debug("Already has Lesser Ecumonopolis texture, not changing.");
                    break;
                }
                else {
                    log.debug("Changing to lesser Ecumenopolis.");

                    // Refreshes condition
                    conditionsRemove(market);
                    market.addCondition("AA_partial_ecumenopolis");
                    market.addCondition("pollution");

                    // testing glow shit
                    planet.getSpec().setUseReverseLightForGlow(false);
                    planet.getSpec().setGlowTexture("graphics/planet/AA_partialEcumLights.png");
                    planet.getSpec().setGlowColor(new Color(255,255,255,255));

                    // PlanetAPI, planetType ID, planet type name, Texture Path's for texture and clouds,
                    // Colour for clouds, Clouds thickness, Colour for Atmosphere,
                    // Shield Texture, Shield thickness, Shield Colour, and Icon Colour.
                    planetChanger(planet,
                            "partialecumenopolis",
                            "Lesser Ecumenopolis",
                            "graphics/planets/AA_ecumenopolis.jpg",
                            "graphics/planets/AA_partialEcumLights.png",
                            new Color(255,255,255,255),
                            "graphics/planets/clouds_banded01.png",
                            new Color(220,190,160,180), 0.1f, 40, -5,
                            new Color(150,150,145,130),
                            null, null, null,
                            new Color(140,140,140,255));

                }
            }

            // Full Ecumenopolis
            // A city spanning the world, every trace of nature is paved over for human expansion.
            else if (market.getSize() >= 13 && market.getSize() <= 14) {
                if (Objects.equals(planet.getSpec().getTexture(), "graphics/planets/AA_fullEcumenopolis.jpg")) {
                    //log.debug("Already has Ecumenopolis texture, not changing.");
                    break;
                }
                else {
                    log.debug("Changing to Ecumenopolis");

                    conditionsRemove(market);
                    market.addCondition("AA_ecumenopolis");
                    market.addCondition("pollution");

                    planetChanger(planet,
                            "fullecumenopolis",
                            "Ecumenopolis",
                            "graphics/planets/AA_fullEcumenopolis.jpg",
                            "graphics/planets/AA_ecumenopolisLights.png",
                            new Color(255,255,255,255),
                            "graphics/planets/clouds_banded01.png",
                            new Color(140,140,160,180),0.1f, 60, -5,
                            new Color(140,140,160,130),
                            null, null, null,
                            new Color(140,120,170,255));
                }
            }

            // Hyperspacial Ecumonopolis
            // With so many people, this Ecumonopolis has begun to expand into Hyperspace to supply the needed living space.
            else if (market.getSize() >= 15 && market.getSize() <= 17) {
                if (Objects.equals(planet.getSpec().getTexture(), "graphics/planets/AA_hyperEcumenopolis.jpg")) {
                    //log.debug("Already has Hyperspacial Ecumonopolis texture, not changing.");
                    break;
                }
                else {
                    log.debug("Changing to Hyperspacial Ecumenopolis");

                    conditionsRemove(market);
                    market.addCondition("AA_hyperspacial_ecumenopolis");
                    market.addCondition("pollution");

                    planetChanger(planet,
                            "hyperecumenopolis",
                            "Hyperspacial Ecumenopolis",
                            "graphics/planets/AA_hyperEcumenopolis.jpg",
                            "graphics/planets/AA_ecumenopolisLights.png",
                            new Color(190, 150, 255,255),
                            "graphics/planets/clouds_banded01.png",
                            new Color(114, 11, 234, 82),0.1f,80, -5,
                            new Color(94, 28, 169, 147),
                            "graphics/planets/AA_hyperspaceLeakage.png", 0.05f,
                            new Color(255, 255, 255,255),
                            new Color(75,50,100,255));
                }
            }

            // Matrioshka World
            // A world of multiple layers, many never seeing the natural light of day.
            else if (market.getSize() >= 18 && market.getSize() <= 20) {
                if (Objects.equals(planet.getSpec().getTexture(), "graphics/planets/AA_matrioshka.jpg")) {
                    //log.debug("Already has Matrioshka texture, not changing.");
                    break;
                }
                else {
                    log.debug("Changing to Matrioshka World");

                    conditionsRemove(market);
                    market.addCondition("AA_matrioshka_world");

                    planetChanger(planet,
                            "matrioshkaworld",
                            "Matrioshka",
                            "graphics/planets/AA_hyperEcumenopolis.jpg",
                            "graphics/planets/AA_ecumenopolisLights.png",
                            new Color(190, 150, 255,255),
                            "graphics/planets/AA_matrioshkaLayer.png",
                            // Since the layer is ACTUALLY solid, it needs to have 255 in everything.
                            new Color(255, 255, 255,255),0.1f,80, 2,
                            new Color(20, 110, 230,200),
                            "graphics/planets/AA_matrioshkaLayer2.png", 0.15f,
                            new Color(255, 255, 255,255),
                            new Color(6, 61, 119,255));
                }
            }

            // Astral Singularity
            // And God said, Let there be light: and there was light.
            // And God saw the light, and it was good, and God divided the light from the darkness.
            else if (market.getSize() >= 21) {
                if (Objects.equals(planet.getSpec().getTexture(), "graphics/planets/AA_astralSingularity.jpg")) {
                    //log.debug("Already has Astral texture, not changing.");
                    break;
                }
                else {
                    log.debug("Changing to Astral World");

                    conditionsRemove(market);
                    market.addCondition("AA_astral_world");

                    planetChanger(planet,
                            "astralworld",
                            "Astral",
                            "graphics/planets/AA_astral.jpg",
                            "graphics/planets/AA_astralStars.png",
                            new Color(255,255,255,255),
                            "graphics/planets/AA_astralStars.png",
                            new Color(255, 255, 255,255),0.2f,120, 5,
                            new Color(255, 255, 255,255),
                            "graphics/planets/AA_astralStars.png", 0.05f,
                            new Color(255,255,255,255),
                            new Color(30, 23, 42, 255));
                }
            }
        }
    }

    @Override
    public boolean isEconomyListenerExpired() {
        return false;
    }
}