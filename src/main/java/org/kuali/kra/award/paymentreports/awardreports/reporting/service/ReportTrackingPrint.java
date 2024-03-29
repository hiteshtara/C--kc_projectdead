/*
 * Copyright 2005-2013 The Kuali Foundation
 * 
 * Licensed under the Educational Community License, Version 2.0 (the "License");
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
package org.kuali.kra.award.paymentreports.awardreports.reporting.service;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Source;

import org.kuali.kra.award.paymentreports.awardreports.reporting.service.ReportTrackingType;
import org.kuali.kra.coi.print.CoiDisclosureType;
import org.kuali.kra.printing.print.AbstractPrint;
import org.kuali.kra.printing.util.PrintingUtils;
import org.kuali.rice.core.api.config.property.ConfigurationService;

/**
 * This class provides the implementation for printing Award  Report Tracking. It
 * generates XML that conforms with delta Report XSD, fetches XSL style-sheets
 * applicable to this XML, returns XML and XSL for any consumer that would use
 * this XML and XSls for any purpose like report generation, PDF streaming etc.
 * 
 * @author
 * 
 */
public class ReportTrackingPrint extends AwardReportTracking {

      private ConfigurationService configurationService;
      ArrayList<Source> sourceList;
       public void setConfigurationService(ConfigurationService configurationService) {
               this.configurationService = configurationService;
           }
    
    /**
     * This method fetches the XSL style-sheets required for transforming the
     * generated XML into PDF.
     * 
     * @return {@link ArrayList}} of {@link Source} XSLs
     */
    public List<Source> getXSLTemplates() {
        sourceList = PrintingUtils
                .getXSLTforReport(ReportTrackingType.AWARD_REPORT_TRACKING
                        .getReportTrackingType());
        return sourceList;
    }
    
   
    }
