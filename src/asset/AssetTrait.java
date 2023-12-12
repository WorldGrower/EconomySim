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

import society.PublicAssets;

public enum AssetTrait {
	LIGHT_SOURCE,
	DELETE_AT_END_OF_DAY {
		@Override
		public void endOfTurn(Assets assets, Asset asset, PublicAssets publicAssets, AssetStorage assetStorage) {
			assets.remove(asset.getAssetType());
		}
	},
	REGENERATIVE,
	PERISHABLE {
		@Override
		public void endOfTurn(Assets assets, Asset asset, PublicAssets publicAssets, AssetStorage assetStorage) {
			removePerishableAssetWithoutStorage(assets, asset, AssetTrait.PERISHABLE_STORAGE, assetStorage);
		}
	},
	PERISHABLE_STORAGE,
	ENVIRONMENTAL_PROTECTION,
	FOOD_SOURCE,
	LIQUID {
		@Override
		public void endOfTurn(Assets assets, Asset asset, PublicAssets publicAssets, AssetStorage assetStorage) {
			removePerishableAssetWithoutStorage(assets, asset, AssetTrait.LIQUID_STORAGE, assetStorage);
		}
	},
	LIQUID_STORAGE, 
	ACCOUNTING_SYSTEM,
	WATER_CONSUMING {
		
		@Override
		public void endOfTurn(Assets assets, Asset asset, PublicAssets publicAssets, AssetStorage assetStorage) {
			Asset riverAsset = publicAssets.get(AssetType.RIVER);
			int waterNeeded = 50 * asset.getQuantity();
			int waterConsumed = riverAsset.useRemainingProduce(AssetType.WATER, waterNeeded);
			if (waterConsumed < waterNeeded) {
				final int numberOfAssetsRemoved;
				if (waterConsumed > 0) {
					numberOfAssetsRemoved = ((waterNeeded - waterConsumed) / 50) + 1;
				} else {
					numberOfAssetsRemoved = asset.getQuantity();
				}
				
				assets.removeAsset(asset.getAssetType(), numberOfAssetsRemoved);
			}
		}
	}
	;
	
	public void endOfTurn(Assets assets, Asset asset, PublicAssets publicAssets, AssetStorage assetStorage) {
	}
	
	private static void removePerishableAssetWithoutStorage(Assets assets, Asset asset, AssetTrait storageTrait, AssetStorage assetStorage) {
		int storageQuantity = assetStorage.getRemainingStorageQuantity(assets, storageTrait);
		int assetQuantity = asset.getQuantity();
		if (assetQuantity > storageQuantity) {
			assets.removeAsset(asset.getAssetType(), assetQuantity - storageQuantity);
			assetStorage.consumeStorage(storageQuantity, storageTrait);
		} else {
			assetStorage.consumeStorage(assetQuantity, storageTrait);
		}
	}
	
	public static final AssetTrait[] VALUES = AssetTrait.values();
}
