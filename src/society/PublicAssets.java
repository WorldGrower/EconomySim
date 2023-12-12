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
import asset.AssetContainer;
import asset.AssetType;
import asset.Assets;

public interface PublicAssets extends AssetContainer {
	public Asset findAsset(AssetType neededAssetType);
	public Collection<AssetType> getAssetTypes();
	public Asset get(AssetType assetType);
	public Assets retrieveAssetsForNewPerson();
	public int getQuantityFor(AssetType assetType);
	public List<AssetType> getSortedAssetKeys();
}
