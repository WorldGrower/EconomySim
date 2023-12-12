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

import asset.AssetType;
import environment.PublicLocations;
import knowledge.Knowledge;
import person.SurplusAssetCalculator.SurplusAsset;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class TradeBehaviour implements PersonBehaviour, Serializable {
	
	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	private final TradeNeedsNotMetAction tradeNeedsNotMetAction;
	private final SurplusAssetTrader surplusAssetTrader;
	
	public TradeBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
		this.tradeNeedsNotMetAction = new TradeNeedsNotMetAction(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
		this.surplusAssetTrader = new SurplusAssetTrader(personFinder, publicOrganizations, publicAssets, publicKnowledge, publicLocations);
	}
	
	@Override
	public void processActions(Person person, PersonDecisions personDecisions, ProfessionStatistics professionStatistics) {
		SurplusAsset surplusAsset = SurplusAssetCalculator.calculateSurplusAsset(person);
		AssetType surplusAssetType = surplusAsset.getSurplusAssetType();
		AssetType neededAssetType = surplusAsset.getNeededAssetType();
		
		if (surplusAssetType != null && neededAssetType != null && surplusAssetType != neededAssetType) {
			person.learnKnowledgeIfNeeded(Knowledge.LANGUAGE, personFinder, publicAssets, publicKnowledge, publicOrganizations);
			person.learnKnowledgeIfNeeded(Knowledge.TRADE, personFinder, publicAssets, publicKnowledge, publicOrganizations);
			if (person.hasKnowledge(Knowledge.TRADE)) {
				surplusAssetTrader.tradeSurplusAssets(person, surplusAssetType, neededAssetType);
			}
		}
		if (person.hasKnowledge(Knowledge.TRADE)) {
			FutureNeeds futureNeeds = person.calculateFutureNeeds();
			futureNeeds.checkNeeds(person, tradeNeedsNotMetAction);
		}
	}
}
