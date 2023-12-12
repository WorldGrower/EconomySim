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

import java.util.List;

public interface Asset {

	int getQuantity();

	AssetType getAssetType();

	Asset add(Asset asset);

	boolean work(AssetType neededAssetType);

	void regenerate();

	void increaseQuantity(int quantityIncrease);
	void decreaseQuantity(int quantityDecrease);

	String getRemainingProduceDescription();
	String getRemainingProduceDescription(AssetType assetType);

	boolean hasRemainingProduce(AssetType neededAssetType);
	
	List<Asset> divide(int divisor);

	int useRemainingProduce(AssetType water, int amount);

	List<Asset> divideUsingLandAsset(List<Asset> dividedLandAssets, int divisor);

	Asset retrieve(int retrievedQuantity);
	
	public static int getQuantityFor(List<Asset> assetsList) {
		int quantity = 0;
		for(Asset asset : assetsList) {
			quantity += asset.getQuantity();
		}
		return quantity;
	}

}