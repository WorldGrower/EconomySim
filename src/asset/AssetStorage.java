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

import java.util.Arrays;
import java.util.List;

class AssetStorage {
	private static final int UNINITIALIZED = -1;
	
	private final int[] remainingStorageQuantity;
	
	public AssetStorage() {
		super();
		this.remainingStorageQuantity = new int[AssetTrait.VALUES.length];
		Arrays.fill(remainingStorageQuantity, UNINITIALIZED);
	}

	public int getRemainingStorageQuantity(Assets assets, AssetTrait assetTrait) {
		if (remainingStorageQuantity[assetTrait.ordinal()] == UNINITIALIZED) {
			remainingStorageQuantity[assetTrait.ordinal()] = calculateInitialStorageQuantity(assets, assetTrait);
		}
		return remainingStorageQuantity[assetTrait.ordinal()];
	}
	
	private static int calculateInitialStorageQuantity(Assets assets, AssetTrait assetTrait) {
		List<Asset> perishableStorageAssets = assets.getAssetsWithTrait(assetTrait);
		int totalPerishableStorage = 0;
		for(Asset perishableStorageAsset : perishableStorageAssets) {
			totalPerishableStorage += perishableStorageAsset.getQuantity();
		}
		return totalPerishableStorage * 100;
	}

	public void consumeStorage(int assetQuantity, AssetTrait assetTrait) {
		if (remainingStorageQuantity[assetTrait.ordinal()] < assetQuantity) {
			throw new IllegalArgumentException("Storage for assetTrait " + assetTrait + " cannot be decreased by " + assetQuantity + " from current " + remainingStorageQuantity[assetTrait.ordinal()]);
		}
		remainingStorageQuantity[assetTrait.ordinal()] -= assetQuantity;
	}
}
