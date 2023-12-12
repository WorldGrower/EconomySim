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
import asset.AssetTrait;
import asset.AssetType;
import environment.PublicLocations;
import knowledge.Knowledge;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class CreateStorageBehaviour implements PersonBehaviour, Serializable {
	
	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	private final AssetExtractionBehaviour assetExtractionBehaviour;
	private final StorageCalculator storageCalculator;
	
	public CreateStorageBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations, StorageCalculator storageCalculator) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
		this.assetExtractionBehaviour = new AssetExtractionBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.storageCalculator = storageCalculator;
	}
	
	@Override
	public void processActions(Person person, PersonDecisions personDecisions, ProfessionStatistics professionStatistics) {
		person.learnKnowledgeIfNeeded(Knowledge.POTTERY, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		extractAsset(person, AssetType.CLAY_POT, Activity.CREATE_CLAY_POT, storageCalculator.calculateRequiredStorage(person.getAssets(), AssetTrait.PERISHABLE));
		
		person.learnKnowledgeIfNeeded(Knowledge.KILN, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		person.learnKnowledgeIfNeeded(Knowledge.LEAD_SMELTING, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		extractAsset(person, AssetType.KILN, Activity.CREATE_KILN, 1);
		extractAsset(person, AssetType.GLAZED_POT, Activity.CREATE_GLAZED_POT, storageCalculator.calculateRequiredStorage(person.getAssets(), AssetTrait.LIQUID));

	}
	
	private void extractAsset(Person person, AssetType neededAssetType, Activity activity, int quantityNeeded) {
		assetExtractionBehaviour.extractAsset(person, neededAssetType, activity, quantityNeeded);
	}
}
