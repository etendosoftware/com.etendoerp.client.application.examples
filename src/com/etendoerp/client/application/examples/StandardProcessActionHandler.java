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
 * All portions are Copyright (C) 2013 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */
package com.etendoerp.client.application.examples;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openbravo.client.application.process.BaseProcessActionHandler;
import org.openbravo.dal.service.OBDal;
import org.openbravo.erpCommon.utility.OBMessageUtils;
import org.openbravo.model.common.order.Order;

public class StandardProcessActionHandler extends BaseProcessActionHandler {
  private static final Logger log = Logger.getLogger(StandardProcessActionHandler.class);

  @Override
  protected JSONObject doExecute(Map<String, Object> parameters, String content) {
    try {
      JSONObject result = new JSONObject();

      JSONObject request = new JSONObject(content);
      JSONObject params = request.getJSONObject("_params");

      // Do validations on param values
      double min = params.getDouble("min");
      double max = params.getDouble("max");

      if (max < min) {
        // In case validations are not satisfied, show an error message and allow user to fix
        // parameters
        result.put("retryExecution", true);

        JSONObject msg = new JSONObject();
        msg.put("severity", "error");
        msg.put("text", OBMessageUtils.getI18NMessage("OBEXAPP_MinGtMax",
            new String[] { Double.toString(min), Double.toString(max) }));
        result.put("message", msg);
        return result;
      }

      // Execute process and prepare an array with actions to be executed after execution
      JSONArray actions = new JSONArray();

      // 1. Sum amounts of all orders and show a message in process view
      JSONArray orders = params.getJSONArray("orders");
      BigDecimal totalAmnt = BigDecimal.ZERO;
      for (int i = 0; i < orders.length(); i++) {
        String orderId = orders.getString(i);
        Order order = OBDal.getInstance().get(Order.class, orderId);
        totalAmnt = totalAmnt.add(order.getGrandTotalAmount());
      }
      JSONObject msgTotal = new JSONObject();
      msgTotal.put("msgType", "info");
      // XXX: these two messages should be translatable, like OBEXAPP_MinGtMax above
      msgTotal.put("msgTitle", "Selected Orders");
      msgTotal.put("msgText", "Total amount: " + totalAmnt.toString());

      JSONObject msgTotalAction = new JSONObject();
      msgTotalAction.put("showMsgInProcessView", msgTotal);

      actions.put(msgTotalAction);

      // 2. If business partner is not null, open it in BP window and show a message in new tab
      if (!params.isNull("bp")) {
        String bpId = params.getString("bp");
        JSONObject recordInfo = new JSONObject();
        recordInfo.put("tabId", "220");
        recordInfo.put("recordId", bpId);
        recordInfo.put("wait", true);

        JSONObject openTabAction = new JSONObject();
        openTabAction.put("openDirectTab", recordInfo);

        actions.put(openTabAction);

        JSONObject msgInBPTab = new JSONObject();
        msgInBPTab.put("msgType", "success");
        msgInBPTab.put("msgTitle", "Process execution");
        msgInBPTab.put("msgText", "This record was opened from process execution");

        JSONObject msgInBPTabAction = new JSONObject();
        msgInBPTabAction.put("showMsgInView", msgInBPTab);

        actions.put(msgInBPTabAction);
      }

      result.put("responseActions", actions);

      return result;
    } catch (JSONException e) {
      log.error("Error in process", e);
      return new JSONObject();
    }
  }
}
