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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import action.Activity;

public class UTestActivityCache {

	@Test
	public void testFindActivity() {
		assertEquals(Activity.CREATE_DOG, ActivityCache.findActivity(AssetType.LAND, AssetType.DOG));
		assertEquals(null, ActivityCache.findActivity(AssetType.WATER, AssetType.DOG));
		assertEquals(Activity.SLAUGHTER_CATTLE, ActivityCache.findActivity(AssetType.CATTLE, AssetType.FOOD));
		assertEquals(Activity.HARVEST_WHEAT, ActivityCache.findActivity(AssetType.WHEAT_FIELD, AssetType.WHEAT));
	}
}
