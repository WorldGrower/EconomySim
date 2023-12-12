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

import asset.AssetType;

public class MockPublicLocations implements PublicLocations {

	@Override
	public boolean isFreeLocation(Location location, AssetType assetType) {
		return false;
	}

	@Override
	public void useLocation(Location location, AssetType assetType) {

	}

	@Override
	public void freeLocation(Location location, AssetType assetType) {
	}
}
