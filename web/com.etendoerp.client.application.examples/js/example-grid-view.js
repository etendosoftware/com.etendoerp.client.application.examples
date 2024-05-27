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
 * All portions are Copyright (C) 2011-2012 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */

// This javascript defines a grid and a layout containing the grid
// together with a button to show the selected records
// This testgrid shows data from the product table
isc.defineClass('OBEXAPP_TestGrid', isc.OBGrid);

isc.OBEXAPP_TestGrid.addProperties({
  dataSource: null,
  showFilterEditor: true,

  dataProperties: {
    useClientFiltering: false
  },

  gridFields: [
    {
      name: 'name',
      // allow filtering on name
      canFilter: true,
      // filter automatically when the user types
      filterOnKeypress: true
    },

    {
      // description is a property of the product
      name: 'description',
      // allow filtering on description
      canFilter: true,
      // filter automatically when the user types
      filterOnKeypress: true
    }
  ],

  setDataSource: function(ds) {
    // is called when the datasource is retrieved from the server
    this.Super('setDataSource', [ds, this.gridFields]);
    this.refreshFields();
    this.sort('name');
    this.fetchData();
  },

  initWidget: function() {
    // get the datasource, if it is not yet defined
    // it is retrieved from the server and returned
    // Datasources refer to tables using the entity name
    OB.Datasource.get('Product', this, null, true);
    this.Super('initWidget', arguments);
  }
});

isc.defineClass('OBEXAPP_SimpleView', isc.VLayout);

isc.OBEXAPP_SimpleView.addProperties({
  // do some margins between the members
  membersMargin: 10,
  defaultLayoutAlign: 'center',

  initWidget: function() {
    // create a button which is enabled
    var grid, btn;

    // create an instance of the grid
    grid = isc.OBEXAPP_TestGrid.create({
      // add logic to enable/disable the button when
      // the selected records changes
      selectionUpdated: function(record, recordList) {
        if (recordList && recordList.length > 0) {
          btn.setDisabled(false);
        } else {
          btn.setDisabled(true);
        }
      }
    });

    // and create a button which refers to the grid
    btn = isc.OBFormButton.create({
      title: OB.I18N.getLabel('OBEXAPP_ClickMe'),
      // let it be enabled when more than one
      disabled: true,
      action: function() {
        // show the number of selected records
        // illustrates the usage of a parameterized message
        isc.say(
          OB.I18N.getLabel('OBEXAPP_SelectedRecordsMsg', [
            grid.getSelectedRecords().length
          ])
        );
      }
    });

    // add the grid
    this.addMember(btn);
    this.addMember(grid);
    this.Super('initWidget', arguments);
  },

  // the following three methods are related to the view handling
  // api of Openbravo
  isSameTab: function(viewId, params) {
    // return false, allows this view to be opened many times
    return false;
  },

  // just return the classname and nothing else to be bookmarked
  getBookMarkParams: function() {
    var result = {};
    result.viewId = this.getClassName();
    return result;
  },

  // this view does not have a help view
  getHelpView: function() {
    return;
  }
});
