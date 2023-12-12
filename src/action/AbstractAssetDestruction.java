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

import asset.Asset;
import asset.AssetEvaluator;
import asset.AssetType;
import asset.Assets;
import asset.SimpleAsset;
import asset.Tools;
import environment.PublicLocations;
import society.PublicAssets;

abstract class AbstractAssetDestruction implements AssetAction {
	private final AssetType destroyedAssetType;
	private final AssetType createdAssetType;
	private final int createdQuantity;
	private final AssetEvaluator assetEvaluator;

	public AbstractAssetDestruction(AssetType destroyedAssetType, AssetType createdAssetType, int createdQuantity) {
		this(destroyedAssetType, createdAssetType, createdQuantity, AssetEvaluator.NONE);
	}
	
	public AbstractAssetDestruction(AssetType destroyedAssetType, AssetType createdAssetType, int createdQuantity, AssetEvaluator assetEvaluator) {
		super();
		this.destroyedAssetType = destroyedAssetType;
		this.createdAssetType = createdAssetType;
		this.createdQuantity = createdQuantity;
		this.assetEvaluator = assetEvaluator;
	}

	@Override
	public final AssetType getAssetType() {
		return destroyedAssetType;
	}

	@Override
	public final AssetType getProvidedAssetType() {
		return createdAssetType;
	}

	protected final void createAsset(Assets assets, int numberOfAssetsProduced) {
		Asset createdAsset = new SimpleAsset(createdAssetType, numberOfAssetsProduced);
		assets.addAsset(createdAsset);
	}
	
	@Override
	public abstract void perform(Assets assets, Tools tools, PublicAssets publicAssets, PublicLocations publicLocations);

	@Override
	public final boolean canPerform(Assets assets, PublicLocations publicLocations) {
		return assets.getQuantityFor(destroyedAssetType) > 0 && ((AttributableAsset)assets.get(destroyedAssetType)).getValidIndex(assetEvaluator) != -1;
	}

	@Override
	public final TimeRequiredPerProducedAsset calculateTimeRequiredPerProducedAsset(int timeRequired) {
		return new TimeRequiredPerProducedAsset(destroyedAssetType, 1, createdAssetType, createdQuantity, timeRequired);
	}

	protected final AssetType getDestroyedAssetType() {
		return destroyedAssetType;
	}

	protected final AssetType getCreatedAssetType() {
		return createdAssetType;
	}

	protected final int getCreatedQuantity() {
		return createdQuantity;
	}

	protected final AssetEvaluator getAssetEvaluator() {
		return assetEvaluator;
	}
	
	
}
