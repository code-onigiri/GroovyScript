package com.cleanroommc.groovyscript.compat.mods.mekanism;

import com.cleanroommc.groovyscript.api.GroovyLog;
import com.cleanroommc.groovyscript.api.IIngredient;
import com.cleanroommc.groovyscript.api.documentation.annotations.*;
import com.cleanroommc.groovyscript.compat.mods.ModSupport;
import com.cleanroommc.groovyscript.compat.mods.mekanism.recipe.GasRecipeBuilder;
import com.cleanroommc.groovyscript.compat.mods.mekanism.recipe.VirtualizedMekanismRegistry;
import com.cleanroommc.groovyscript.helper.Alias;
import com.cleanroommc.groovyscript.helper.ingredient.IngredientHelper;
import mekanism.api.gas.GasStack;
import mekanism.common.recipe.RecipeHandler;
import mekanism.common.recipe.inputs.AdvancedMachineInput;
import mekanism.common.recipe.machines.InjectionRecipe;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@RegistryDescription
public class InjectionChamber extends VirtualizedMekanismRegistry<InjectionRecipe> {

    public InjectionChamber() {
        super(RecipeHandler.Recipe.CHEMICAL_INJECTION_CHAMBER, Alias.generateOfClassAnd(InjectionChamber.class, "Injector"));
    }

    @RecipeBuilderDescription(example = @Example(value = ".input(item('minecraft:diamond')).gasInput(gas('water'))/*()!*/.output(item('minecraft:nether_star'))", annotations = "groovyscript.wiki.mekanism.injection_chamber.annotation"))
    public RecipeBuilder recipeBuilder() {
        return new RecipeBuilder();
    }

    @MethodDescription(type = MethodDescription.Type.ADDITION, example = @Example(value = "item('minecraft:diamond'), gas('water'), item('minecraft:nether_star')", commented = true))
    public InjectionRecipe add(IIngredient ingredient, GasStack gasInput, ItemStack output) {
        return recipeBuilder().gasInput(gasInput).output(output).input(ingredient).register();
    }

    @MethodDescription(example = @Example("item('minecraft:hardened_clay'), gas('water')"))
    public boolean removeByInput(IIngredient ingredient, GasStack gasInput) {
        GroovyLog.Msg msg = GroovyLog.msg("Error removing Mekanism Injection Chamber recipe").error();
        msg.add(IngredientHelper.isEmpty(ingredient), () -> "input must not be empty");
        msg.add(Mekanism.isEmpty(gasInput), () -> "gas input must not be empty");
        if (msg.postIfNotEmpty()) return false;

        boolean found = false;
        for (ItemStack itemStack : ingredient.getMatchingStacks()) {
            InjectionRecipe recipe = recipeRegistry.get().remove(new AdvancedMachineInput(itemStack, gasInput.getGas()));
            if (recipe != null) {
                addBackup(recipe);
                found = true;
            }
        }
        if (!found) {
            removeError("could not find recipe for {} and {}", ingredient, gasInput);
        }
        return found;
    }

    @Property(property = "input", comp = @Comp(eq = 1))
    @Property(property = "output", comp = @Comp(eq = 1))
    @Property(property = "gasInput", comp = @Comp(eq = 1))
    public static class RecipeBuilder extends GasRecipeBuilder<InjectionRecipe> {

        @Override
        public String getErrorMsg() {
            return "Error adding Mekanism Injection Chamber recipe";
        }

        @Override
        public void validate(GroovyLog.Msg msg) {
            validateItems(msg, 1, 1, 1, 1);
            validateFluids(msg);
            validateGases(msg, 1, 1, 0, 0);
        }

        @Override
        @RecipeBuilderRegistrationMethod
        public @Nullable InjectionRecipe register() {
            if (!validate()) return null;
            InjectionRecipe recipe = null;
            for (ItemStack itemStack : input.get(0).getMatchingStacks()) {
                InjectionRecipe r = new InjectionRecipe(itemStack, gasInput.get(0).getGas(), output.get(0));
                if (recipe == null) recipe = r;
                ModSupport.MEKANISM.get().injectionChamber.add(r);
            }
            return recipe;
        }
    }
}
