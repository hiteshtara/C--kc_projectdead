/*
 * Copyright 2005-2013 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.kra.s2s.generator.impl;

import gov.grants.apply.forms.performanceSite13V13.PerformanceSite13Document;
import gov.grants.apply.forms.performanceSite13V13.SiteLocationDataType;
import gov.grants.apply.forms.performanceSite13V13.PerformanceSite13Document.PerformanceSite13;
import gov.grants.apply.system.attachmentsV10.AttachedFileDataType;
import gov.grants.apply.system.globalLibraryV20.YesNoDataType;

import java.util.List;

import org.apache.xmlbeans.XmlObject;
import org.kuali.kra.bo.Organization;
import org.kuali.kra.bo.OrganizationYnq;
import org.kuali.kra.bo.Rolodex;
import org.kuali.kra.proposaldevelopment.bo.CongressionalDistrict;
import org.kuali.kra.proposaldevelopment.bo.ProposalSite;
import org.kuali.kra.proposaldevelopment.document.ProposalDevelopmentDocument;
import org.kuali.kra.s2s.generator.S2SBaseFormGenerator;
import org.kuali.kra.s2s.util.S2SConstants;

/**
 * 
 * This class is used to generate XML Document object for grants.gov
 * PerformanceSiteV1.2. This form is generated using XMLBean API's generated by
 * compiling PerformanceSiteV1.2 schema.
 * 
 * @author Kuali Research Administration Team (kualidev@oncourse.iu.edu)
 */
public class PerformanceSiteV1_3Generator extends S2SBaseFormGenerator {

	private static final String QUESTION_ID_FOR_INDIVIDUAL_YNQ = "30";
    private static final int PERFORMING_ORG_LOCATION_TYPE_CODE = 2;
    private static final int OTHER_ORG_LOCATION_TYPE_CODE = 3;
    private static final int PERFORMANCE_SITE_LOCATION_TYPE_CODE = 4;

	private XmlObject getPerformanceSite() {
		PerformanceSite13Document performanceSite13Document = PerformanceSite13Document.Factory
				.newInstance();
		PerformanceSite13 performanceSite13 = PerformanceSite13.Factory
				.newInstance();
		performanceSite13.setFormVersion(S2SConstants.FORMVERSION_1_3);
		setOtherSites(performanceSite13);
		AttachedFileDataType attachedFile = getAttachment();
		if(attachedFile!=null){
		    performanceSite13.setAttachedFile(attachedFile);
	    }
		performanceSite13Document.setPerformanceSite13(performanceSite13);
		return performanceSite13Document;
	}


	private void setSiteLocationDataType(
			SiteLocationDataType siteLocationDataType, Organization organization) {
		if (organization.getOrganizationYnqs() != null
				&& !organization.getOrganizationYnqs().isEmpty()) {
			for (OrganizationYnq organizationYnq : organization
					.getOrganizationYnqs()) {
				if (organizationYnq.getQuestionId().equals(
						QUESTION_ID_FOR_INDIVIDUAL_YNQ)) {
					YesNoDataType.Enum answer = organizationYnq.getAnswer()
							.equals("Y") ? YesNoDataType.Y_YES
							: YesNoDataType.N_NO;
					siteLocationDataType.setIndividual(answer);
				}
			}
		}
	}

    private void setOtherSites(PerformanceSite13  performanceSite) {
        List<ProposalSite> proposalSites = pdDoc.getDevelopmentProposal().getProposalSites();
        if (proposalSites != null) {
            Organization organization = null;
            Rolodex rolodex = null;
            SiteLocationDataType siteLocationDataType = null;
            for (ProposalSite proposalSite : proposalSites) {
                switch(proposalSite.getLocationTypeCode()){
                    case(PERFORMING_ORG_LOCATION_TYPE_CODE):
                        siteLocationDataType = performanceSite.addNewPrimarySite();
                        organization = proposalSite.getOrganization();
                        setSiteLocationDataType(siteLocationDataType, organization);
                        rolodex = organization.getRolodex();
                        break;
                    case(OTHER_ORG_LOCATION_TYPE_CODE):
                        organization = proposalSite.getOrganization();
                        rolodex = organization.getRolodex();
                        siteLocationDataType = performanceSite.addNewOtherSite();
                        break;
                    case(PERFORMANCE_SITE_LOCATION_TYPE_CODE):
                        rolodex = proposalSite.getRolodex();
                        siteLocationDataType = performanceSite.addNewOtherSite();
                        break;
                }
                if(siteLocationDataType!=null){
                    siteLocationDataType.setOrganizationName(proposalSite.getLocationName());
                    siteLocationDataType.setAddress(globLibV20Generator.getAddressDataType(rolodex));
                    if (organization!=null && organization.getDunsNumber() != null) {
                        siteLocationDataType.setDUNSNumber(organization.getDunsNumber());
                    }
                    String congressionalDistrict = getCongressionalDistrict(proposalSite);
                    if (congressionalDistrict != null) {
                        siteLocationDataType.setCongressionalDistrictProgramProject(congressionalDistrict);
                    }
                }
            }
        }
    }

    private String getCongressionalDistrict(ProposalSite proposalSite) {
        String congDistrictProject = null;
        for (CongressionalDistrict congDistrict : proposalSite.getCongressionalDistricts()) {
            congDistrictProject = congDistrict.getCongressionalDistrict();
            if (congDistrictProject != null && congDistrictProject.length() > CONGRESSIONAL_DISTRICT_MAX_LENGTH) {
                congDistrictProject = congDistrictProject.substring(0, CONGRESSIONAL_DISTRICT_MAX_LENGTH);
            }
        }
        return congDistrictProject;
    }

	protected AttachedFileDataType getAttachment(){
	    return getAttachedFileDataType("40");
	}
	/**
	 * This method creates {@link XmlObject} of type
	 * {@link PerformanceSite13Document} by populating data from the given
	 * {@link ProposalDevelopmentDocument}
	 * 
	 * @param proposalDevelopmentDocument
	 *            for which the {@link XmlObject} needs to be created
	 * @return {@link XmlObject} which is generated using the given
	 *         {@link ProposalDevelopmentDocument}
	 * @see org.kuali.kra.s2s.generator.S2SFormGenerator#getFormObject(ProposalDevelopmentDocument)
	 */
	public XmlObject getFormObject(
			ProposalDevelopmentDocument proposalDevelopmentDocument) {
		this.pdDoc = proposalDevelopmentDocument;
		return getPerformanceSite();
	}

	/**
	 * This method typecasts the given {@link XmlObject} to the required
	 * generator type and returns back the document of that generator type.
	 * 
	 * @param xmlObject
	 *            which needs to be converted to the document type of the
	 *            required generator
	 * @return {@link XmlObject} document of the required generator type
	 * @see org.kuali.kra.s2s.generator.S2SFormGenerator#getFormObject(XmlObject)
	 */
	public XmlObject getFormObject(XmlObject xmlObject) {
		PerformanceSite13Document performanceSite13Document = PerformanceSite13Document.Factory
				.newInstance();
		PerformanceSite13 performanceSite13 = (PerformanceSite13) xmlObject;
		performanceSite13Document.setPerformanceSite13(performanceSite13);
		return performanceSite13Document;
	}
}