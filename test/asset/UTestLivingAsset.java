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

import java.util.List;

import org.junit.Test;

public class UTestLivingAsset {

	@Test
	public void testAdd() {
		Asset asset1 = new LivingAsset(AssetType.DOG);
		Asset asset2 = new LivingAsset(AssetType.DOG);
		assertEquals(1, asset1.getQuantity());
		assertEquals(1, asset2.getQuantity());
		
		LivingAsset asset = (LivingAsset) asset1.add(asset2);
		assertEquals(2, asset.getQuantity());
	}
	
	@Test
	public void testDecreaseQuantity() {
		Asset asset = new LivingAsset(AssetType.DOG);
		asset.decreaseQuantity(1);
		
		assertEquals(0, asset.getQuantity());
	}
	
	@Test
	public void testDivide() {
		Asset asset = (LivingAsset) new LivingAsset(AssetType.DOG).add(new LivingAsset(AssetType.DOG));
		asset.add(new LivingAsset(AssetType.DOG));
		List<Asset> dividedAssets = asset.divide(2);
		
		assertEquals(2, asset.getQuantity());
		assertEquals(2, dividedAssets.size());
		assertEquals(1, dividedAssets.get(0).getQuantity());
		assertEquals(1, dividedAssets.get(1).getQuantity());
	}
	
	@Test
	public void testEndOfTurn() {
		LivingAsset asset = new LivingAsset(AssetType.DOG);

		assertEquals(0, asset.getAge(0));
		
		asset.endOfTurn();
		assertEquals(1, asset.getAge(0));
	}

	@Test
	public void testEndOfTurnDeath() {
		LivingAsset asset = new LivingAsset(AssetType.DOG);

		assertEquals(1, asset.getQuantity());
		
		for (int i=0; i<1001; i++) { asset.endOfTurn(); }
		assertEquals(0, asset.getQuantity());
	}
	
	@Test
	public void testRetrieve() {
		Asset asset = (LivingAsset) new LivingAsset(AssetType.DOG).add(new LivingAsset(AssetType.DOG));
		LivingAsset retrievedAsset = (LivingAsset) asset.retrieve(1);
		
		assertEquals(1, retrievedAsset.getQuantity());
		
		assertEquals(1, asset.getQuantity());
	}
}
