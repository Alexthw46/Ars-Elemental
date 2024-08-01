package alexthw.ars_elemental.client.patchouli;

//
//public class ElementalArmorProcessor implements IComponentProcessor {
//
//    private ElementalArmorRecipe recipe;
//
//    @Override
//    public void setup(Level level, IVariableProvider variables) {
//        RecipeManager manager = level.getRecipeManager();
//        String recipeID = variables.get("recipe").asString();
//        if (manager.byKey(ResourceLocation.fromNamespaceAndPath(recipeID)).orElse(null) instanceof ElementalArmorRecipe ear)
//            recipe = ear;
//    }
//
//    @Override
//    public @NotNull IVariable process(Level level, String key) {
//        if (recipe == null) return IVariable.empty();
//        if (key.equals("reagent"))
//            return IVariable.wrapList(Arrays.stream(recipe.reagent.getItems()).map(IVariable::from).collect(Collectors.toList()));
//
//        if (key.equals("recipe")) {
//            return IVariable.wrap(recipe.getId().toString());
//        }
//        if (key.equals("tier")) {
//            return IVariable.wrap(recipe.getOutputComponent().getString());
//        }
//        if (key.equals("output")) {
//            return IVariable.from(recipe.result);
//        }
//        if (key.equals("footer")) {
//            return IVariable.wrap(recipe.result.getItem().getDescriptionId());
//        }
//
//        return IVariable.empty();
//    }
//}
