package data.console.commands;

import java.util.Arrays;
import java.util.Random;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CharacterDataAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.FullName.Gender;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.events.OfficerManagerEvent;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import org.lazywizard.console.BaseCommand;
import org.lazywizard.console.CommonStrings;
import org.lazywizard.console.Console;
import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.console.CommandUtils;

public class AddAdmin implements BaseCommand
{
    @Override
    public CommandResult runCommand(String args, CommandContext context)
    {
        if (!context.isInCampaign())
        {
            Console.showMessage(CommonStrings.ERROR_CAMPAIGN_ONLY);
            return CommandResult.WRONG_CONTEXT;
        }

        if (args.isEmpty())
        {
            return runCommand("0 " + Factions.PLAYER, context);
        }

        final String[] tmp = args.split(" ");
        FullName name = null;
        switch (tmp.length)
        {
            case 1:
                return runCommand(args + " " + Factions.PLAYER, context);
            case 2:
                break;
            // Custom name support
            case 3:
                name = new FullName(tmp[2], "", Gender.MALE);
                break;
            default:
                name = new FullName(tmp[2], CollectionUtils.implode(Arrays.asList(
                        Arrays.copyOfRange(tmp, 4, tmp.length)), " "), Gender.MALE);
        }

        if (!CommandUtils.isInteger(tmp[0]))
        {
            Console.showMessage("Error: admin tier must be a whole number from 0 to 2!");
            return CommandResult.BAD_SYNTAX;
        }

        final int tier = Math.min(2, Math.max(0, Integer.parseInt(tmp[0])));

        final FactionAPI faction = CommandUtils.findBestFactionMatch(tmp[1]);
        if (faction == null)
        {
            Console.showMessage("No faction found with id '" + tmp[1] + "'!");
            return CommandResult.ERROR;
        }

        final PersonAPI person = OfficerManagerEvent.createAdmin(faction, tier, new Random());
        final CharacterDataAPI characterData = Global.getSector().getCharacterData();
        if (name != null)
        {
            person.setName(name);
        }
        characterData.addAdmin(person);

        Console.showMessage("Created tier " + tier + " administrator " + person.getName().getFullName() + ".");
        return CommandResult.SUCCESS;
    }
}
