/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package py.pol.una.ii.pw.service;

import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.util.ServiceException;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ClienteRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;
    
    @Resource
	SessionContext ctx;

    @Inject
    private Event<Cliente> clienteEventSrc;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void register(Cliente cliente) throws ServiceException {
    	try{
    		log.info("Registering " + cliente.getNombre());
            em.persist(cliente);
            clienteEventSrc.fire(cliente);
    	}catch (Exception e){
    		ctx.setRollbackOnly();
			new ServiceException(e);
    	}
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void alter(Cliente cliente) throws ServiceException {
    	try{
    		log.info("Altering " + cliente.getNombre());
            em.merge(cliente);
            clienteEventSrc.fire(cliente);
    	}catch (Exception e){
    		ctx.setRollbackOnly();
			new ServiceException(e);
    	}
    }
    
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void remove(Cliente cliente) throws ServiceException {
    	try{
    		log.info("Removing " + cliente.getNombre());
            em.remove(em.contains(cliente) ? cliente : em.merge(cliente));
            clienteEventSrc.fire(cliente);
    	}catch (Exception e){
    		ctx.setRollbackOnly();
			new ServiceException(e);
    	}
    }
}
