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

public class OBEXAPP_Assets_Desc extends OBEXAPP_Assets_Name {

  @Override
  protected void execute(CalloutInfo info) throws ServletException {

    // OBEXAPP_Assets_Name callout is executed
    super.execute(info);

    // Combo example. Removed USD currency from combo and select DEM currency.
    info.addSelect("inpcCurrencyId");
    info.removeSelectResult("100");
    info.addSelectResult("103", "DEM", true);
    info.endSelect();

    // Checks if name field has been updated by parent callout.
    String name = info.getStringParameter("inpname");
    String message = "Feature 'Extends a Callout' works as expected.";
    if (name.endsWith(MODIFIED_FIELD)) {
      info.addResult("inpdescription", message);
      info.addResult("MESSAGE", message);
    } else {
      message = "Feature 'Extends a Callout' not works as expected.";
      info.addResult("inpdescription", message);
      info.addResult("ERROR", message);
    }

    // Now it is possible to update the 'name' field again and the value will be overwritten
    info.addResult("inpname", "UPDATED...");
  }
}
