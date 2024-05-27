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
 * All portions are Copyright (C) 2011 Openbravo SLU
 * All Rights Reserved.
 * Contributor(s):  ______________________________________.
 ************************************************************************
 */
package com.etendoerp.client.application.examples;

import java.math.BigDecimal;
import java.util.Calendar;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.openbravo.dal.service.OBCriteria;
import org.openbravo.dal.service.OBDal;
import org.openbravo.model.common.order.OrderLine;
import org.openbravo.model.common.plm.Product;
import org.openbravo.scheduling.ProcessBundle;
import org.openbravo.scheduling.ProcessLogger;
import org.openbravo.service.db.DalBaseProcess;
import org.quartz.JobExecutionException;

/**
 * Shows an example of a background process implemented in java. The background process needs to
 * extend DalBaseProcess since we will be using DAL objects to perform database operations
 * 
 * This class is used as an example in howtos in the Openbravo Developers Guide:
 * http://wiki.openbravo.com/wiki/Category:Developers_Guide
 * 
 * @author mtaal
 */
public class ProductRevenueCalculation extends DalBaseProcess {

  static int counter = 0;

  private ProcessLogger logger;

  // abstract method doExecute needs to be implemented and carries
  // with itself the ProcessBundle object deriving from Openbravo Quartz
  // scheduler
  @Override
  public void doExecute(ProcessBundle bundle) throws Exception {

    logger = bundle.getLogger(); // this logger logs into the LOG column of
    // the AD_PROCESS_RUN database table
    BigDecimal sumAmount = new BigDecimal(0);

    logger.log("Starting background product revenue calculation. Loop " + counter + "\n");

    // define time 6 months ago from today which is the timespan that our
    // calculation will consider
    Calendar timeSixMonthsAgo = Calendar.getInstance();
    timeSixMonthsAgo.add(Calendar.DAY_OF_MONTH, -180);

    try {
      // get all products that are sold (M_PRODUCT.ISSOLD = 'Y')
      final OBCriteria<Product> productList = OBDal.getInstance().createCriteria(Product.class);
      productList.add(Restrictions.eq(Product.PROPERTY_SALE, true));

      logger.log("No of products = " + productList.list().size() + "\n");

      // loop through all products that are sold and calculate revenues
      // for each
      for (Product product : productList.list()) {

        sumAmount = new BigDecimal(0);

        // select lines from C_ORDERLINE table that match the product
        final Criteria orderLineList = OBDal.getInstance()
            .createCriteria(OrderLine.class)
            .add(Restrictions.eq(OrderLine.PROPERTY_PRODUCT, product));

        // filter out lines that belong to sales (as opposed to
        // purchase) and fit within the last six months
        //
        // when you want to filter on a property of an associated entity
        // then the property of that association needs an alias, see
        // here: http://www.javalobby.org/articles/hibernatequery102/
        orderLineList.createAlias(OrderLine.PROPERTY_SALESORDER, "order")
            .add(Restrictions.eq("order.salesTransaction", true))
            .add(Restrictions.gt("order.orderDate", timeSixMonthsAgo.getTime()));

        // Sum line amounts grouped by product
        orderLineList.setProjection(Projections.projectionList()
            .add(Projections.sum(OrderLine.PROPERTY_LINENETAMOUNT))
            .add(Projections.groupProperty(OrderLine.PROPERTY_PRODUCT)));

        // due to grouping and sum operation there will really only be
        // one resulting record but in theory there could be more (a
        // list)
        for (Object o : orderLineList.list()) {
          // the query returns a list of arrays (columns of the query)
          final Object[] os = (Object[]) o;
          sumAmount = (BigDecimal) os[0];
          final Product p = (Product) os[1];
          logger.log(p.getName() + " Amount " + sumAmount + "\n");
        }
        product.setDescription("6 monthRevenue = " + sumAmount);
      }

    } catch (Exception e) {
      // catch any possible exception and throw it as a Quartz
      // JobExecutionException
      throw new JobExecutionException(e.getMessage(), e);
    }
  }
}
