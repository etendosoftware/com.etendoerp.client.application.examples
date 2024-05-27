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
 * All portions are Copyright (C) 2016 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */
package com.etendoerp.client.application.examples.callouts;

import javax.servlet.ServletException;

import org.openbravo.erpCommon.ad_callouts.SimpleCallout;

public class OBEXAPP_Assets_Name extends SimpleCallout {

  protected static final String MODIFIED_FIELD = "_UPDATED";

  @Override
  protected void execute(CalloutInfo info) throws ServletException {

    // get value of field name and update value
    final String name = info.getStringParameter("inpname");
    info.addResult("inpname", name + MODIFIED_FIELD);

    // Combo example. Added three currencies to currency combo.
    info.addSelect("inpcCurrencyId");
    // USD currency is selected.
    info.addSelectResult("100", "USD", true);
    info.addSelectResult("102", "EUR", false);
    info.addSelectResult("103", "DEM", false);
    info.endSelect();
  }

}
