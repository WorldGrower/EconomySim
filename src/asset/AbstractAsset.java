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

import java.io.Serializable;

abstract class AbstractAsset implements Asset, Serializable {
	private final AssetType assetType;
	protected AssetProduce remainingProduce;
	
	public AbstractAsset(AssetType assetType, AssetProduce remainingProduce) {
		super();
		this.assetType = assetType;
		this.remainingProduce = remainingProduce;
	}

	@Override
	public final AssetType getAssetType() {
		return assetType;
	}

	@Override
	public final boolean work(AssetType neededAssetType) {
		if (remainingProduce.getProduce(neededAssetType) > 0) {
			remainingProduce.use(neededAssetType);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public final void regenerate() {
		endOfTurn(); // TODO rename regerate
		if (assetType.hasTrait(AssetTrait.REGENERATIVE)) {
			remainingProduce = assetType.getAssetProduceForQuantity(getQuantity());
		}
	}
	
	public abstract void endOfTurn();

	@Override
	public final String getRemainingProduceDescription() {
		return remainingProduce.toString();
	}
	
	@Override
	public final String getRemainingProduceDescription(AssetType assetType) {
		return remainingProduce.toString(assetType);
	}

	@Override
	public final boolean hasRemainingProduce(AssetType neededAssetType) {
		return remainingProduce.getProduce(neededAssetType) > 0;
	}
	
	@Override
	public final int useRemainingProduce(AssetType neededAssetType, int amount) {
		int quantityUsed = Math.min(remainingProduce.getProduce(neededAssetType), amount);
		remainingProduce.use(neededAssetType, quantityUsed);
		return quantityUsed;
	}
}
