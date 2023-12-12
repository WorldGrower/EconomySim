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

import action.Activity;

class ActivityCache {
	private static Activity[] ACTIVITY_CACHE;
	
	static {
		ACTIVITY_CACHE = new Activity[AssetType.SIZE + AssetType.SIZE * AssetType.SIZE];
		for(Activity activity : Activity.VALUES) {
			int index = calculateActivityIndex(activity.getAssetType(), activity.getProvidedAssetType());
			ACTIVITY_CACHE[index] = activity;
		}
	}
	
	public static Activity findActivity(AssetType assetType, AssetType neededAssetType) {
		return ACTIVITY_CACHE[calculateActivityIndex(assetType, neededAssetType)];
	}

	private static int calculateActivityIndex(AssetType assetType, AssetType providedAssetType) {
		return assetType.ordinal() + providedAssetType.ordinal() * AssetType.SIZE;
	}
}
