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

import person.StorageCalculator;
import person.StorageCalculatorOwnNeeds;

public class UTestStorageCalculatorOwnNeeds {

	@Test
	public void testConsume() {
		AssetStorage assetStorage = new AssetStorage();
		Assets assets = new Assets();
		assertEquals(0, assetStorage.getRemainingStorageQuantity(assets, AssetTrait.LIQUID_STORAGE));
		
		assetStorage = new AssetStorage();
		assets.addAsset(new SimpleAsset(AssetType.GLAZED_POT, 1));
		assertEquals(100, assetStorage.getRemainingStorageQuantity(assets, AssetTrait.LIQUID_STORAGE));
		
		assetStorage.consumeStorage(50, AssetTrait.LIQUID_STORAGE);
		assertEquals(50, assetStorage.getRemainingStorageQuantity(assets, AssetTrait.LIQUID_STORAGE));
	}
	
	@Test
	public void testCalculateRequiredStorage() {
		StorageCalculator instance = StorageCalculatorOwnNeeds.INSTANCE;
		Assets assets = new Assets();
		assertEquals(0, instance.calculateRequiredStorage(assets, AssetTrait.PERISHABLE));
		
		assets.addAsset(new SimpleAsset(AssetType.WHEAT, 350));
		assertEquals(4, instance.calculateRequiredStorage(assets, AssetTrait.PERISHABLE));
		
		assets.addAsset(new SimpleAsset(AssetType.FLOUR, 60));
		assertEquals(5, instance.calculateRequiredStorage(assets, AssetTrait.PERISHABLE));
	}
}
