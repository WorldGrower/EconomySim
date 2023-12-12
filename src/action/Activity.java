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
package action;

import static asset.AssetType.ACCOUNTING_SYSTEM;
import static asset.AssetType.ANIMAL_HIDE_CLOTHES;
import static asset.AssetType.BREAD;
import static asset.AssetType.CAMP_FIRE;
import static asset.AssetType.CATTLE;
import static asset.AssetType.CLAY_POT;
import static asset.AssetType.COTTON_CLOTHES;
import static asset.AssetType.DOG;
import static asset.AssetType.FLOUR;
import static asset.AssetType.FOOD;
import static asset.AssetType.GLAZED_POT;
import static asset.AssetType.IRRIGATION_CANAL;
import static asset.AssetType.KILN;
import static asset.AssetType.LAND;
import static asset.AssetType.PLOUGH;
import static asset.AssetType.RIVER;
import static asset.AssetType.STONE_MORTAR_AND_PESTLE;
import static asset.AssetType.STONE_SICKLE;
import static asset.AssetType.WATER;
import static asset.AssetType.WHEAT;
import static asset.AssetType.WHEAT_FIELD;

import asset.Asset;
import asset.AssetType;
import asset.Assets;
import asset.Tools;
import environment.PublicLocations;
import knowledge.Knowledge;
import society.PublicAssets;

public enum Activity {

	GATHER_FOOD(new AssetExtraction(LAND, FOOD, 100), 4, "gather food", Knowledge.NONE, STONE_SICKLE, DOG),
	GATHER_WATER(new AssetExtraction(RIVER, WATER, 100), 1, "gather water", Knowledge.NONE),
	CREATE_STONE_SICKLE(new AssetExtraction(LAND, STONE_SICKLE, 1), 2, "create stone sickle", Knowledge.STONE_TOOLING),
	CREATE_DOG(new LivingAssetGeneration(LAND, DOG, 1), 2, "create dog", Knowledge.DOMESTICATION_OF_DOG),
	CREATE_CATTLE(new LivingAssetGeneration(LAND, CATTLE, 1), 2, "create cattle", Knowledge.DOMESTICATION_OF_CATTLE),
	SLAUGHTER_CATTLE(new LivingAssetDestruction(CATTLE, FOOD, 400, attr -> attr.getAge() >= 3), 1, "slaughter cattle", Knowledge.NONE),
	CREATE_FIRE(new AssetExtraction(LAND, CAMP_FIRE, 1), 1, "create fire", Knowledge.CONTROL_OF_FIRE),
	CREATE_CLAY_POT(new AssetExtraction(CAMP_FIRE, CLAY_POT, 1), 4, "create clay pot", Knowledge.POTTERY),
	CREATE_ANIMAL_HIDE_CLOTHING(new AssetExtraction(LAND, ANIMAL_HIDE_CLOTHES, 1), 3, "create animal hide clothes", Knowledge.CLOTHING),
	CREATE_COTTON_CLOTHING(new AssetExtraction(LAND, COTTON_CLOTHES, 1), 3, "create cotton clothes", Knowledge.WEAVING),
	GATHER_WHEAT(new AssetExtraction(LAND, WHEAT, 5), 2, "gather wheat", Knowledge.NONE),
	CREATE_FLOUR(new AssetTransformation(WHEAT, FLOUR, 100), 2, "create flour", Knowledge.NONE, STONE_MORTAR_AND_PESTLE),
	CREATE_BREAD(new AssetTransformation(FLOUR, BREAD, 100), 2, "create bread", Knowledge.COOKING),
	PLANT_WHEAT(new AssetCreation(WHEAT_FIELD, LAND, WHEAT, 100), 8, "plant wheat", Knowledge.FARMING, PLOUGH, CATTLE),
	HARVEST_WHEAT(new LocatableAssetDestruction(WHEAT_FIELD, WHEAT, 100, attr -> attr.getAge() >= 3), 8, "harvest wheat", Knowledge.FARMING, STONE_SICKLE),
	CREATE_MORTAR_AND_PESTLE(new AssetExtraction(LAND, STONE_MORTAR_AND_PESTLE, 1), 2, "create stone mortar and pestle", Knowledge.STONE_TOOLING),
	CREATE_GLAZED_POT(new AssetExtraction(CAMP_FIRE, GLAZED_POT, 1), 4, "create glazed pot", Knowledge.LEAD_SMELTING),
	CREATE_KILN(new AssetExtraction(LAND, KILN, 6), 4, "create kiln", Knowledge.KILN),
	CREATE_IRRIGATION_CANAL(new OperationalAssetCreation(RIVER, IRRIGATION_CANAL, AssetOwner.PUBLIC), 8, "start irrigation canal construction", Knowledge.IRRIGATION),
	CREATE_ACCOUNTING_SYSTEM(new AssetExtraction(LAND, ACCOUNTING_SYSTEM, 1), 4, "create accounting system", Knowledge.PROTO_WRITING),
	CREATE_PLOUGH(new AssetExtraction(LAND, PLOUGH, 1), 2, "create plough", Knowledge.PLOUGHING),
	;
	
	public static final Activity[] VALUES = Activity.values();
	
	private final AssetAction assetAction;
	private final AssetType[] tools;
	private final int timeRequired;
	private final String name;
	private final Knowledge requiredKnowledge;
	
	private Activity(AssetAction assetAction, int timeRequired, String name, Knowledge requiredKnowledge, AssetType... tools) {
		this.assetAction = assetAction;
		this.timeRequired = timeRequired;
		this.name = name;
		this.requiredKnowledge = requiredKnowledge;
		this.tools = tools;
	}

	public AssetType[] getTools() {
		return tools;
	}

	public AssetType getProvidedAssetType() {
		return assetAction.getProvidedAssetType();
	}

	public AssetType getAssetType() {
		return assetAction.getAssetType();
	}

	public int getTimeRequired() {
		return timeRequired;
	}

	public String getName() {
		return name;
	}

	public Knowledge getRequiredKnowledge() {
		return requiredKnowledge;
	}

	public boolean isApplicableTo(Asset asset) {
		return assetAction.getAssetType() == asset.getAssetType();
	}

	public void perform(Assets assets, Tools tools, PublicAssets publicAssets, PublicLocations publicLocations) {
		assetAction.perform(assets, tools, publicAssets, publicLocations);
	}

	public boolean canPerformAssetAction(Assets assets, PublicLocations publicLocations) {
		return assetAction.canPerform(assets, publicLocations);
	}
	
	public TimeRequiredPerProducedAsset calculateTimeRequiredPerProducedAsset() {
		//TODO: + tooling & capacity bonus
		return assetAction.calculateTimeRequiredPerProducedAsset(timeRequired);
	}
}
