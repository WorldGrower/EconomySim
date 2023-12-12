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

import java.util.ArrayList;
import java.util.List;

public class SimpleAsset extends AbstractAsset {
	private int quantity;
	
	public SimpleAsset(AssetType assetType, int quantity) {
		this(assetType, quantity, assetType.getAssetProduceForQuantity(quantity));
	}
	
	private SimpleAsset(AssetType assetType, int quantity, AssetProduce remainingProduce) {
		super(assetType, remainingProduce);
		this.quantity = quantity;
		if (quantity < 0) {
			throw new IllegalArgumentException("quantity is negative:" + quantity);
		}
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public SimpleAsset add(Asset asset) {
		SimpleAsset other = (SimpleAsset) asset;
		return new SimpleAsset(getAssetType(), quantity + other.quantity, remainingProduce.add(other.remainingProduce));
	}

	@Override
	public void increaseQuantity(int quantityIncrease) {
		quantity += quantityIncrease;
	}

	@Override
	public void decreaseQuantity(int quantityDecrease) {
		if (quantityDecrease > quantity) {
			throw new IllegalArgumentException("quantity " + quantity + " cannot be decreased by " + quantityDecrease + " for " + toString());
		}
		quantity -= quantityDecrease;
	}
	
	@Override
	public List<Asset> divide(int divisor) {
		List<Asset> assetList = new ArrayList<>();
		int dividedQuantity = quantity / divisor;
		if (dividedQuantity == 0) { dividedQuantity = 1; }
		int remainingQuantity = quantity;
		for(int i=0; i<divisor; i++) {
			if (i == divisor - 1) {
				assetList.add(new SimpleAsset(getAssetType(), remainingQuantity));
			} else if (remainingQuantity - dividedQuantity >= 0) {
				assetList.add(new SimpleAsset(getAssetType(), dividedQuantity));
				remainingQuantity -= dividedQuantity;
			} else {
				assetList.add(new SimpleAsset(getAssetType(), 0));
			}
		}
		return assetList;
	}

	@Override
	public void endOfTurn() {
	}

	@Override
	public List<Asset> divideUsingLandAsset(List<Asset> dividedLandAssets, int divisor) {
		return divide(divisor);
	}

	@Override
	public Asset retrieve(int retrievedQuantity) {
		if (retrievedQuantity > quantity) {
			throw new IllegalArgumentException("quantity " + quantity + " cannot be retrieved by " + retrievedQuantity + " for " + toString());
		}
		this.quantity -= retrievedQuantity;
		return new SimpleAsset(getAssetType(), retrievedQuantity);
	}
	
	@Override
	public String toString() {
		return "assetType: " + getAssetType() + ", quantity: " + quantity;
	}
}
