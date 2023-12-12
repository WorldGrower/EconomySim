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
import java.util.HashMap;
import java.util.Map;

import asset.AssetType;

public class PublicLocationsImpl implements PublicLocations, Serializable {

	private final Map<Location, boolean[]> isUsedMap = new HashMap<>();
	
	@Override
	public boolean isFreeLocation(Location location, AssetType assetType) {
		boolean[] isUsed = isUsedMap.get(location);
		return isUsed == null || !isUsed[assetType.ordinal()];
	}

	@Override
	public void useLocation(Location location, AssetType assetType) {
		boolean[] isUsed = isUsedMap.get(location);
		if (isUsed == null) {
			isUsed = new boolean[AssetType.SIZE];
			isUsedMap.put(location, isUsed);
		}
		isUsed[assetType.ordinal()] = true;
	}

	@Override
	public void freeLocation(Location location, AssetType assetType) {
		boolean[] isUsed = isUsedMap.get(location);
		if (isUsed != null) {
			isUsed[assetType.ordinal()] = false;
		}
	}
}
