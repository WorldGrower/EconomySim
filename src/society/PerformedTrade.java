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

import java.io.Serializable;

import asset.AssetType;

public class PerformedTrade implements Serializable {
	private final String personName;
	private final AssetType assetType;
	private final int quantity;
	private final String targetPersonName;
	private final AssetType targetAssetType;
	private final int targetQuantity;
	
	public PerformedTrade(String personName, AssetType assetType, int quantity, String targetPersonName, AssetType targetAssetType,
			int targetQuantity) {
		super();
		this.personName = personName;
		this.assetType = assetType;
		this.quantity = quantity;
		this.targetPersonName = targetPersonName;
		this.targetAssetType = targetAssetType;
		this.targetQuantity = targetQuantity;
	}

	public String getPersonName() {
		return personName;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getTargetPersonName() {
		return targetPersonName;
	}

	public AssetType getTargetAssetType() {
		return targetAssetType;
	}

	public int getTargetQuantity() {
		return targetQuantity;
	}
	

}
