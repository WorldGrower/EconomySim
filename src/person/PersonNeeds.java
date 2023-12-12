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
package person;

import java.io.Serializable;
import java.util.List;

import asset.Asset;
import asset.AssetTrait;
import asset.AssetType;
import asset.Assets;

class PersonNeeds implements Serializable {
	public static final int DEFAULT_PRODUCTION = 100;
	
	private static final int FOOD_MAX = 2 * DEFAULT_PRODUCTION;
	private static final int WATER_MAX = 2 * DEFAULT_PRODUCTION;
	
	private int food;
	private int water;
	
	public PersonNeeds() {
		this.food = FOOD_MAX;
		this.water = WATER_MAX;
	}
	
	public void consumeFoodAndWater(Assets[] familyAssetsList) {
		for(Assets assets : familyAssetsList) {
			if (food < FOOD_MAX || water < WATER_MAX) {
				List<Asset> foodAssets = assets.getAssetsWithTrait(AssetTrait.FOOD_SOURCE);
				int foodQuantity = Asset.getQuantityFor(foodAssets);
				if (foodQuantity > 0) {
					int requiredFood = FOOD_MAX - food;
					int foodConsumed = Math.min(foodQuantity, requiredFood);
					food += foodConsumed;
					assets.removeAssetsWithTrait(AssetTrait.FOOD_SOURCE, foodConsumed);
				}
				int waterQuantity = assets.getQuantityFor(AssetType.WATER);
				if (waterQuantity > 0) {
					int requiredWater = WATER_MAX - water;
					int waterConsumed = Math.min(waterQuantity, requiredWater);
					water += waterConsumed;
					assets.removeAsset(AssetType.WATER, waterConsumed);
				}
			}
		}
	}
	
	public CauseOfDeath checkFoodAndWater(Assets assets) {
		int foodWaterconsumption = getFoodWaterConsumption(assets);
		if (food >= foodWaterconsumption) {
			food -= foodWaterconsumption;
			if (water >= foodWaterconsumption) {
				water -= foodWaterconsumption;
			} else {
				return CauseOfDeath.DEHYDRATION;
			}
		} else {
			return CauseOfDeath.STARVATION;
		}
		return null;
	}
	
	private int getFoodWaterConsumption(Assets assets) {
		int consumption = DEFAULT_PRODUCTION;
		if (assets.hasTrait(AssetTrait.ENVIRONMENTAL_PROTECTION)) {
			consumption -= (consumption / 10);
		}
		return consumption;
	}
	
	public String getFoodDescription() {
		return food + " / " + FOOD_MAX;
	}
	
	public String getWaterDescription() {
		return water + " / " + WATER_MAX;
	}

	public boolean hasEnoughFood(Assets assets, Assets[] familyAssetsList) {
		int foodAvailable = 0;
	
		for(Assets familyAssets : familyAssetsList) {
			List<Asset> foodAssets = familyAssets.getAssetsWithTrait(AssetTrait.FOOD_SOURCE);
			int foodQuantity = Asset.getQuantityFor(foodAssets);
			foodAvailable += foodQuantity;
		}
		return food + foodAvailable > getFoodWaterConsumption(assets);
	}

	public boolean hasEnoughWater(Assets assets, Assets[] familyAssetsList) {
		int waterAvailable = 0;
		for(Assets familyAssets : familyAssetsList) {
			int waterQuantity = familyAssets.getQuantityFor(AssetType.WATER);
			waterAvailable += waterQuantity;
		}
		return water + waterAvailable > getFoodWaterConsumption(assets);
	}
}
