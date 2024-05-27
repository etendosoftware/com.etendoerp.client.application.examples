/*
 *************************************************************************
 * The contents of this file are subject to the Openbravo  Public  License
 * Version  1.1  (the  "License"),  being   the  Mozilla   Public  License
 * Version 1.1  with a permitted attribution clause; you may not  use this
 * file except in compliance with the License. You  may  obtain  a copy of
 * the License at http://www.openbravo.com/legal/license.html
 * Software distributed under the License  is  distribfuted  on  an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific  language  governing  rights  and  limitations
 * under the License.
 * The Original Code is Openbravo ERP.
 * The Initial Developer of the Original Code is Openbravo SLU
 * All portions are Copyright (C) 2011-2013 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */

/*global console*/

OB.OBEXAPP.PNE = {};

OB.OBEXAPP.PNE.validate = function(item, validator, value, record) {
  // item has access to grid: item.grid
  // from the grid you can get all selected records and edited values, e.g.
  //   * item.grid.getSelection()
  //   * item.grid.getEditedRecord()
  // grid has access to view: grid.view
  // view has access to parentWindow: view.parentWindow (the window running the process)
  // parentWindow has access to currentView
  // currentView has getContextInfo
  // debugger;
  if (window.console) {
    console.log('validation function!', value); //eslint-disable-line
  }
  return true;
};

OB.OBEXAPP.PNE.selectionChanged = function(grid, record, recordList) {
  if (window.console) {
    console.log('selection function!'); //eslint-disable-line
    console.log(grid, record, recordList); //eslint-disable-line
  }
};

OB.OBEXAPP.PNE.addNew = function(grid) {
  // grid.view.parentWindow.activeView.viewForm
  // grid.view.parentWindow.activeView.viewGrid
  // grid.view.parentWindow.activeView.getContextInfo()
  if (window.console) {
    console.log('addNew should return an object with the initial values'); //eslint-disable-line
  }

  return {
    businessPartner: 'Business Partner 1',
    // tricky one
    invoiceDate: new Date(1979, 4, 24),
    chargeAmount: 0,
    documentNo: 'N/A'
  };
};

OB.OBEXAPP.PNE.onRemove = function(grid, rowNum, record) {
  // If this function returns false, the row is not removed
  if (window.console) {
    console.log('onRemove called!'); //eslint-disable-line
  }
  return true;
};
