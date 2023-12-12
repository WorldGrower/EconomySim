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

import java.util.ArrayList;
import java.util.List;

import asset.AssetType;

class TimeRequiredPerProducedAssetCalculator {

	public static int calculate(AssetType assetType) {
		return calculate(assetType, new ArrayList<>());
	}
	
	private static int calculate(AssetType assetType, List<Activity> calculatedActivities) {
		int totalTimeRequired = Integer.MAX_VALUE;
		for (Activity activity : Activity.VALUES) {
			if (activity.getProvidedAssetType() == assetType && !calculatedActivities.contains(activity)) {//TODO: check required knowledge
				calculatedActivities.add(activity);
				
				TimeRequiredPerProducedAsset timeRequiredPerProducedAsset = activity.calculateTimeRequiredPerProducedAsset();
				final int consumedTimeRequired;
				if (timeRequiredPerProducedAsset.getConsumedAssetQuantity() > 0) {
					consumedTimeRequired = calculate(timeRequiredPerProducedAsset.getConsumedAssetType(), calculatedActivities);
				} else {
					consumedTimeRequired = 0;
				}
				totalTimeRequired = Math.min(totalTimeRequired, timeRequiredPerProducedAsset.calculate(consumedTimeRequired));
			}
		}
		return totalTimeRequired;
	}
}
