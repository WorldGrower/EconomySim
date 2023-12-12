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

public class MockAssetContainer implements AssetContainer {

	private Assets assets = new Assets();

	public int getCash() {
		return assets.get(AssetType.CASH) != null ? assets.get(AssetType.CASH).getQuantity() : 0;
	}
	
	public int getLand() {
		return assets.get(AssetType.LAND) != null ? assets.get(AssetType.LAND).getQuantity() : 0;
	}

	@Override
	public void addAssets(Assets assetsRemaining) {
		assets.addAssets(assetsRemaining);
	}

	@Override
	public void addAsset(Asset asset) {
		assets.addAsset(asset);
	}

	@Override
	public int getQuantityFor(AssetType assetType) {
		return assets.getQuantityFor(assetType);
	}

	@Override
	public Asset retrieve(AssetType assetType, int quantity) {
		return assets.retrieve(assetType, quantity);
	}
}
