package com.cleanroommc.groovyscript.compat.vanilla;

import com.cleanroommc.groovyscript.GroovyScript;
import com.cleanroommc.groovyscript.api.GroovyBlacklist;
import com.cleanroommc.groovyscript.api.IScriptReloadable;
import com.cleanroommc.groovyscript.compat.content.Content;
import com.cleanroommc.groovyscript.compat.inworldcrafting.InWorldCrafting;
import com.cleanroommc.groovyscript.compat.loot.Loot;
import com.cleanroommc.groovyscript.compat.mods.GroovyPropertyContainer;
import com.cleanroommc.groovyscript.sandbox.expand.ExpansionHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Collections;

public class VanillaModule extends GroovyPropertyContainer implements IScriptReloadable {

    public static final VanillaModule INSTANCE = new VanillaModule();

    public static final Crafting crafting = new Crafting();
    public static final Furnace furnace = new Furnace();
    public static final Loot loot = new Loot();
    public static final OreDict oreDict = new OreDict();
    public static final Player player = new Player();
    public static final Content content = new Content();
    public static final Rarity rarity = new Rarity();
    public static final InWorldCrafting inWorldCrafting = new InWorldCrafting();
    public static final Command command = new Command();
    public static final GameRule gameRule = new GameRule();

    public static void initializeBinding() {
        GroovyScript.getSandbox().registerBinding(crafting);
        GroovyScript.getSandbox().registerBinding(furnace);
        GroovyScript.getSandbox().registerBinding(loot);
        GroovyScript.getSandbox().registerBinding(oreDict);
        GroovyScript.getSandbox().registerBinding(player);
        GroovyScript.getSandbox().registerBinding(content);
        GroovyScript.getSandbox().registerBinding(rarity);
        GroovyScript.getSandbox().registerBinding(inWorldCrafting);
        GroovyScript.getSandbox().registerBinding(command);
        GroovyScript.getSandbox().registerBinding(gameRule);

        ExpansionHelper.mixinClass(ItemStack.class, ItemStackExpansion.class);
        ExpansionHelper.mixinClass(ICommandSender.class, CommandSenderExpansion.class);
    }

    @Override
    @GroovyBlacklist
    public void onReload() {
        crafting.onReload();
        furnace.onReload();
        loot.onReload();
        oreDict.onReload();
        rarity.onReload();
        player.onReload();
        inWorldCrafting.onReload();
        command.onReload();
        gameRule.onReload();
    }

    @Override
    @GroovyBlacklist
    public void afterScriptLoad() {
        furnace.afterScriptLoad();
        loot.afterScriptLoad();
        inWorldCrafting.afterScriptLoad();
    }

    @Override
    public Collection<String> getAliases() {
        return Collections.emptyList();
    }
}
