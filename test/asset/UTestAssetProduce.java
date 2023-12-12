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

public class UTestAssetProduce {

	@Test
	public void testGetUse() {
		AssetProduce assetProduce = new AssetProduce(AssetType.LAND);
		assertEquals(1, assetProduce.getProduce(AssetType.LAND));
		assertEquals(0, assetProduce.getProduce(AssetType.RIVER));
		
		assetProduce.use(AssetType.LAND);
		assertEquals(0, assetProduce.getProduce(AssetType.LAND));
	}
	
	@Test
	public void testQuantity() {
		AssetProduce assetProduce = AssetType.LAND.getAssetProduceForQuantity(10);
		assertEquals(10, assetProduce.getProduce(AssetType.FOOD));
		assertEquals(10, assetProduce.getProduce(AssetType.STONE_SICKLE));
		
		assetProduce.use(AssetType.FOOD);
		assertEquals(9, assetProduce.getProduce(AssetType.FOOD));
		assertEquals(10, assetProduce.getProduce(AssetType.STONE_SICKLE));
	}
	
	@Test
	public void testToString() {
		AssetProduce assetProduce = AssetType.RIVER.getAssetProduceForQuantity(10);
		assertEquals("water: 10 / 10, irrigation canal: 10 / 10", assetProduce.toString());
	}
	
	@Test
	public void testToStringAssetType() {
		AssetProduce assetProduce = AssetType.RIVER.getAssetProduceForQuantity(10);
		assertEquals("10 / 10", assetProduce.toString(AssetType.WATER));
	}
	
	@Test
	public void testAdd() {
		AssetProduce assetProduce1 = AssetType.LAND.getAssetProduceForQuantity(10);
		assetProduce1.use(AssetType.FOOD);
		AssetProduce assetProduce2 = AssetType.LAND.getAssetProduceForQuantity(10);
		
		AssetProduce assetProduce = assetProduce1.add(assetProduce2);
		assertEquals(19, assetProduce.getProduce(AssetType.FOOD));
	}
}
