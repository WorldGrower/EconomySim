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

import asset.AssetEvaluator;
import asset.AssetType;
import asset.Assets;
import asset.LivingAsset;
import asset.Tools;
import environment.PublicLocations;
import society.PublicAssets;

class LivingAssetDestruction extends AbstractAssetDestruction {

	public LivingAssetDestruction(AssetType destroyedAssetType, AssetType createdAssetType, int createdQuantity) {
		super(destroyedAssetType, createdAssetType, createdQuantity);
	}
	
	public LivingAssetDestruction(AssetType destroyedAssetType, AssetType createdAssetType, int createdQuantity, AssetEvaluator assetEvaluator) {
		super(destroyedAssetType, createdAssetType, createdQuantity, assetEvaluator);
	}

	@Override
	public void perform(Assets assets, Tools tools, PublicAssets publicAssets, PublicLocations publicLocations) {
		LivingAsset destroyedAsset = ((LivingAsset) assets.get(getDestroyedAssetType()));
		int index = destroyedAsset.getValidIndex(getAssetEvaluator());
		
		int numberOfAssetsProduced = tools.calculateProduction(getCreatedQuantity());
		createAsset(assets, numberOfAssetsProduced);
		
		destroyedAsset.remove(index);
	}
}
