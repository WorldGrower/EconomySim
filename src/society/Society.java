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
package society;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import action.TradeArgs;
import asset.Asset;
import asset.AssetContainer;
import asset.AssetType;
import asset.Assets;
import environment.PublicLocations;
import environment.PublicLocationsImpl;
import knowledge.Knowledge;
import organization.Organization;
import person.CauseOfDeath;
import person.Person;
import person.PersonDecisions;
import person.PlayerPersonActions;
import person.ProfessionStatistics;

public class Society implements AssetContainer, PersonFinder, PublicOrganizations, PublicAssets, PublicKnowledge, Serializable {

	private List<Person> persons = new ArrayList<Person>();
	private Assets assets = new Assets();
	private SocietyKnowledge societyKnowledge = new SocietyKnowledge();
	private final PublicLocations publicLocations;
	private final DeathStatistics deathStatistics = new DeathStatistics();
	private final TradeOverviewImpl tradeOverview = new TradeOverviewImpl();
	
	private Person controlledPerson = null;
	private Organization organization;
	private boolean calendarSystemExists = false;
	
	private final SocialOrderImpl socialOrder = new SocialOrderImpl();
	
	public Society() {
		this.publicLocations = new PublicLocationsImpl();
	}
	
	public Society(PublicLocations publicLocations) {
		this.publicLocations = publicLocations;
	}
	
	public void addPerson(Person person) {
		persons.add(person);
	}
	
	public void addPersons(List<Person> newPersons) {
		persons.addAll(newPersons);
	}

	public List<Person> getReadOnlyList() {
		return Collections.unmodifiableList(persons);
	}
	
	@Override
	public int size() {
		return persons.size();
	}
	
	public void nextTurn(int numberOfTurns, PlayerPersonActions playerPersonActions, PersonDecisions personBehaviour) {
		if (controlledPerson != null && playerPersonActions == null) { throw new IllegalArgumentException("playerPersonActions is null when controlledPerson is not null"); }
		for(int i=0; i<numberOfTurns; i++) {
			nextTurn(playerPersonActions, personBehaviour);
		}
	}

	private void nextTurn(PlayerPersonActions playerPersonActions, PersonDecisions personBehaviour) {
		socialOrder.startTurn();
		if (organization != null) { organization.startTurn(assets, this, this); }
		regenerateAssets();
		if (organization != null) { organization.endTurn(this, this, this); }
		socialOrder.endTurn(persons.size());
		tradeOverview.endTurn();
		
		personsNextTurn(playerPersonActions, personBehaviour); // personsNextTurn should come last as persons marked for death are killed in this method
	}

	private void regenerateAssets() {
		for(Person person : persons) {
			person.getAssets().regenerate();
		}
		assets.regenerate();
	}

	private void personsNextTurn(PlayerPersonActions playerPersonActions, PersonDecisions personBehaviour) {
		ProfessionStatistics professionStatistics = new ProfessionStatistics(persons);
		List<Person> bornPersonList = new ArrayList<>();
		final Iterator<Person> personIterator = persons.iterator();
		while (personIterator.hasNext()) {
			Person person = personIterator.next();
			
			//TODO: person can't take any actions while marked for death
			if (person == controlledPerson) {
				person.processPlayerActions(playerPersonActions, this, this, this, this);
			} else {
				person.processActions(personBehaviour, professionStatistics);
			}
			CauseOfDeath causeOfDeath = person.processTurn(bornPersonList, this, this);
			if (causeOfDeath != null) {
				personIterator.remove();
				personDied(person, causeOfDeath);
			}
		}
		addPersons(bornPersonList);
		deathStatistics.endTurn();
	}
	
	private void personDied(Person person, CauseOfDeath causeOfDeath) {
		person.onDeath(this);
		if (controlledPerson == person) {
			controlledPerson = null;
		}
		if (organization != null) {
			organization.remove(person);
		}
		deathStatistics.addDeath(causeOfDeath);
		socialOrder.onDeath(person);
	}

	@Override
	public void addAssets(Assets assetsIncrease) {
		assetsIncrease.removeNonPublicAssets();
		assets.addAssets(assetsIncrease);
	}
	
	@Override
	public void addAsset(Asset asset) {
		if (asset.getAssetType().canBePublic()) {
			assets.addAsset(asset);
		}
	}

	@Override
	public Person findPerson(PersonAcceptanceFunction personAcceptanceFunction) {
		return PersonFinderImpl.findPerson(persons, personAcceptanceFunction);
	}
	
	@Override
	public List<Person> findPersons(PersonAcceptanceFunction personAcceptanceFunction, int limit) {
		return PersonFinderImpl.findPersons(persons, personAcceptanceFunction, limit);
	}

	public PopulationStatistics getPopulationStatistics() {
		return new PopulationStatistics(getReadOnlyList());
	}
	
	public DeathStatistics getDeathStatistics() {
		return deathStatistics;
	}

	public int getCash() {
		return assets.getQuantityFor(AssetType.CASH);
	}

	public int getUnclaimedLand() {
		return assets.getQuantityFor(AssetType.LAND);
	}

	@Override
	public Asset findAsset(AssetType neededAssetType) {
		return assets.getProducingAsset(neededAssetType);
	}

	@Override
	public Collection<AssetType> getAssetTypes() {
		return assets.keys();
	}

	@Override
	public Asset get(AssetType assetType) {
		return assets.get(assetType);
	}

	public Person getControlledPerson() {
		return controlledPerson;
	}

	public void setControlledPerson(Person person) {
		controlledPerson = person;
	}

	@Override
	public boolean canObserveKnowledge(Knowledge knowledge) {
		return societyKnowledge.canObserveKnowledge(knowledge);
	}

	@Override
	public void addKnowledge(Knowledge knowledge) {
		societyKnowledge.addKnowledge(knowledge);
	}
	
	public PublicLocations getPublicLocations() {
		return publicLocations;
	}

	@Override
	public Organization findOrganization() {
		return organization;
	}
	
	@Override
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	@Override
	public void registerTheft(Person person, Person targetPerson, boolean success, boolean detected) {
		socialOrder.registerTheft(person, targetPerson, success, detected);
	}

	@Override
	public int calculateKnownThiefPenalty(Person person) {
		return socialOrder.calculateKnownThiefPenalty(person, persons.size());
	}

	@Override
	public void punishThief(Person thief, Person victim, TradeArgs tradeArgs, boolean success) {
		if (success) {
			tradeArgs.perform(victim, thief);
		}

		boolean isKnownThief = socialOrder.isKnownThief(thief);
		if (organization != null && organization.contains(thief)) {
			organization.punishThief(thief, isKnownThief);
		} else {
			TheftPunishment.DEATH_FOR_KNOWN_THIEF.perform(thief, isKnownThief);
		}
	}

	@Override
	public void maintainSocialOrder() {
		socialOrder.maintainSocialOrder();
	}

	public int getLatestNumberOfThefts() {
		return socialOrder.getLatestNumberOfThefts();
	}

	@Override
	public int getQuantityFor(AssetType assetType) {
		return assets.getQuantityFor(assetType);
	}

	@Override
	public Asset retrieve(AssetType assetType, int quantity) {
		return assets.retrieve(assetType, quantity);
	}

	@Override
	public List<AssetType> getSortedAssetKeys() {
		return assets.getSortedAssetKeys();
	}

	@Override
	public void createCalendar() {
		this.calendarSystemExists = true;
	}

	@Override
	public boolean calendarExists() {
		return calendarSystemExists;
	}

	@Override
	public void addPerformedTrade(PerformedTrade performedTrade) {
		tradeOverview.addPerformedTrade(performedTrade);
	}

	@Override
	public List<PerformedTrade> getPerformedTrades() {
		return tradeOverview.getPerformedTrades();
	}

	@Override
	public Assets retrieveAssetsForNewPerson() {
		Assets assetsForNewPerson = assets.retrieveLocationAssetsForOneLocation();
		for(AssetType assetType : AssetType.VALUES) {
			if (assetType.isTradeable() && assetType != AssetType.RIVER) {
				if (assets.getQuantityFor(assetType) > 0) {
					assetsForNewPerson.addAsset(assets.retrieve(assetType, 1));
				}
			}
		}
		return assetsForNewPerson;
	}
}
