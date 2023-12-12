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
import java.util.List;
import java.util.Map;
import java.util.Random;

import action.OrganizationArgs;
import action.PersonAction;
import action.PersonActionArgs;
import action.PersonActionFactory;
import action.PersonActions;
import action.TradeArgs;
import asset.Asset;
import asset.AssetContainer;
import asset.AssetTrait;
import asset.AssetType;
import asset.Assets;
import environment.LightSourceEnvironment;
import environment.PublicLocations;
import environment.TimeRemaining;
import knowledge.InformationSource;
import knowledge.Knowledge;
import knowledge.KnowledgeHolder;
import organization.SocialObligations;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;
import society.SocialOrder;

public class Person implements LightSourceEnvironment, KnowledgeHolder, Serializable {

	private final Assets assets = new Assets();
	private final Age age;
	private final String name;
	private final Sex sex;
	private final Family family = new Family();
	private final Pregnancy pregnancy;
	private final Random random;
	private final PersonNeeds needs = new PersonNeeds();
	private final PersonKnowledge personKnowledge = new PersonKnowledge();
	private final PersonBehaviour personBehaviour;
	private Profession profession = Profession.NONE;

	private final TimeRemaining timeRemaining = new TimeRemaining(this);
	private final SocialObligations socialObligations = new SocialObligations();
	
	private CauseOfDeath causeOfDeath = null;
	
	public Person(int age, Sex sex) {
		this(age, sex, PersonBehaviour.NONE);
	}
	
	public Person(int age, Sex sex, PersonBehaviour personBehaviour) {
		this(age, sex, 0, personBehaviour);
	}
	
	public Person(int age, Sex sex, int randomSeed, PersonBehaviour personBehaviour) {
		super();
		this.age = new Age(age);
		this.sex = sex;
		this.name = "Person " + this.hashCode();
		this.random = new Random(randomSeed);
		this.personBehaviour = personBehaviour;
		this.pregnancy = new Pregnancy(this, random, personBehaviour);
	}

	public Person(int age, Sex sex, Profession profession) {
		this(age, sex);
		this.profession = profession;
	}

	public Assets getAssets() {
		return assets;
	}
	
	public int getFamilyAssetQuantityFor(AssetType assetType) {
		return family.getQuantityFor(assets, assetType);
	}

	public int getCash() {
		return assets.getQuantityFor(AssetType.CASH);
	}

	public int getAge() {
		return age.getAge();
	}
	
	public Sex getSex() {
		return sex;
	}

	public boolean isPregnant() {
		return pregnancy.isPregnant();
	}
	
	public Family getFamily() {
		return family;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public void learnLearnableKnowledge(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations, PersonDecisions personDecisions) {
		TimeRemaining timeRemaining = person.getTimeRemaining();
		while (timeRemaining.hasRemainingTime(1)) {
			Knowledge knowledge = personKnowledge.getLearnableKnowledgeInProgress();
			if (knowledge == null) {
				List<Knowledge> learnableKnowledge = personKnowledge.getLearnableKnowledge();
				if (learnableKnowledge.contains(Knowledge.LANGUAGE)) {
					knowledge = Knowledge.LANGUAGE;
				}
			}
			if (knowledge != null) {
				learnKnowledge(knowledge, personFinder, publicAssets, publicKnowledge, publicOrganizations);
			} else {
				break;
			}
		}
	}
	
	public void processActions(PersonDecisions personDecisions, ProfessionStatistics professionStatistics) {
		personBehaviour.processActions(this, personDecisions, professionStatistics);
	}
	
	public FutureNeeds calculateFutureNeeds() {
		return new FutureNeeds(this, needs);
	}

	public void learnKnowledge(Knowledge knowledge, PersonFinder personFinder, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
		PersonAction personAction = PersonActionFactory.LEARN_KNOWLEDGE_PERSON_ACTION;
		if (personAction.canPerform(this, timeRemaining, publicAssets, publicOrganizations)) {
			PersonActionArgs args = new PersonActionArgs(null, knowledge, null, OrganizationArgs.NONE, PersonDecisions.AI);
			personAction.perform(this, timeRemaining, args, publicAssets, publicKnowledge, publicOrganizations);
		}
	}
	
	public void learnKnowledgeIfNeeded(Knowledge knowledge, PersonFinder personFinder, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
		List<Knowledge> learnableKnowledge = personKnowledge.getLearnableKnowledge();
		if (learnableKnowledge.contains(knowledge)) {
			learnKnowledge(knowledge, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		}
	}
	
	public CauseOfDeath processTurn(List<Person> personList, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
		if (personList == null) { throw new IllegalArgumentException("personList is null"); }

		if (causeOfDeath != null) { return causeOfDeath; }
		
		timeRemaining.reset(publicOrganizations, this.hasKnowledge(Knowledge.CALENDAR));
		pregnancy.processTurn(personList, publicOrganizations, publicAssets);
		needs.consumeFoodAndWater(getFamily().getFamilyAssets(assets));
		assets.endOfTurn(getMaxQuantity(), publicAssets);
		
		CauseOfDeath needscauseOfDeath = needs.checkFoodAndWater(assets);
		if (needscauseOfDeath != null) { return needscauseOfDeath; }
		socialObligations.endTurn();
		return age.processTurn();
	}
	
	public int getMaxQuantity() {
		boolean hasAccountingSystem = assets.hasTrait(AssetTrait.ACCOUNTING_SYSTEM);
		if (hasAccountingSystem && hasKnowledge(Knowledge.WRITING)) {
			return Integer.MAX_VALUE;
		} else if (hasAccountingSystem) {
			return 300;
		} else {
			return 150;
		}
	}

	public boolean isValidPartner(Person otherPerson) {
		return otherPerson.getFamily().getPartner() == null 
				&& sex != otherPerson.sex
				&& otherPerson.age.isAdult();
	}
	
	public boolean isAdult() {
		return age.isAdult();
	}

	public Asset findAsset(PublicAssets publicAssets, AssetType neededAssetType) {
		Asset asset = publicAssets.findAsset(neededAssetType);
		if (asset == null) {
			asset = family.findAssetToWorkOn(assets, neededAssetType);
		}
		return asset;
	}
	
	public Asset getAsset(PublicAssets publicAssets, AssetType assetType) {
		Asset asset = publicAssets.get(assetType);
		if (asset == null) {
			asset = family.getAsset(assets, assetType);
		}
		return asset;
	}
	
	public void onDeath(AssetContainer assetContainer) {
		family.divideAssets(assets, assetContainer);
		family.onDeath(this);
	}

	public void increaseCash(int cashIncrease) {
		assets.increaseQuantity(AssetType.CASH, cashIncrease);
	}

	public String getFoodDescription() {
		return needs.getFoodDescription();
	}
	
	public String getWaterDescription() {
		return needs.getWaterDescription();
	}

	public void impregnate() {
		pregnancy.impregnate();
	}

	public void processPlayerActions(PlayerPersonActions playerPersonActions, PersonFinder personFinder, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
		PersonActions personActions = playerPersonActions.getPersonActions();
		for(int i=0; i<personActions.size(); i++) {
			PersonAction personAction = personActions.get(i);
			if (personAction.canPerform(this, timeRemaining, publicAssets, publicOrganizations)) {
				PersonActionArgs args = playerPersonActions.getArgs(i);
				personAction.perform(this, timeRemaining, args, publicAssets, publicKnowledge, publicOrganizations);
			}
		}
	}

	public TimeRemaining getTimeRemaining() {
		return timeRemaining;
	}

	@Override
	public boolean hasKnowledge(Knowledge requiredKnowledge) {
		return personKnowledge.hasKnowledge(requiredKnowledge);
	}
	
	@Override
	public int getKnowledgePercentageKnown(Knowledge knowledge) {
		return personKnowledge.getKnowledgePercentageKnown(knowledge);
	}
	
	public List<Knowledge> getLearnableKnowledge() {
		return personKnowledge.getLearnableKnowledge();
	}

	public void increaseKnowledge(Knowledge knowledge, InformationSource informationSource, PublicKnowledge publicKnowledge) {
		personKnowledge.increaseKnowledge(knowledge, informationSource, publicKnowledge);
	}

	@Override
	public boolean isLightedAtNight() {
		return family.findAssetWithTrait(assets, AssetTrait.LIGHT_SOURCE);
	}

	public List<Knowledge> getKnownKnowledge() {
		return personKnowledge.getKnownKnowledge();
	}
	
	public Map<Knowledge, String> getKnowledgeDescription() {
		return personKnowledge.getDescription();
	}

	public void stealFrom(Person targetPerson, TradeArgs tradeArgs, SocialOrder socialOrder) {
		int knownThiefPenalty = socialOrder.calculateKnownThiefPenalty(this); 
		int successThresHold = 25 + knownThiefPenalty;
		int detectedThreshold = 50 + knownThiefPenalty;
		int roll = random.nextInt(100);
		
		boolean success = roll >= successThresHold;
		boolean detected = roll >= detectedThreshold;
		
		if (success) {
			tradeArgs.perform(this, targetPerson);
		}
		if (detected) {
			socialOrder.punishThief(this, targetPerson, tradeArgs, success);
		}
		if (success || detected) {
			socialOrder.registerTheft(this, targetPerson, success, detected);
		}
	}
	
	public SocialObligations getSocialObligations() {
		return socialObligations;
	}
	
	public void kill(CauseOfDeath causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}
	
	//TODO: for testing: make Person interface to make this method no longer necessary
	public CauseOfDeath getCauseOfDeath() {
		return causeOfDeath;
	}

	public Profession getProfession() {
		return profession;
	}

	public void chooseProfession(PublicKnowledge publicKnowledge, ProfessionStatistics professionStatistics) {
		if (profession == Profession.NONE) {
			this.profession = ProfessionChooser.choose(this, publicKnowledge, professionStatistics);
		}
	}
}