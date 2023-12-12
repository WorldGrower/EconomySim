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

import action.Activity;
import asset.AssetType;
import asset.OperationalAsset;
import asset.SimpleAsset;
import environment.PublicLocationsImpl;
import knowledge.Knowledge;
import society.MockPersonFinder;
import society.MockPublicAssets;
import society.MockPublicKnowledge;
import society.MockPublicOrganizations;
import society.PublicAssets;

public class UTestAssetExtractionBehaviour {

	@Test
	public void testExtractAsset() {
		Person person = new Person(18, Sex.FEMALE);
		person.getAssets().addAsset(new SimpleAsset(AssetType.WHEAT, 100));
		
		AssetExtractionBehaviour assetExtractionBehaviour = new AssetExtractionBehaviour(new MockPersonFinder(), new MockPublicOrganizations(), new MockPublicAssets(), new MockPublicKnowledge(), new PublicLocationsImpl());
		assetExtractionBehaviour.extractAsset(person, AssetType.FLOUR, Activity.CREATE_FLOUR, 1);
		
		assertEquals(0, person.getAssets().getQuantityFor(AssetType.WHEAT));
		assertEquals(100, person.getAssets().getQuantityFor(AssetType.FLOUR));
	}
	
	@Test
	public void testExtractPublicAsset() {
		Person person = new Person(18, Sex.FEMALE);
		PersonKnowledgeUtils.learnKnowledge(person, Knowledge.IRRIGATION);
		PublicAssets publicAssets = new MockPublicAssets();
		publicAssets.addAsset(new OperationalAsset(AssetType.RIVER));
		
		AssetExtractionBehaviour assetExtractionBehaviour = new AssetExtractionBehaviour(new MockPersonFinder(), new MockPublicOrganizations(), publicAssets, new MockPublicKnowledge(), new PublicLocationsImpl());
		assetExtractionBehaviour.extractAsset(person, AssetType.IRRIGATION_CANAL, Activity.CREATE_IRRIGATION_CANAL, 1);
		
		assertEquals(0, person.getAssets().getQuantityFor(AssetType.IRRIGATION_CANAL));
		assertEquals(0, person.getAssets().getQuantityFor(AssetType.RIVER));
		
		assertEquals(1, publicAssets.getQuantityFor(AssetType.IRRIGATION_CANAL));
		assertEquals(1, publicAssets.getQuantityFor(AssetType.RIVER));
	}
}
