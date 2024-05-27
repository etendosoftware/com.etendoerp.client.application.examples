/*
 *************************************************************************
 * The contents of this file are subject to the Openbravo  Public  License
 * Version  1.1  (the  "License"),  being   the  Mozilla   Public  License
 * Version 1.1  with a permitted attribution clause; you may not  use this
 * file except in compliance with the License. You  may  obtain  a copy of
 * the License at http://www.openbravo.com/legal/license.html
 * Software distributed under the License  is  distributed  on  an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific  language  governing  rights  and  limitations
 * under the License.
 * The Original Code is Openbravo ERP.
 * The Initial Developer of the Original Code is Openbravo SLU
 * All portions are Copyright (C) 2010 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */
package com.etendoerp.client.application.examples;

import org.openbravo.dal.service.OBDal;
import org.openbravo.erpCommon.utility.OBError;
import org.openbravo.model.common.businesspartner.BusinessPartner;
import org.openbravo.model.common.enterprise.Organization;
import org.openbravo.scheduling.ProcessBundle;
import org.openbravo.service.db.DalBaseProcess;

/**
 * Provides an example of a java process which is called from the ui directly.
 * 
 * This class is used as an example in howtos in the Openbravo Developers Guide:
 * http://wiki.openbravo.com/wiki/Category:Developers_Guide
 * 
 * @author mtaal
 */
public class ExampleJavaProcess extends DalBaseProcess {

  @Override
  public void doExecute(ProcessBundle bundle) throws Exception {
    try {

      // retrieve the parameters from the bundle
      final String bPartnerId = (String) bundle.getParams().get("cBpartnerId");
      final String organizationId = (String) bundle.getParams().get("adOrgId");
      final String myString = (String) bundle.getParams().get("mystring");

      // implement your process here

      // Show a result
      final StringBuilder sb = new StringBuilder();
      sb.append("Read information:<br/>");
      if (bPartnerId != null) {
        final BusinessPartner bPartner = OBDal.getInstance().get(BusinessPartner.class, bPartnerId);
        sb.append("Business Partner: " + bPartner.getIdentifier() + "<br/>");
      }
      if (organizationId != null) {
        final Organization organization = OBDal.getInstance()
            .get(Organization.class, organizationId);
        sb.append("Organization: " + organization.getIdentifier() + "<br/>");
      }
      sb.append("MyString: " + myString + "<br/>");

      // OBError is also used for successful results
      final OBError msg = new OBError();
      msg.setType("Success");
      msg.setTitle("Read parameters!");
      msg.setMessage(sb.toString());

      bundle.setResult(msg);

    } catch (final Exception e) {
      e.printStackTrace(System.err);
      final OBError msg = new OBError();
      msg.setType("Error");
      msg.setMessage(e.getMessage());
      msg.setTitle("Error occurred");
      bundle.setResult(msg);
    }
  }
}
