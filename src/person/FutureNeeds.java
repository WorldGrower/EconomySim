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

import asset.AssetType;
import asset.Assets;

class FutureNeeds {

	private static final AssetType[] FOOD_SOURCES = { AssetType.FOOD, AssetType.BREAD };
	
	private final Assets assets;
	private final Assets[] familyAssets;
	private final PersonNeeds needs;
	
	public FutureNeeds(Person person, PersonNeeds needs) {
		this.assets = person.getAssets();
		this.familyAssets = person.getFamily().getFamilyAssets(person.getAssets());
		this.needs = needs;
	}
	
	private boolean hasEnoughFood() {
		return needs.hasEnoughFood(assets, familyAssets);
	}
	
	private boolean hasEnoughWater() {
		return needs.hasEnoughWater(assets, familyAssets);
	}
	
	public void checkNeeds(Person person, NeedsNotMetAction needsNotMetAction) {
		if (!hasEnoughFood()) {//TODO: generalize assettypes
			for(AssetType foodSource : FOOD_SOURCES) {
				if (needsNotMetAction.perform(person, foodSource)) {
					break;
				}
			}
		}
		if (!hasEnoughWater()) {//TODO: generalize assettypes
			needsNotMetAction.perform(person, AssetType.WATER);
		}
	}
	
	static interface NeedsNotMetAction {
		public boolean perform(Person person, AssetType assetType);
	}
}
