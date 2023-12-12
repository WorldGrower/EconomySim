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
import java.util.Arrays;
import java.util.List;

public class NeededAssetTypeCache {

	// NeededAssetType --> AssetType[]
	private static AssetType[][] CACHE;
	
	static {
		AssetTypes[] cache = new AssetTypes[AssetType.VALUES.length];
		for(AssetType neededAssetType : AssetType.VALUES) {
			cache[neededAssetType.ordinal()] = new AssetTypes();
		}
		for(AssetType neededAssetType : AssetType.VALUES) {
			for(AssetType assetType : AssetType.VALUES) {
				if (ActivityCache.findActivity(assetType, neededAssetType) != null) {
					cache[neededAssetType.ordinal()].add(assetType);
				}
			}
		}
		
		CACHE = new AssetType[AssetType.VALUES.length][];
		for(AssetType neededAssetType : AssetType.VALUES) {
			CACHE[neededAssetType.ordinal()] = cache[neededAssetType.ordinal()].toArray();
		}
	}
	
	public static AssetType[] getProducingAssets(AssetType neededAssetType) {
		return CACHE[neededAssetType.ordinal()];
	}
	
	public static void main(String[] args) {
		for(AssetType neededAssetType : AssetType.VALUES) {
			System.out.println(neededAssetType + ": " + Arrays.toString(CACHE[neededAssetType.ordinal()]));
		}
	}
	
	private static class AssetTypes {
		private final List<AssetType> assetTypes = new ArrayList<>();

		public void add(AssetType assetType) {
			assetTypes.add(assetType);
		}

		public AssetType[] toArray() {
			return assetTypes.toArray(new AssetType[assetTypes.size()]);
		}
	}
}
