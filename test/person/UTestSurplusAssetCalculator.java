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
package person;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import asset.AssetType;
import asset.SimpleAsset;
import person.SurplusAssetCalculator.SurplusAsset;

public class UTestSurplusAssetCalculator {

	@Test
	public void testCalculateSurplusAsset() {
		Person person = new Person(18, Sex.MALE);
		person.getAssets().addAsset(new SimpleAsset(AssetType.COTTON_CLOTHES, 100));
		SurplusAsset surplusAsset = SurplusAssetCalculator.calculateSurplusAsset(person);
		
		assertEquals(AssetType.COTTON_CLOTHES, surplusAsset.getSurplusAssetType());
		assertEquals(AssetType.GLAZED_POT, surplusAsset.getNeededAssetType());
	}
	
	@Test
	public void testCalculateSurplusAssetNoAssets() {
		Person person = new Person(18, Sex.MALE);
		SurplusAsset surplusAsset = SurplusAssetCalculator.calculateSurplusAsset(person);
		
		assertEquals(null, surplusAsset.getSurplusAssetType());
		assertEquals(AssetType.COTTON_CLOTHES, surplusAsset.getNeededAssetType());
	}
	
	@Test
	public void testCalculateSurplusAssetNoNeeds() {
		Person person = new Person(18, Sex.MALE);
		person.getAssets().addAsset(new SimpleAsset(AssetType.COTTON_CLOTHES, 100));
		person.getAssets().addAsset(new SimpleAsset(AssetType.GLAZED_POT, 100));
		person.getAssets().addAsset(new SimpleAsset(AssetType.BREAD, 100));
		person.getAssets().addAsset(new SimpleAsset(AssetType.CATTLE, 100));
		SurplusAsset surplusAsset = SurplusAssetCalculator.calculateSurplusAsset(person);
		
		assertEquals(AssetType.COTTON_CLOTHES, surplusAsset.getSurplusAssetType());
		assertEquals(null, surplusAsset.getNeededAssetType());
	}
}
