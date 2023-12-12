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

class WritingSystemBehaviour implements PersonBehaviour, Serializable {
	
	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	private final AssetExtractionBehaviour assetExtractionBehaviour;
	
	public WritingSystemBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
		this.assetExtractionBehaviour = new AssetExtractionBehaviour(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
	}
	
	@Override
	public void processActions(Person person, PersonDecisions personDecisions, ProfessionStatistics professionStatistics) {
		person.learnKnowledgeIfNeeded(Knowledge.PROTO_WRITING, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		
		if (!person.getAssets().hasTrait(AssetTrait.ACCOUNTING_SYSTEM)) {
			assetExtractionBehaviour.extractAsset(person, AssetType.ACCOUNTING_SYSTEM, Activity.CREATE_ACCOUNTING_SYSTEM, 1);
		}
		
		person.learnKnowledgeIfNeeded(Knowledge.WRITING, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		//TODO: create writing items
	}

}
