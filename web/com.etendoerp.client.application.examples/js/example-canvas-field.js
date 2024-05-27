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
 * All portions are Copyright (C) 2011-2013 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */

// This javascript defines a grid and a layout containing the grid
// together with a button to show the selected records
// This testgrid shows data from the product table
isc.defineClass('OBEXAPP_SalesOrderActionButton', isc.OBGridFormButton);

isc.OBEXAPP_SalesOrderActionButton.addProperties({
  noTitle: true,
  title: OB.I18N.getLabel('OBUISC_Identifier'),
  click: function() {
    var info = '';
    if (this.record) {
      info = this.record[OB.Constants.IDENTIFIER];
    } else if (this.canvasItem) {
      info = this.canvasItem.form.getValue(OB.Constants.IDENTIFIER);
    }
    isc.say(info);
  }
});

isc.defineClass('OBEXAPP_SalesOrderCalculated', isc.OBGridFormLabel);

isc.OBEXAPP_SalesOrderCalculated.addProperties({
  wrap: false,
  height: 1,
  width: 1,
  overflow: 'visible',
  contents: '',
  initWidget: function() {
    if (this.canvasItem) {
      this.computeContents(
        this.canvasItem.form.getValue('grandTotalAmount'),
        this.canvasItem.form.getValue('summedLineAmount')
      );
    }

    this.Super('initWidget', arguments);
  },

  showValue: function(displayValue, dataValue, form, item) {
    this.computeContents(
      form.getValue('grandTotalAmount'),
      form.getValue('summedLineAmount')
    );
  },

  // is called when the form gets redrawn
  redrawingItem: function() {
    this.computeContents(
      this.canvasItem.form.getValue('grandTotalAmount'),
      this.canvasItem.form.getValue('summedLineAmount')
    );
  },

  // is called when a field on the form changes its value
  onItemChanged: function() {
    this.computeContents(
      this.canvasItem.form.getValue('grandTotalAmount'),
      this.canvasItem.form.getValue('summedLineAmount')
    );
  },

  // is called in grid-display mode when the canvas is created/used
  // for a record
  setRecord: function(record) {
    this.computeContents(record.grandTotalAmount, record.summedLineAmount);
  },

  computeContents: function(val1, val2) {
    var num, numStr;
    if (!val2) {
      this.setContents('');
    } else {
      num = OB.Utilities.Number.JSToOBMasked(
        val1 / val2,
        OB.Format.defaultNumericMask,
        OB.Format.defaultDecimalSymbol,
        OB.Format.defaultGroupingSymbol,
        OB.Format.defaultGroupingSize
      );
      numStr = '' + num;

      // set contents will redraw, resulting in computeContents to be called again
      // prevent this looping
      if (this.contents !== numStr) {
        this.setContents(num);
      }
    }
  }
});
