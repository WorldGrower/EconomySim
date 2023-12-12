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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import action.PersonActionFactory;
import asset.AssetType;
import asset.Assets;
import asset.SimpleAsset;
import environment.Payment;
import person.CauseOfDeath;
import person.Person;
import person.Sex;
import society.MockPublicAssets;
import society.MockPublicOrganizations;
import society.MockSocialOrder;
import society.Society;
import society.TheftPunishment;

public class UTestOrganization {
	
	@Test
	public void testTheftChangePolicy() {
		Person creator = new Person(18, Sex.FEMALE);
		Organization organization = new Organization(creator, DecisionCriteriaFactory.createOligarchy());
		assertEquals(TheftPunishment.DEATH_FOR_KNOWN_THIEF, organization.getOrganizationPolicy().getTheftPunishment());
		
		AvailableOrganizationPolicies availableOrganizationPolicies = new MockAvailableOrganizationPolicies(true, true);
		OrganizationPolicy organizationPolicy = new OrganizationPolicy(TheftPunishment.DEATH, Payment.NONE, Payment.NONE, 0);
		organization.changePolicy(organizationPolicy, availableOrganizationPolicies);
		
		assertEquals(TheftPunishment.DEATH, organization.getOrganizationPolicy().getTheftPunishment());
	}
	
	@Test
	public void testRemove() {
		Person creator = new Person(18, Sex.FEMALE);
		Organization organization = new Organization(creator, DecisionCriteriaFactory.createOligarchy());
		Person person = new Person(18, Sex.FEMALE);
		organization.add(person);
		organization.hireSocialOrderMaintainer(person);
		
		assertEquals(true, organization.contains(person));
		assertEquals(true, organization.isEmployee(person));
		
		organization.remove(person);
		assertEquals(false, organization.contains(person));
		assertEquals(false, organization.isEmployee(person));
	}
	
	@Test
	public void testStartTurn() {
		Person creator = new Person(18, Sex.FEMALE);
		OrganizationPolicy organizationPolicy = OrganizationPolicy.DEFAULT.changeTaxes(new Payment(AssetType.WHEAT, 10));
		Organization organization = new Organization(creator, DecisionCriteriaFactory.createOligarchy(), organizationPolicy);
		
		Person commoner = new Person(18, Sex.FEMALE);
		commoner.getAssets().addAsset(new SimpleAsset(AssetType.WHEAT, 50));
		organization.add(commoner);
		
		Person employee = new Person(18, Sex.FEMALE);
		organization.hireSocialOrderMaintainer(employee);
		
		Person employee2 = new Person(18, Sex.FEMALE);
		organization.hireSocialOrderMaintainer(employee2);
		
		Assets organizationAssets = new Assets();
		organizationAssets.addAsset(new SimpleAsset(AssetType.WHEAT, 150));
		
		organization.startTurn(organizationAssets, new MockPublicAssets(), new MockPublicOrganizations(organization));
		
		assertEquals(1, commoner.getSocialObligations().size());
		assertEquals(100, employee.getAssets().getQuantityFor(AssetType.WHEAT));
		assertEquals(50, organizationAssets.getQuantityFor(AssetType.WHEAT));
		assertEquals(0, employee2.getAssets().getQuantityFor(AssetType.WHEAT));
		
		assertEquals(true, organization.isEmployee(employee));
		assertEquals(false, organization.isEmployee(employee2));
	}
	
	@Test
	public void testEndTurnEmployee() {
		Person creator = new Person(18, Sex.FEMALE);
		Organization organization = new Organization(creator, DecisionCriteriaFactory.createOligarchy());
		
		Person employee = new Person(18, Sex.MALE);
		organization.hireSocialOrderMaintainer(employee);
		employee.getSocialObligations().add(SocialObligationFactory.create(SocialObligationType.EMPLOYEE, PersonActionFactory.HIRE_SOCIAL_ORDER_MAINTAINER_PERSON_ACTION, 6, new MockPublicAssets(), new MockPublicOrganizations(organization)));
		organization.add(employee);
		
		organization.endTurn(new MockSocialOrder(), new MockPublicAssets(), new MockPublicOrganizations(organization));
	
		assertEquals(false, organization.isEmployee(employee));
		assertEquals(0, employee.getSocialObligations().size());
	}
	
	@Test
	public void testEndTurnTaxes() {
		Person creator = new Person(18, Sex.FEMALE);
		OrganizationPolicy organizationPolicy = OrganizationPolicy.DEFAULT.changeTaxes(new Payment(AssetType.WHEAT, 10));
		Organization organization = new Organization(creator, DecisionCriteriaFactory.createOligarchy(), organizationPolicy);
		
		Person commoner = new Person(18, Sex.MALE);
		commoner.getSocialObligations().add(SocialObligationFactory.create(SocialObligationType.TAXES, AssetType.WHEAT, 1, new Assets()));
		organization.add(commoner);
		
		organization.endTurn(new MockSocialOrder(), new MockPublicAssets(), new MockPublicOrganizations(organization));
	
		assertEquals(2, commoner.getSocialObligations().size());
		assertEquals(true, commoner.getSocialObligations().hasSocialObligation(SocialObligationType.TAXES));
		assertEquals(true, commoner.getSocialObligations().hasSocialObligation(SocialObligationType.FORCED_LABOR));
	}
	
	@Test
	public void testEndTurnForcedLabor() {
		Person creator = new Person(18, Sex.FEMALE);
		OrganizationPolicy organizationPolicy = OrganizationPolicy.DEFAULT.changeTaxes(new Payment(AssetType.WHEAT, 10));
		Organization organization = new Organization(creator, DecisionCriteriaFactory.createOligarchy(), organizationPolicy);
		
		Person commoner = new Person(18, Sex.MALE);
		commoner.getSocialObligations().add(SocialObligationFactory.create(SocialObligationType.FORCED_LABOR, PersonActionFactory.MAINTAIN_SOCIAL_ORDER_PERSON_ACTION, 2, 10, new MockPublicAssets(), new MockPublicOrganizations(organization)));
		organization.add(commoner);
		
		MockSocialOrder socialOrder = new MockSocialOrder();
		organization.endTurn(socialOrder, new MockPublicAssets(), new MockPublicOrganizations(organization));
		assertEquals(CauseOfDeath.RETRIBUTION, commoner.getCauseOfDeath());
	}
	
	@Test
	public void testEndTurnForcedLaborKilled() {
		Person creator = new Person(18, Sex.FEMALE);
		Organization organization = new Organization(creator, DecisionCriteriaFactory.createOligarchy(), OrganizationPolicy.DEFAULT);
		
		Person commoner = new Person(18, Sex.MALE);
		commoner.getSocialObligations().add(SocialObligationFactory.create(SocialObligationType.FORCED_LABOR, PersonActionFactory.MAINTAIN_SOCIAL_ORDER_PERSON_ACTION, 2, 10, new MockPublicAssets(), new MockPublicOrganizations(organization)));
		organization.add(commoner);
		organization.add(new Person(18, Sex.MALE));
		
		Society society = new Society();
		society.setOrganization(organization);
		society.addPerson(commoner);
		
		society.nextTurn(1, null, null);
		
		assertEquals(false, organization.contains(commoner));
		assertEquals(null, society.findPerson(p -> p == commoner));
	}
}
