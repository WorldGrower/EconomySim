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

import java.util.List;

import asset.Asset;
import asset.AssetTrait;
import asset.AssetType;

class SurplusAssetCalculator {

	private final static AssetType[] NEEDED_ASSET_TYPES = { AssetType.COTTON_CLOTHES, AssetType.GLAZED_POT, AssetType.BREAD, AssetType.CATTLE};
	
	public static SurplusAsset calculateSurplusAsset(Person person) {
		AssetType surplusAssetType = calculateSurplusAssetType(person);
		AssetType neededAssetType = calculateNeededAssetType(person);
		
		return new SurplusAsset(surplusAssetType, neededAssetType);
	}
	
	private static AssetType calculateNeededAssetType(Person person) {
		for(AssetType neededAssetType : NEEDED_ASSET_TYPES) {
			if (person.getAssets().getQuantityFor(neededAssetType) == 0) {
				return neededAssetType;
			}
		}
		return null;
	}

	private static AssetType calculateSurplusAssetType(Person person) {
		List<Asset> assets = person.getAssets().getAssetsWithTrait(AssetTrait.ENVIRONMENTAL_PROTECTION);
		for(Asset asset : assets) {
			if (asset.getQuantity() > 1) {
				return asset.getAssetType();
			}
		}

		assets = person.getAssets().getAssetsWithTrait(AssetTrait.PERISHABLE_STORAGE);
		for(Asset asset : assets) {
			if (asset.getQuantity() > 10) {
				return asset.getAssetType();
			}
		}
		assets = person.getAssets().getAssetsWithTrait(AssetTrait.LIQUID_STORAGE);
		for(Asset asset : assets) {
			if (asset.getQuantity() > 10) {
				return asset.getAssetType();
			}
		}
		return null;
	}
	
	static class SurplusAsset {
		private final AssetType surplusAssetType;
		private final AssetType neededAssetType;
		
		public SurplusAsset(AssetType surplusAssetType, AssetType neededAssetType) {
			super();
			this.surplusAssetType = surplusAssetType;
			this.neededAssetType = neededAssetType;
		}

		public AssetType getSurplusAssetType() {
			return surplusAssetType;
		}

		public AssetType getNeededAssetType() {
			return neededAssetType;
		}
	}
}
