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

import java.util.List;

class LocatableAssetDivision implements AssetDivision {

	private final List<Asset> dividedLandAssets;
	
	public LocatableAssetDivision(Assets assets, int divisor) {
		Asset landAsset = assets.get(AssetType.LAND);
		if (landAsset != null) {
			dividedLandAssets = landAsset.divide(divisor);
		} else {
			dividedLandAssets = null;
		}
	}

	@Override
	public List<Asset> getDividedAssets(Asset asset, int divisor) {
		if (asset.getAssetType() == AssetType.LAND) {
			return dividedLandAssets;
		} else {
			return asset.divideUsingLandAsset(dividedLandAssets, divisor);
		}
	}
}
