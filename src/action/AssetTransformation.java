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

import asset.AssetType;
import asset.Assets;
import asset.SimpleAsset;
import asset.Tools;
import environment.PublicLocations;
import society.PublicAssets;

class AssetTransformation implements AssetAction {
	private final AssetType oldAssetType;
	private final AssetType newAssetType;
	private final int maxQuantityTransformed;
	
	public AssetTransformation(AssetType oldAssetType, AssetType newAssetType, int maxQuantityTransformed) {
		super();
		this.oldAssetType = oldAssetType;
		this.newAssetType = newAssetType;
		this.maxQuantityTransformed = maxQuantityTransformed;
	}

	@Override
	public AssetType getAssetType() {
		return oldAssetType;
	}

	@Override
	public AssetType getProvidedAssetType() {
		return newAssetType;
	}

	@Override
	public void perform(Assets assets, Tools tools, PublicAssets publicAssets, PublicLocations publicLocations) {
		int quantityProvided = Math.min(assets.getQuantityFor(oldAssetType), maxQuantityTransformed);
		if (quantityProvided > 0) {
			int numberOfAssetsProduced = tools.calculateProduction(quantityProvided);
			assets.addAsset(new SimpleAsset(newAssetType, numberOfAssetsProduced));
			assets.removeAsset(oldAssetType, quantityProvided);
		}
	}

	@Override
	public boolean canPerform(Assets assets, PublicLocations publicLocations) {
		return assets.getQuantityFor(oldAssetType) > 0;
	}

	@Override
	public TimeRequiredPerProducedAsset calculateTimeRequiredPerProducedAsset(int timeRequired) {
		return new TimeRequiredPerProducedAsset(oldAssetType, maxQuantityTransformed, newAssetType, maxQuantityTransformed, timeRequired);
	}	
}
