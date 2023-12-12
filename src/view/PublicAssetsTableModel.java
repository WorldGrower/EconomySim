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
package view;

import java.util.List;

import asset.Asset;
import asset.AssetType;
import society.PublicAssets;

public class PublicAssetsTableModel extends AbstractAssetsTableModel {

	private PublicAssets publicAssets;
	
	public PublicAssetsTableModel(PublicAssets publicAssets) {
		super();
		this.publicAssets = publicAssets;
	}

	public void updatePublicAssets(PublicAssets publicAssets) {
		this.publicAssets = publicAssets;
		this.fireTableDataChanged();
	}
	
	@Override
	public List<AssetType> getAssetTypes() {
		return publicAssets.getSortedAssetKeys();
	}
	
	@Override
	public Asset getAsset(AssetType assetType) {
		return publicAssets.get(assetType);
	}
}
