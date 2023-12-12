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
package environment;

import java.io.Serializable;

import asset.AssetType;

public class Payment implements Serializable {
	public static final Payment NONE = new Payment(null, 0);
	
	private final AssetType assetType;
	private final int quantity;
	
	public Payment(AssetType assetType, int quantity) {
		super();
		this.assetType = assetType;
		this.quantity = quantity;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public int getQuantity() {
		return quantity;
	}
}
