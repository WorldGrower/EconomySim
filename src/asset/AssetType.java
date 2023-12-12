/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package asset;

public enum AssetType {
	TOOL(AssetLocation.SIMPLE, "tool", AssetProduce.NONE),
	CASH(AssetLocation.SIMPLE, "cash", AssetProduce.NONE),
	FOOD(AssetLocation.SIMPLE, "food", AssetProduce.NONE, AssetTrait.FOOD_SOURCE, AssetTrait.PERISHABLE),
	WATER(AssetLocation.SIMPLE, "water", AssetProduce.NONE, AssetTrait.LIQUID),
	STONE_SICKLE(AssetLocation.SIMPLE, "stone sickle", 20, 10, new AssetProduce(TOOL)),
	DOG(AssetLocation.SIMPLE, "dog", LivingAssetAttribute.MAX_AGE, 10, new AssetProduce(TOOL), AssetTrait.WATER_CONSUMING),
	CATTLE(AssetLocation.SIMPLE, "cattle", LivingAssetAttribute.MAX_AGE, 10, new AssetProduce(TOOL, FOOD), AssetTrait.WATER_CONSUMING),
	STONE_MORTAR_AND_PESTLE(AssetLocation.SIMPLE, "stone mortar and pestle", 50, 0, new AssetProduce(TOOL)),
	CLAY_POT(AssetLocation.SIMPLE, "clay pot", AssetProduce.NONE, AssetTrait.PERISHABLE_STORAGE),
	GLAZED_POT(AssetLocation.SIMPLE, "glazed pot", AssetProduce.NONE, AssetTrait.LIQUID_STORAGE, AssetTrait.PERISHABLE_STORAGE),
	CAMP_FIRE(AssetLocation.SIMPLE, "camp fire", new AssetProduce(CLAY_POT, GLAZED_POT), AssetTrait.LIGHT_SOURCE, AssetTrait.DELETE_AT_END_OF_DAY),
	KILN(AssetLocation.SIMPLE, "kiln", new AssetProduce(CLAY_POT, GLAZED_POT)),
	ANIMAL_HIDE_CLOTHES(AssetLocation.SIMPLE, "animal hide clothes", AssetProduce.NONE, AssetTrait.ENVIRONMENTAL_PROTECTION),
	COTTON_CLOTHES(AssetLocation.SIMPLE, "cotton clothes", AssetProduce.NONE, AssetTrait.ENVIRONMENTAL_PROTECTION),
	BREAD(AssetLocation.SIMPLE, "bread", AssetProduce.NONE, AssetTrait.FOOD_SOURCE, AssetTrait.PERISHABLE),
	FLOUR(AssetLocation.SIMPLE, "flour", new AssetProduce(BREAD), AssetTrait.PERISHABLE),
	WHEAT(AssetLocation.SIMPLE, "wheat", new AssetProduce(FLOUR), AssetTrait.PERISHABLE),
	WHEAT_FIELD(AssetLocation.LOCATABLE, "wheat field", new AssetProduce(WHEAT)),
	IRRIGATION_CANAL(AssetLocation.PUBLIC, "irrigation canal", AssetProduce.NONE),
	RIVER(AssetLocation.SIMPLE, "river", new AssetProduce(WATER, IRRIGATION_CANAL), AssetTrait.REGENERATIVE),
	PLOUGH(AssetLocation.SIMPLE, "plough", 50, 50, new AssetProduce(TOOL)),
	ACCOUNTING_SYSTEM(AssetLocation.PERSONAL, "accounting system", AssetProduce.NONE, AssetTrait.ACCOUNTING_SYSTEM),
	LAND(AssetLocation.LOCATABLE, "land", new AssetProduce(FOOD, STONE_SICKLE, CAMP_FIRE, ANIMAL_HIDE_CLOTHES, COTTON_CLOTHES, WHEAT, WHEAT_FIELD, STONE_MORTAR_AND_PESTLE, KILN, PLOUGH, ACCOUNTING_SYSTEM, DOG, CATTLE), AssetTrait.REGENERATIVE),
	;
	
	public static final AssetType[] VALUES = values();
	public static final int SIZE = 23;//values().length;
	
	private final AssetLocation assetLocation;
	private final String description;
	private final int numberOfUsesPerQuantity;
	private final int productionBonus;
	private final AssetProduce assetProduce;
	
	private final boolean[] assetTraits = new boolean[AssetTrait.VALUES.length];
	private AssetTrait[] assetTraitsValue = new AssetTrait[0];
	
	private AssetType(AssetLocation assetLocation, String description, AssetProduce assetProduce) {
		this(assetLocation, description, 1, 0, assetProduce);
	}
	
	private AssetType(AssetLocation assetLocation, String description, AssetProduce assetProduce, AssetTrait...assetTraits) {
		this(assetLocation, description, 1, 0, assetProduce);
		initializeAssetTraits(assetTraits);
	}

	private void initializeAssetTraits(AssetTrait... assetTraits) {
		for(AssetTrait assetTrait : assetTraits) {
			this.assetTraits[assetTrait.ordinal()] = true;
		}
		this.assetTraitsValue = assetTraits;
	}
	
	private AssetType(AssetLocation assetLocation, String description, int numberOfUsesPerQuantity, int productionBonus, AssetProduce assetProduce) {
		this.assetLocation = assetLocation;
		this.description = description;
		this.numberOfUsesPerQuantity = numberOfUsesPerQuantity;
		this.productionBonus = productionBonus;
		this.assetProduce = assetProduce;
	}
	
	private AssetType(AssetLocation assetLocation, String description, int numberOfUsesPerQuantity, int productionBonus, AssetProduce assetProduce, AssetTrait...assetTraits) {
		this.assetLocation = assetLocation;
		this.description = description;
		this.numberOfUsesPerQuantity = numberOfUsesPerQuantity;
		this.productionBonus = productionBonus;
		this.assetProduce = assetProduce;
		initializeAssetTraits(assetTraits);
	}

	public final boolean hasTrait(AssetTrait assetTrait) {
		return assetTraits[assetTrait.ordinal()];
	}
	
	public final AssetTrait[] getAssetTraits() {
		return assetTraitsValue;
	}

	public final int getNumberOfUsesPerQuantity() {
		return numberOfUsesPerQuantity;
	}

	public final int getProductionBonus() {
		return productionBonus;
	}

	public final AssetProduce getAssetProduceForQuantity(int quantity) {
		return assetProduce.create(quantity * getNumberOfUsesPerQuantity());
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		return description;
	}

	public boolean isTradeable() {
		return assetLocation == AssetLocation.SIMPLE;
	}
	
	public boolean hasLocation() {
		return assetLocation == AssetLocation.LOCATABLE;
	}
	
	public boolean canBePublic() {
		return assetLocation != AssetLocation.PERSONAL 
			&& this != FOOD && this != WATER && this != FLOUR && this != BREAD && this != CAMP_FIRE && this != ANIMAL_HIDE_CLOTHES && this != COTTON_CLOTHES && this != CLAY_POT && this != GLAZED_POT;
	}
	
	private static enum AssetLocation {
		SIMPLE,
		LOCATABLE, 
		PERSONAL,
		PUBLIC
	}
}
