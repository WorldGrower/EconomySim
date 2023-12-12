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
package action;

import java.util.ArrayList;
import java.util.List;

import asset.Asset;
import environment.PublicLocations;
import knowledge.InformationSource;
import knowledge.Knowledge;
import organization.AvailableOrganizationPolicies;
import organization.AvailableOrganizationPoliciesImpl;
import organization.Organization;
import organization.OrganizationPolicy;
import person.Person;
import person.PersonDecisions;
import person.Sex;
import society.PerformedTrade;
import society.PersonFinder;
import society.PublicAssets;
import society.PublicKnowledge;
import society.PublicOrganizations;

public class PersonActionFactory {

	public static final PersonAction PARTNER_UP_PERSON_ACTION = new PartnerUpPersonAction();
	public static final PersonAction HAVE_SEX_PERSON_ACTION = new HaveSexPersonAction();
	public static final PersonAction SHARE_KNOWLEDGE_PERSON_ACTION = new ShareKnowledgePersonAction();
	public static final PersonAction LEARN_KNOWLEDGE_PERSON_ACTION = new LearnKnowledgePersonAction();
	public static final PersonAction TRADE_PERSON_ACTION = new TradePersonAction();
	
	public static final PersonAction CREATE_CALENDAR_SYSTEM_PERSON_ACTION = new CreateCalendarPersonAction();
	public static final PersonAction CREATE_ORGANIZATION_PERSON_ACTION = new CreateOrganizationPersonAction();
	public static final PersonAction ASK_OTHER_JOIN_ORGANIZATION_PERSON_ACTION = new AskOtherJoinOrganizationPersonAction();
	public static final PersonAction JOIN_ORGANIZATION_PERSON_ACTION = new JoinOrganizationPersonAction();
	public static final PersonAction STEAL_PERSON_ACTION = new StealPersonAction();
	public static final PersonAction MAINTAIN_SOCIAL_ORDER_PERSON_ACTION = new MaintainSocialOrderPersonAction();
	public static final PersonAction CHANGE_POLICY_PERSON_ACTION = new ChangePolicyPersonAction();
	public static final PersonAction HIRE_SOCIAL_ORDER_MAINTAINER_PERSON_ACTION = new HireSocialOrderMaintainerPersonAction();
	
	public static PersonActions createSortedPersonActions(Person person, PublicAssets publicAssets, PublicLocations publicLocations) {
		PersonActions personActions = new PersonActions();
		
		personActions.addAll(AssetPersonActions.createAssetPersonActions(person, publicAssets, publicLocations));
		personActions.addAll(InfrastructurePersonActions.createInfrastructurePersonActions());
		addPersonalPersonActions(personActions);
		addOrganizationPersonActions(personActions);
		
		personActions = personActions.filterOutUnavailablePersonActions(person);
		personActions = personActions.sortPersonActions();
		
		return personActions;
	}
	
	
	public static PersonActions createAllPersonActions() {
		PersonActions personActions = new PersonActions();
		
		personActions.addAll(AssetPersonActions.createAllAssetPersonActions());
		personActions.addAll(InfrastructurePersonActions.createInfrastructurePersonActions());
		addPersonalPersonActions(personActions);
		addOrganizationPersonActions(personActions); 

		return personActions;
	}

	private static void addPersonalPersonActions(PersonActions personActions) {
		personActions.add(LEARN_KNOWLEDGE_PERSON_ACTION);
		personActions.add(PARTNER_UP_PERSON_ACTION);
		personActions.add(HAVE_SEX_PERSON_ACTION);
		personActions.add(SHARE_KNOWLEDGE_PERSON_ACTION);
		personActions.add(TRADE_PERSON_ACTION);
	}

	private static void addOrganizationPersonActions(PersonActions personActions) {
		personActions.add(CREATE_CALENDAR_SYSTEM_PERSON_ACTION);
		
		personActions.add(CREATE_ORGANIZATION_PERSON_ACTION);
		personActions.add(ASK_OTHER_JOIN_ORGANIZATION_PERSON_ACTION);
		personActions.add(JOIN_ORGANIZATION_PERSON_ACTION);
		personActions.add(STEAL_PERSON_ACTION);
		personActions.add(MAINTAIN_SOCIAL_ORDER_PERSON_ACTION);
		personActions.add(CHANGE_POLICY_PERSON_ACTION);
		personActions.add(HIRE_SOCIAL_ORDER_MAINTAINER_PERSON_ACTION);
	}
	
	public static PersonAction createAssetPersonAction(Asset asset, Activity activity, PublicAssets publicAssets, PublicLocations publicLocations) {
		return AssetPersonActions.createAssetPersonAction(asset, activity, publicAssets, publicLocations);
	}
	
	private static class LearnKnowledgePersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			Knowledge knowledge = args.getKnowledge();
			person.increaseKnowledge(knowledge, InformationSource.PRACTICE, publicKnowledge);
		}

		@Override
		public List<Knowledge> getPossibleKnowledge(Person person, Person targetPerson) {
			return person.getLearnableKnowledge();
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return true;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "learn knowledge";
		}
	}
	
	private static class ShareKnowledgePersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			Person targetPerson = args.getTargetPerson();
			Knowledge knowledge = args.getKnowledge();
			targetPerson.increaseKnowledge(knowledge, InformationSource.SPEECH, publicKnowledge);
		}
	
		@Override
		public List<Person> getPossibleTargetPersons(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations, int limit) {
			return personFinder.findPersons(p -> p != person && p.hasKnowledge(Knowledge.LANGUAGE), limit);
		}

		@Override
		public List<Knowledge> getPossibleKnowledge(Person person, Person targetPerson) {
			List<Knowledge> possibleKnowledgeList = new ArrayList<>(person.getKnownKnowledge());
			possibleKnowledgeList.removeAll(targetPerson.getKnownKnowledge());
			return possibleKnowledgeList;
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return true;
		}

		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.LANGUAGE;
		}

		@Override
		public int getTimeRequired() {
			return 2;
		}

		@Override
		public String getName() {
			return "share knowledge";
		}
	}
	
	private static class TradePersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			Person targetPerson = args.getTargetPerson();
			TradeArgs tradeArgs = args.getTradeArgs();
			PerformedTrade performedTrade = tradeArgs.perform(person, targetPerson);
			if (performedTrade != null) {
				publicOrganizations.addPerformedTrade(performedTrade);
			}
		}
	
		@Override
		public List<Person> getPossibleTargetPersons(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations, int limit) {
			return personFinder.findPersons(p -> p != person && p.hasKnowledge(Knowledge.LANGUAGE), limit);
		}

		@Override
		public boolean requiresTradeArgs() {
			return true;
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return true;
		}
		
		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.TRADE;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "trade assets";
		}
	}
	
	private static class CreateCalendarPersonAction extends AbstractPersonAction {
		
		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			publicOrganizations.createCalendar();
			publicKnowledge.addKnowledge(Knowledge.CELESTIAL_OBSERVATION);
		}
	
		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return !publicOrganizations.calendarExists();
		}
		
		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.CALENDAR;
		}

		@Override
		public int getTimeRequired() {
			return 4;
		}

		@Override
		public String getName() {
			return "create calendar";
		}
	}
	
	private static class CreateOrganizationPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			Organization organization = new Organization(person, args.getDecisionCriteria());
			publicOrganizations.setOrganization(organization);
		}
	
		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return publicOrganizations.findOrganization() == null;
		}
		
		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.CENTRALIZED_GOVERNMENT;
		}
		
		@Override
		public boolean requiresDecisionCriteria() {
			return true;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "create organization";
		}
	}
	
	private static class AskOtherJoinOrganizationPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			Organization targetOrganization = publicOrganizations.findOrganization();
			Person targetPerson = args.getTargetPerson();
			PersonDecisions personDecisions = args.getPersonDecisions();
		
			if (personDecisions.personWantsToJoin(targetOrganization, targetPerson, person)) {
				targetOrganization.add(targetPerson);
			}
		}
	
		@Override
		public List<Person> getPossibleTargetPersons(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations, int limit) {
			Organization organization = publicOrganizations.findOrganization();
			return personFinder.findPersons(p -> p != person && p.hasKnowledge(Knowledge.LANGUAGE) && !organization.contains(p), limit);
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return publicOrganizations.findOrganization() != null && publicOrganizations.findOrganization().contains(person);
		}
		
		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.LANGUAGE;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "ask other to join organization";
		}
	}
	
	private static class JoinOrganizationPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			Organization targetOrganization = publicOrganizations.findOrganization();
			Person targetPerson = args.getTargetPerson();
			PersonDecisions personDecisions = args.getPersonDecisions();
		
			if (personDecisions.personWantsToJoin(targetOrganization, person, targetPerson)) {
				targetOrganization.add(person);
			}
		}
	
		@Override
		public List<Person> getPossibleTargetPersons(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations, int limit) {
			Organization organization = publicOrganizations.findOrganization();
			return personFinder.findPersons(p -> p != person && p.hasKnowledge(Knowledge.LANGUAGE) && organization.contains(p), limit);
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return publicOrganizations.findOrganization() != null &&  !publicOrganizations.findOrganization().contains(person);
		}
		
		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.LANGUAGE;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "join organization";
		}
	}
	
	private static class StealPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			Person targetPerson = args.getTargetPerson();
			TradeArgs tradeArgs = args.getTradeArgs();
			
			person.stealFrom(targetPerson, tradeArgs, publicOrganizations);
		}
	
		@Override
		public List<Person> getPossibleTargetPersons(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations, int limit) {
			return personFinder.findPersons(p -> p != person, limit);
		}

		@Override
		public boolean requiresTradeArgs() {
			return true;
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return true;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "steal";
		}
	}
	
	private static class MaintainSocialOrderPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			publicOrganizations.maintainSocialOrder();
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return true;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "maintain social order";
		}
	}

	private static class HireSocialOrderMaintainerPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			Person targetPerson = args.getTargetPerson();
			PersonDecisions personDecisions = args.getPersonDecisions();
			if (personDecisions.acceptsJob(targetPerson, person)) {
				Organization organization = publicOrganizations.findOrganization();
				organization.hireSocialOrderMaintainer(targetPerson);
			}
		}

		@Override
		public List<Person> getPossibleTargetPersons(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations, int limit) {
			return personFinder.findPersons(p -> p != person, limit);
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return publicOrganizations.findOrganization() != null && publicOrganizations.findOrganization().personCanDecide(person);
		}
		
		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.DIVISION_OF_LABOR;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "hire social order maintainer";
		}
	}
	
	private static class ChangePolicyPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			OrganizationPolicy organizationPolicy = args.getOrganizationPolicy();
			PersonDecisions personDecisions = args.getPersonDecisions();
			
			Organization organization = publicOrganizations.findOrganization();
			if (organization.approvePolicyChange(person, organizationPolicy, personDecisions)) {
				AvailableOrganizationPolicies availableOrganizationPolicies = new AvailableOrganizationPoliciesImpl(this, publicKnowledge);
				organization.changePolicy(organizationPolicy, availableOrganizationPolicies);
			}
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return publicOrganizations.findOrganization() != null && publicOrganizations.findOrganization().personCanDecide(person);
		}
		
		@Override
		public Knowledge getRequiredKnowledge() {
			return Knowledge.CENTRALIZED_GOVERNMENT;
		}
		
		@Override
		public boolean requiresOrganizationPolicy() {
			return true;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "change policy";
		}
	}
	
	private static class PartnerUpPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			Person newPartner = args.getTargetPerson();
			PersonDecisions personDecisions = args.getPersonDecisions();
			if (newPartner != null && personDecisions.wantsToPartnerUp(newPartner, person)) {
				person.getFamily().setPartner(person, newPartner);
			}
		}

		@Override
		public List<Person> getPossibleTargetPersons(Person person, PersonFinder personFinder, PublicOrganizations publicOrganizations, int limit) {
			return personFinder.findPersons(person::isValidPartner, limit);
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return person.getFamily().getPartner() == null && person.isAdult();
		}

		@Override
		public int getTimeRequired() {
			return 2;
		}

		@Override
		public String getName() {
			return "partner up";
		}
	}
	
	private static class HaveSexPersonAction extends AbstractPersonAction {

		@Override
		public void performInternal(Person person, PersonActionArgs args, PublicAssets publicAssets, PublicKnowledge publicKnowledge, PublicOrganizations publicOrganizations) {
			Person partner = person.getFamily().getPartner();
			PersonDecisions personDecisions = args.getPersonDecisions();
			if (personDecisions.wantsToSex(partner, person)) {
				if (canBecomePregnant(person)) {
					person.impregnate();
				} else if (partner != null && canBecomePregnant(partner)) {
					partner.impregnate();
				}
			}
		}
		
		private boolean canBecomePregnant(Person person) {
			return person.getSex() == Sex.FEMALE && !person.isPregnant();
		}

		@Override
		public boolean canPerformInternal(Person person, PublicAssets publicAssets, PublicOrganizations publicOrganizations) {
			return person.isAdult() && person.getFamily().getPartner() != null;
		}

		@Override
		public int getTimeRequired() {
			return 1;
		}

		@Override
		public String getName() {
			return "have sex";
		}		
	}
}
