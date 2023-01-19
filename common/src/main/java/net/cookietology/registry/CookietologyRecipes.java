package net.cookietology.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.cookietology.Cookietology;
import net.cookietology.item.crafting.IBakingRecipe;
import net.cookietology.item.crafting.IMixingRecipe;
import net.cookietology.item.crafting.MixingRecipe;
import net.cookietology.item.crafting.special.CookieBakingRecipe;
import net.cookietology.item.crafting.special.DoughBrillianceRecipe;
import net.cookietology.item.crafting.special.DoughCompressingRecipe;
import net.cookietology.item.crafting.special.DoughMoldingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public class CookietologyRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Cookietology.MODID, Registry.RECIPE_SERIALIZER_REGISTRY);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Cookietology.MODID, Registry.RECIPE_TYPE_REGISTRY);

    public static final RegistrySupplier<RecipeSerializer<MixingRecipe>> MIXING_SERIALIZER = RECIPE_SERIALIZERS.register("mixing", MixingRecipe.Serializer::new);
    public static final RegistrySupplier<RecipeSerializer<DoughCompressingRecipe>> DOUGH_COMPRESSING_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_special_doughcompressing", () -> new SimpleRecipeSerializer<>(DoughCompressingRecipe::new));
    public static final RegistrySupplier<RecipeSerializer<DoughMoldingRecipe>> DOUGH_MOLDING_SERIALIZER = RECIPE_SERIALIZERS.register("crafting_special_doughmolding", () -> new SimpleRecipeSerializer<>(DoughMoldingRecipe::new));
    public static final RegistrySupplier<RecipeSerializer<DoughBrillianceRecipe>> DOUGH_BRILLIANCE_SERIALIZER = RECIPE_SERIALIZERS.register("mixing_special_doughbrilliance", () -> new SimpleRecipeSerializer<>(DoughBrillianceRecipe::new));
    public static final RegistrySupplier<RecipeSerializer<CookieBakingRecipe>> COOKIE_BAKING_SERIALIZER = RECIPE_SERIALIZERS.register("baking_special_cookie", () -> new SimpleRecipeSerializer<>(CookieBakingRecipe::new));

    //public static final RecipeType<IMixingRecipe> MIXING = RecipeType.register(Cookietology.MODID + ":mixing");
    //public static final RecipeType<IBakingRecipe> BAKING = RecipeType.register(Cookietology.MODID + ":baking");

    public static final RegistrySupplier<RecipeType<IMixingRecipe>> MIXING = RECIPE_TYPES.register("mixing", () -> new RecipeType<IMixingRecipe>() {
        @Override
        public String toString() {
            return "mixing";
        }
    });

    public static final RegistrySupplier<RecipeType<IBakingRecipe>> BAKING = RECIPE_TYPES.register("baking", () -> new RecipeType<IBakingRecipe>() {
        @Override
        public String toString() {
            return "baking";
        }
    });
}