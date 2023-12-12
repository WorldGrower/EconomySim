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
import asset.AssetType;
import asset.Assets;
import asset.LocatableAsset;
import asset.Tools;
import environment.Location;
import environment.PublicLocations;
import society.PublicAssets;

class AssetCreation implements AssetAction {
	private final AssetType createdAssetType;
	private final AssetType parentAssetType;
	private final AssetType consumedAssetType;
	private final int consumedQuantity;

	public AssetCreation(AssetType createdAssetType, AssetType parentAssetType) {
		super();
		this.createdAssetType = createdAssetType;
		this.parentAssetType = parentAssetType;
		this.consumedAssetType = null;
		this.consumedQuantity = 0;
	}
	
	public AssetCreation(AssetType createdAssetType, AssetType parentAssetType, AssetType consumedAssetType, int consumedQuantity) {
		super();
		this.createdAssetType = createdAssetType;
		this.parentAssetType = parentAssetType;
		this.consumedAssetType = consumedAssetType;
		this.consumedQuantity = consumedQuantity;
	}

	@Override
	public AssetType getAssetType() {
		return parentAssetType;
	}

	@Override
	public AssetType getProvidedAssetType() {
		return createdAssetType;
	}

	@Override
	public void perform(Assets assets, Tools tools, PublicAssets publicAssets, PublicLocations publicLocations) {
		LocatableAsset parentAsset = (LocatableAsset) assets.get(parentAssetType);
		Location freeLocation = findFreeLocation(createdAssetType, parentAsset, publicLocations);
		Asset createdAsset = new LocatableAsset(createdAssetType, freeLocation);
		publicLocations.useLocation(freeLocation, createdAssetType);
		assets.addAsset(createdAsset);
		if (consumedAssetType != null) {
			assets.removeAsset(consumedAssetType, consumedQuantity);
		}
	}

	@Override
	public boolean canPerform(Assets assets, PublicLocations publicLocations) {
		LocatableAsset parentAsset = (LocatableAsset) assets.get(parentAssetType);
		final boolean hasConsumedAssetType;
		if (consumedAssetType != null) {
			hasConsumedAssetType = assets.getQuantityFor(consumedAssetType) >= consumedQuantity;
		} else {
			hasConsumedAssetType = true;
		}
		return hasConsumedAssetType && parentAsset != null && findFreeLocation(createdAssetType, parentAsset, publicLocations) != null;
	}
	
	private static Location findFreeLocation(AssetType createdAssetType, LocatableAsset parentAsset, PublicLocations publicLocations) {
		for(int i=0; i<parentAsset.size(); i++) {
			Location location = parentAsset.getLocation(i);
			if (publicLocations.isFreeLocation(location, createdAssetType)) {
				return location;
			}
		}
		return null;
	}

	@Override
	public TimeRequiredPerProducedAsset calculateTimeRequiredPerProducedAsset(int timeRequired) {
		return new TimeRequiredPerProducedAsset(consumedAssetType, consumedQuantity, createdAssetType, 1, timeRequired);
	}	
}
