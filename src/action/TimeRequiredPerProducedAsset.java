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

class TimeRequiredPerProducedAsset {
	private final AssetType consumedAssetType;
	private final int consumedAssetQuantity;
	
	private final AssetType producedAssetType;
	private final int producedQuantity;
	
	private final int timeRequired;

	public TimeRequiredPerProducedAsset(AssetType consumedAssetType, int consumedAssetQuantity, AssetType producedAssetType, int producedQuantity,
			int timeRequired) {
		super();
		this.consumedAssetType = consumedAssetType;
		this.consumedAssetQuantity = consumedAssetQuantity;
		this.producedAssetType = producedAssetType;
		this.producedQuantity = producedQuantity;
		this.timeRequired = timeRequired;
	}

	/*
	//AssetCreation:
	public int calculateTimeRequiredFor100Assets(int timeRequired) {
		return (timeRequiredForOldAssetType * 100) / consumedQuantity + (timeRequired * 100);
	}
	
	//AssetDestruction
	public int calculateTimeRequiredFor100Assets(int timeRequired) {
		return timeRequiredForOldAssetType + (timeRequired * 100) / createdQuantity;
	}
	
	//AssetExtraction
	public int calculateTimeRequiredFor100Assets(int timeRequired) {
		return (timeRequired * 100) / quantityProvided;
	}
	
	//AssetTransformation
	public int calculateTimeRequiredFor100Assets(int timeRequired) {
		return timeRequiredForOldAssetType + (timeRequired * 100) / maxQuantityTransformed;
	}
	*/

	public int calculate(int timeRequiredPerConsumedAsset) {
		int consumedAssetQuantityDivider = consumedAssetQuantity > 0 ? consumedAssetQuantity : 1; 
		return (timeRequiredPerConsumedAsset * 100) / consumedAssetQuantityDivider + (timeRequired * 100) / producedQuantity;
	}

	public AssetType getConsumedAssetType() {
		return consumedAssetType;
	}

	public int getConsumedAssetQuantity() {
		return consumedAssetQuantity;
	}
}
