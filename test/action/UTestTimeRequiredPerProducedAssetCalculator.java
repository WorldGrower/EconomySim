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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import asset.AssetType;

public class UTestTimeRequiredPerProducedAssetCalculator {

	@Test
	public void testCalculate() {
		assertEquals(4, calculate(AssetType.FOOD));
		assertEquals(1, calculate(AssetType.WATER));
		
		assertEquals(40, calculate(AssetType.WHEAT));
		assertEquals(42, calculate(AssetType.FLOUR));
		assertEquals(44, calculate(AssetType.BREAD));
		
		assertEquals(400, calculate(AssetType.CLAY_POT));
		assertEquals(400, calculate(AssetType.GLAZED_POT));
	}
	
	private int calculate(AssetType assetType) {
		return TimeRequiredPerProducedAssetCalculator.calculate(assetType);
	}
}
