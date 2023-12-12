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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import asset.AssetType;

public class AssetActivityValidator {

	public static void validate() {
		for(Activity activity : Activity.VALUES) {
			AssetType assetType = activity.getAssetType();
			AssetType providedAssetType = activity.getProvidedAssetType();
			
			if (assetType.getAssetProduceForQuantity(1).getProduce(providedAssetType) == 0) {
				throw new IllegalStateException("AssetType " + assetType + " doesn't produce provided AssetType " + providedAssetType + " which is required for activity " + activity.getName());
			}
		}
		
		if (AssetType.VALUES.length != AssetType.SIZE) {
			throw new IllegalStateException("AssetType.SIZE should be equal to AssetType.VALUES.length: " + AssetType.SIZE + " != " + AssetType.values().length);
		}
		
		List<String> assetTypeDescriptions = Arrays.asList(AssetType.VALUES).stream().map(a -> a.getDescription()).collect(Collectors.toList());
		for(String assetTypeDescription : assetTypeDescriptions) {
			if (Collections.frequency(assetTypeDescriptions, assetTypeDescription) > 1) {
				throw new IllegalStateException("Asset type has duplicate description " + assetTypeDescription);
			}
		}
	}
}
