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

public class AssetProduce implements Serializable {
	public static final AssetProduce NONE = new AssetProduce();
	
	private final int[] produce;
	private int[] maxProduce;
	
	public AssetProduce(AssetType assetType) {
		produce = new int[AssetType.SIZE];
		produce[assetType.ordinal()] = 1;
		maxProduce = produce.clone();
	}
	
	public AssetProduce(AssetType assetType1, AssetType assetType2) {
		produce = new int[AssetType.SIZE];
		produce[assetType1.ordinal()] = 1;
		produce[assetType2.ordinal()] = 1;
		maxProduce = produce.clone();
	}
	
	public AssetProduce(AssetType assetType1, AssetType assetType2, AssetType assetType3) {
		produce = new int[AssetType.SIZE];
		produce[assetType1.ordinal()] = 1;
		produce[assetType2.ordinal()] = 1;
		produce[assetType3.ordinal()] = 1;
		maxProduce = produce.clone();
	}
	
	public AssetProduce(AssetType... assetTypes) {
		produce = new int[AssetType.SIZE];
		for(AssetType assetType : assetTypes) {
			produce[assetType.ordinal()] = 1;
		}
		maxProduce = produce.clone();
	}
	
	private AssetProduce(int[] originalProduce, int quantity) {
		produce = new int[AssetType.SIZE];
		int length = originalProduce.length;
		
		for(int i=0; i<length; i++) {
			produce[i] = originalProduce[i] * quantity;
		}
		maxProduce = produce.clone();
	}

	public int getProduce(AssetType assetType) {
		return produce[assetType.ordinal()];
	}

	public AssetProduce create(int quantity) {
		return new AssetProduce(produce, quantity);
	}

	public AssetProduce add(AssetProduce other) {
		int length = other.produce.length;
		
		AssetProduce assetProduce = new AssetProduce();
		for(int i=0; i<length; i++) {
			assetProduce.produce[i] = this.produce[i] + other.produce[i];
		}
		assetProduce.maxProduce = assetProduce.produce.clone();
		return assetProduce;
	}

	public void use(AssetType neededAssetType) {
		produce[neededAssetType.ordinal()]--;
	}
	
	public void use(AssetType neededAssetType, int amount) {
		produce[neededAssetType.ordinal()] -= amount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for(int i=0; i<produce.length; i++) {
			int value = produce[i];
			int maxValue = maxProduce[i];
			if (maxValue > 0) {
				if (builder.length() > 0) { builder.append(", "); }
				AssetType assetType = AssetType.VALUES[i];
				String name = assetType.getDescription();
				
				builder.append(name).append(": ").append(value).append(" / ").append(maxValue);
			}
		}
		return builder.toString();
	}
	
	public String toString(AssetType assetType) {
		int index = assetType.ordinal();
		int value = produce[index];
		int maxValue = maxProduce[index];
		return value + " / " + maxValue;
	}
}
