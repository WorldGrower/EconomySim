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
package society;

import java.util.Collection;
import java.util.List;

import asset.Asset;
import asset.AssetType;
import asset.Assets;

public class MockPublicAssets implements PublicAssets {

	private Assets assets = new Assets();
	
	public MockPublicAssets() {
	}
	
	public MockPublicAssets(Asset asset) {
		addAsset(asset);
	}	
	
	public void addAsset(Asset asset) {
		assets.addAsset(asset);
	}
	
	@Override
	public Asset findAsset(AssetType neededAssetType) {
		return assets.getProducingAsset(neededAssetType);
	}

	@Override
	public Collection<AssetType> getAssetTypes() {
		return assets.getSortedAssetKeys();
	}

	@Override
	public Asset get(AssetType assetType) {
		return assets.get(assetType);
	}

	@Override
	public int getQuantityFor(AssetType assetType) {
		return assets.getQuantityFor(assetType);
	}

	@Override
	public List<AssetType> getSortedAssetKeys() {
		return null;
	}

	@Override
	public Assets retrieveAssetsForNewPerson() {
		return assets.retrieveLocationAssetsForOneLocation();
	}	
	
	public int size() {
		return assets.getSortedAssetKeys().size();
	}

	@Override
	public void addAssets(Assets assetsRemaining) {
		assets.addAssets(assetsRemaining);
	}

	@Override
	public Asset retrieve(AssetType assetType, int quantity) {
		return assets.retrieve(assetType, quantity);
	}
}
