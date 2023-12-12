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

import action.OrganizationArgs;
import action.PersonAction;
import action.PersonActionArgs;
import action.PersonActionFactory;
import asset.AssetType;
import environment.Payment;
import environment.PublicLocations;
import knowledge.Knowledge;
import organization.AvailableOrganizationPolicies;
import organization.AvailableOrganizationPoliciesImpl;
import organization.DecisionCriteriaFactory;
import organization.Organization;
import organization.OrganizationPolicy;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

class GovernmentBehaviour implements PersonBehaviour, Serializable {
	
	private final PersonFinder personFinder;
	private final PublicOrganizations publicOrganizations;
	private final PublicAssets publicAssets;
	private final PublicKnowledge publicKnowledge;
	private final PublicLocations publicLocations;
	
	public GovernmentBehaviour(PersonFinder personFinder, PublicOrganizations publicOrganizations, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicLocations publicLocations) {
		super();
		this.personFinder = personFinder;
		this.publicOrganizations = publicOrganizations;
		this.publicAssets = publicAssets;
		this.publicKnowledge = publicKnowledge;
		this.publicLocations = publicLocations;
	}
	
	@Override
	public void processActions(Person person, PersonDecisions personDecisions, ProfessionStatistics professionStatistics) {
		person.learnKnowledgeIfNeeded(Knowledge.CENTRALIZED_GOVERNMENT, personFinder, publicAssets, publicKnowledge, publicOrganizations);

		if (publicOrganizations.findOrganization() == null) {
			PersonAction personAction = PersonActionFactory.CREATE_ORGANIZATION_PERSON_ACTION;
			if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
				OrganizationArgs organizationArgs = new OrganizationArgs(DecisionCriteriaFactory.createOligarchy(), OrganizationPolicy.DEFAULT);
				PersonActionArgs args = new PersonActionArgs(null, null, null, organizationArgs, PersonDecisions.AI);
				personAction.perform(person, person.getTimeRemaining(), args, publicAssets, publicKnowledge, publicOrganizations);
			}
		}
		
		Organization organization = publicOrganizations.findOrganization();
		if (organization != null && organization.contains(person) && organization.size() < personFinder.size()) {
			PersonAction personAction = PersonActionFactory.ASK_OTHER_JOIN_ORGANIZATION_PERSON_ACTION;
			if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
				Person targetPerson = personAction.getFirstTargetPerson(person, personFinder, publicOrganizations);
				if (targetPerson != null) {
					PersonActionArgs args = new PersonActionArgs(targetPerson, null, null, OrganizationArgs.NONE, PersonDecisions.AI);
					personAction.perform(person, person.getTimeRemaining(), args, publicAssets, publicKnowledge, publicOrganizations);
				}
			}
		}
		
		person.learnKnowledgeIfNeeded(Knowledge.ARITHMETIC, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		person.learnKnowledgeIfNeeded(Knowledge.RULE_OF_LAW, personFinder, publicAssets, publicKnowledge, publicOrganizations);
		
		if (organization != null) {
			OrganizationPolicy oldOrganizationPolicy = organization.getOrganizationPolicy();
			if (oldOrganizationPolicy.getTaxes().getQuantity() < 1) {
				AvailableOrganizationPolicies availableOrganizationPolicies = new AvailableOrganizationPoliciesImpl(PersonActionFactory.CHANGE_POLICY_PERSON_ACTION, publicKnowledge);
				if (availableOrganizationPolicies.canChangeTaxesWages()) {
					OrganizationPolicy newOrganizationPolicy = oldOrganizationPolicy.changeTaxes(new Payment(AssetType.WHEAT, 1));
					changePolicy(person, newOrganizationPolicy);
				}
			}
		}
		if (organization != null) {
			OrganizationPolicy oldOrganizationPolicy = organization.getOrganizationPolicy();
			Payment wage = oldOrganizationPolicy.getSocialOrderMaintainerWage();
			if (publicAssets.getQuantityFor(wage.getAssetType()) >= wage.getQuantity()) {
				hireSocialOrderMaintainer(person, organization, personDecisions);
			}
		}
	}

	//TODO: extract personAction perform
	private void hireSocialOrderMaintainer(Person person, Organization organization, PersonDecisions personDecisions) {
		PersonAction personAction = PersonActionFactory.HIRE_SOCIAL_ORDER_MAINTAINER_PERSON_ACTION;
		if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
			Person targetPerson = personAction.getFirstTargetPerson(person, personFinder, publicOrganizations);
			
			PersonActionArgs args = new PersonActionArgs(targetPerson, null, null, OrganizationArgs.NONE, personDecisions);
			personAction.perform(person, person.getTimeRemaining(), args, publicAssets, publicKnowledge, publicOrganizations);
		}
	}

	private void changePolicy(Person person, OrganizationPolicy newOrganizationPolicy) {
		PersonAction personAction = PersonActionFactory.CHANGE_POLICY_PERSON_ACTION;
		if (personAction.canPerform(person, person.getTimeRemaining(), publicAssets, publicOrganizations)) {
			OrganizationArgs organizationArgs = new OrganizationArgs(null, newOrganizationPolicy);
			PersonActionArgs args = new PersonActionArgs(null, null, null, organizationArgs, PersonDecisions.AI);
			personAction.perform(person, person.getTimeRemaining(), args, publicAssets, publicKnowledge, publicOrganizations);
		}
	}
}
