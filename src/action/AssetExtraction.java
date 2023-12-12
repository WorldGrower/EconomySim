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

class AssetExtraction implements AssetAction {
	private final AssetType assetType;
	private final AssetType providedAssetType;
	private final int quantityProvided;
	
	public AssetExtraction(AssetType assetType, AssetType providedAssetType, int quantityProvided) {
		super();
		this.assetType = assetType;
		this.providedAssetType = providedAssetType;
		this.quantityProvided = quantityProvided;
	}

	@Override
	public AssetType getAssetType() {
		return assetType;
	}

	@Override
	public AssetType getProvidedAssetType() {
		return providedAssetType;
	}

	@Override
	public void perform(Assets assets, Tools tools, PublicAssets publicAssets, PublicLocations publicLocations) {
		int numberOfAssetsProduced = tools.calculateProduction(quantityProvided);
		assets.addAsset(new SimpleAsset(providedAssetType, numberOfAssetsProduced));
	}

	@Override
	public boolean canPerform(Assets assets, PublicLocations publicLocations) {
		return true;
	}
	
	@Override
	public TimeRequiredPerProducedAsset calculateTimeRequiredPerProducedAsset(int timeRequired) {
		return new TimeRequiredPerProducedAsset(null, 0, providedAssetType, quantityProvided, timeRequired);
	}
}
