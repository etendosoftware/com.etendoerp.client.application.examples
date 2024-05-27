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
 * All portions are Copyright (C) 2012-2015 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */

OB.OBEXAPP = OB.OBEXAPP || {};

OB.OBEXAPP.Process = {
  execute: function(params, view) {
    var i,
      selection = params.button.contextView.viewGrid.getSelectedRecords(),
      orders = [],
      callback;

    callback = function(rpcResponse, data, rpcRequest) {
      // show result
      isc.say(OB.I18N.getLabel('Obexapp_Updated', [data.updated]));

      // refresh the whole grid after executing the process
      params.button.contextView.viewGrid.refreshGrid();
    };

    for (i = 0; i < selection.length; i++) {
      orders.push(selection[i][OB.Constants.ID]);
    }

    OB.RemoteCallManager.call(
      'com.etendoerp.client.application.example.ManualProcessActionHandler',
      {
        orders: orders,
        action: params.action
      },
      {},
      callback
    );
  },

  sum: function(params, view) {
    params.action = 'sum';
    OB.OBEXAPP.Process.execute(params, view);
  },

  subtract: function(params, view) {
    params.action = 'subtract';
    OB.OBEXAPP.Process.execute(params, view);
  }
};
