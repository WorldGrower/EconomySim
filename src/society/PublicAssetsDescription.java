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

import asset.Asset;
import asset.AssetType;
import asset.OperationalAsset;

public class PublicAssetsDescription {

	private final PublicAssets publicAssets;
	
	public PublicAssetsDescription(PublicAssets publicAssets) {
		this.publicAssets = publicAssets;
	}
	
	public String getRiverRemainingProduceDescription() {
		Asset riverAsset = publicAssets.get(AssetType.RIVER);
		return riverAsset != null ? riverAsset.getRemainingProduceDescription(AssetType.WATER) : "";
	}

	public String getIrrigationCanalOperational() {
		OperationalAsset irrigationCanalAsset = (OperationalAsset) publicAssets.get(AssetType.IRRIGATION_CANAL);
		return irrigationCanalAsset != null ? irrigationCanalAsset.getOperationalAssetAttribute(0).toString() + " ( operational: " + irrigationCanalAsset.getOperationalAssetAttribute(0).isOperational() + " )" : "";
	}
}
