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

import java.io.Serializable;

import action.Activity;
import asset.AssetType;
import environment.PublicLocations;
import knowledge.Knowledge;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class DomesticationBehaviour implements Serializable {
	
	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	private final AssetExtractionBehaviour assetExtractionBehaviour;
	
	public DomesticationBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
		this.assetExtractionBehaviour = new AssetExtractionBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
	}
	
	public void processActionsForDog(Person person, PersonDecisions personDecisions) {
		if (person.getFamilyAssetQuantityFor(AssetType.LAND) > 0) {
			person.learnKnowledgeIfNeeded(Knowledge.DOMESTICATION_OF_DOG, personFinder, publicAssets, publicKnowledge, publicOrganizations);
			assetExtractionBehaviour.extractAsset(person, AssetType.DOG, Activity.CREATE_DOG, 1);
			//TODO: at some point food gathering is no longer used if farming becomes dominant?
		}
	}
	
	public void processActionsForCattle(Person person, PersonDecisions personDecisions) {
		if (person.getFamilyAssetQuantityFor(AssetType.LAND) > 0) {
			person.learnKnowledgeIfNeeded(Knowledge.DOMESTICATION_OF_DOG, personFinder, publicAssets, publicKnowledge, publicOrganizations);
			person.learnKnowledgeIfNeeded(Knowledge.DOMESTICATION_OF_CATTLE, personFinder, publicAssets, publicKnowledge, publicOrganizations);
			assetExtractionBehaviour.extractAsset(person, AssetType.CATTLE, Activity.CREATE_CATTLE, 4);
			if (person.getAssets().getQuantityFor(AssetType.CATTLE) >= 2) {
				assetExtractionBehaviour.extractAsset(person, AssetType.FOOD, Activity.SLAUGHTER_CATTLE, 200);
			}
		}
	}

}
