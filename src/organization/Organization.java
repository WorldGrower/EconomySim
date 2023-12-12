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
package organization;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import action.PersonAction;
import action.PersonActionFactory;
import asset.Asset;
import asset.AssetContainer;
import environment.Payment;
import person.CauseOfDeath;
import person.Person;
import person.PersonDecisions;
import society.PublicAssets;
import society.PublicOrganizations;
import society.SocialOrder;

public class Organization implements Serializable {

	private final Set<Person> persons = new LinkedHashSet<Person>();
	private final DecisionCriteria decisionCriteria;
	//private final OrganizationAcceptanceCriteria organizationAcceptanceCriteria = OrganizationAcceptanceCriteria.EVERYONE;
	
	private OrganizationPolicy organizationPolicy;
	
	private final List<Person> socialOrderMaintainers = new ArrayList<>();
	
	public Organization(Person creator, DecisionCriteria decisionCriteria) {
		this(creator, decisionCriteria, OrganizationPolicy.DEFAULT);
	}
	
	public Organization(Person creator, DecisionCriteria decisionCriteria, OrganizationPolicy organizationPolicy) {
		if (creator == null) { throw new IllegalArgumentException("creator is null"); }
		add(creator);
		this.decisionCriteria = decisionCriteria;
		this.organizationPolicy = organizationPolicy;
	}
	
	public void add(Person person) {
		if (person == null) {
			throw new IllegalArgumentException("person is null");
		}
		persons.add(person);
	}
	
	public boolean contains(Person person) {
		return persons.contains(person);
	}

	public void remove(Person person) {
		persons.remove(person);
		socialOrderMaintainers.remove(person);
	}
	
	public boolean personCanDecide(Person person) {
		return decisionCriteria.personCanDecide(person);
	}
	
	public void punishThief(Person thief, boolean isKnownThief) {
		organizationPolicy.getTheftPunishment().perform(thief, isKnownThief);
	}
	
	/* TODO
	public boolean canJoin(Person person) {
		return organizationAcceptanceCriteria.acceptsPerson(person);
	}
	*/
	
	public boolean childrenJoinAtBirth() {
		return true;
	}
	
	public void changePolicy(OrganizationPolicy newOrganizationPolicy, AvailableOrganizationPolicies availableOrganizationPolicies) {
		this.organizationPolicy = availableOrganizationPolicies.changePolicy(this.organizationPolicy, newOrganizationPolicy);
	}

	public DecisionCriteriaType getDecisionCriteriaType() {
		return decisionCriteria.getDecisionCriteriaType();
	}

	public OrganizationPolicy getOrganizationPolicy() {
		return organizationPolicy;
	}

	public int size() {
		return persons.size();
	}

	public boolean approvePolicyChange(Person initiator, OrganizationPolicy newOrganizationPolicy, PersonDecisions personDecisions) {
		for(Person person : persons) {
			if (decisionCriteria.personCanDecide(person)) {
				if (person == initiator) {
					// initiator automatically approves policy change
				} else {
					if (!personDecisions.approvePolicyChange(person, initiator, organizationPolicy, newOrganizationPolicy)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public void startTurn(AssetContainer assetContainer, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
		Payment taxes = organizationPolicy.getTaxes();
		if (taxes.getQuantity() > 0) { 
			for(Person person : persons) {
				if (person.isAdult()) {
					SocialObligation socialObligation = SocialObligationFactory.create(SocialObligationType.TAXES, taxes.getAssetType(), taxes.getQuantity(), assetContainer);
					person.getSocialObligations().add(socialObligation);
				}
			}
		}
		Payment wages = organizationPolicy.getSocialOrderMaintainerWage();
		if (wages.getQuantity() > 0) {
			Iterator<Person> socialOrderMaintainerIterator = socialOrderMaintainers.iterator();
			while(socialOrderMaintainerIterator.hasNext()) {
				Person socialOrderMaintainer = socialOrderMaintainerIterator.next();
				SocialObligations socialObligations = socialOrderMaintainer.getSocialObligations();
				if (assetContainer.getQuantityFor(wages.getAssetType()) >= wages.getQuantity()) {
					Asset asset = assetContainer.retrieve(wages.getAssetType(), wages.getQuantity());
					socialOrderMaintainer.getAssets().addAsset(asset);
					if (!socialObligations.hasSocialObligation(SocialObligationType.EMPLOYEE)) {
						socialObligations.add(SocialObligationFactory.create(SocialObligationType.EMPLOYEE, PersonActionFactory.MAINTAIN_SOCIAL_ORDER_PERSON_ACTION, organizationPolicy.getSocialOrderMaintainerHoursWorked(), publicAssets, publicOrganizations));
					}
				} else {
					// fire person
					socialOrderMaintainerIterator.remove();
					socialObligations.remove(SocialObligationType.EMPLOYEE);
				}
			}
		}
	}
	
	public void endTurn(SocialOrder socialOrder, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
		for(Person person : persons) {
			SocialObligations socialObligations = person.getSocialObligations();
			if (socialObligations.size() > 0) {
				if (socialObligations.hasSocialObligation(SocialObligationType.EMPLOYEE)) {
					socialOrderMaintainers.remove(person);
					socialObligations.remove(SocialObligationType.EMPLOYEE);
				}
				if (socialObligations.hasSocialObligation(SocialObligationType.FORCED_LABOR)) {
					person.kill(CauseOfDeath.RETRIBUTION);
				}
				if (socialObligations.hasSocialObligation(SocialObligationType.TAXES)) {
					PersonAction personAction = PersonActionFactory.MAINTAIN_SOCIAL_ORDER_PERSON_ACTION;
					int numberOfHours = 2;
					int numberOfTurns = 10;
					socialObligations.add(SocialObligationFactory.create(SocialObligationType.FORCED_LABOR, personAction, numberOfHours, numberOfTurns, publicAssets, publicOrganizations));
				}
			}
		}
	}

	public void hireSocialOrderMaintainer(Person targetPerson) {
		socialOrderMaintainers.add(targetPerson);
	}

	public boolean isEmployee(Person person) {
		return socialOrderMaintainers.contains(person);
	}
}
