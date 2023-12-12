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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import asset.AssetType;

public class UTestPublicLocationsImpl {

	@Test
	public void testUseFreeLocation() {
		PublicLocations publicLocations = new PublicLocationsImpl();
		Location location = new Location(0);
		
		assertEquals(true, publicLocations.isFreeLocation(location, AssetType.WHEAT_FIELD));
		assertEquals(true, publicLocations.isFreeLocation(location, AssetType.IRRIGATION_CANAL));
		
		publicLocations.useLocation(location, AssetType.WHEAT_FIELD);
		assertEquals(false, publicLocations.isFreeLocation(location, AssetType.WHEAT_FIELD));
		assertEquals(true, publicLocations.isFreeLocation(location, AssetType.IRRIGATION_CANAL));
		
		publicLocations.freeLocation(location, AssetType.WHEAT_FIELD);
		assertEquals(true, publicLocations.isFreeLocation(location, AssetType.WHEAT_FIELD));
		assertEquals(true, publicLocations.isFreeLocation(location, AssetType.IRRIGATION_CANAL));
	}
}
