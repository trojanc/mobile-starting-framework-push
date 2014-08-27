/**
 * The MIT License
 * Copyright (c) 2011 Kuali Mobility Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */


package org.kuali.mobility.push.dao;

import org.apache.log4j.Logger;
import org.kuali.mobility.push.service.PushDeviceTupleService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

/**
 * PurgePushNotificationDaoImpl class is implementation of PurgePushNotificationDao interface and it provides a
 * method namely purgePushNotification to purge push notification data from the database based on the status provided.
 *
 * @author Kuali Mobility Team (mobility.collab@kuali.org)
 */
public class PurgePushNotificationDaoImpl implements  PurgePushNotificationDao {

    /** A reference to a logger */
    private static final Logger LOG = Logger.getLogger(PurgePushNotificationDaoImpl.class);

    /** A reference to the <code>EntityManger</code> */
    @PersistenceContext
    private EntityManager entityManager;

    /** A reference to the <code>PushDeviceTupleService</code> */
    @Autowired
    private PushDeviceTupleService pdtService;


    /**
     * Creates a new instance of a <code>PurgePushNotificationDaoImpl</code>
     */
    public PurgePushNotificationDaoImpl(){}

    public boolean purgePushNotification(HttpServletRequest request, int status){
        boolean success = false;
       /* User user = (User) request.getSession().getAttribute(Constants.KME_USER_KEY);

        // Only if the user belongs to group KME-ADMINISTRATOR can the user delete pushes
        if(new GroupExpression("KME-ADMINISTRATOR").evaluate(user)) {
            entityManager.createNamedQuery("PushDeviceTuple.deleteForStatus").setParameter("status", status).executeUpdate();
            success = true;
        }*/
        return success;
    }

    /**
     * Return the <code>EntityManager</code> for this DaoImpl..
     * @return
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Set the <code>EntityMAnager</code> for this DaoImpl.
     * @param entityManager
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}
