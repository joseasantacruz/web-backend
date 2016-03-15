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
package py.pol.una.ii.pw.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
//import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import py.pol.una.ii.pw.model.CompraCabecera;

@ApplicationScoped
public class CompraRepository {

    @Inject
    private EntityManager em;

    public CompraCabecera findById(Integer id) {
        return em.find(CompraCabecera.class, id);
    }

    //@SuppressWarnings("unchecked")
    public List<CompraCabecera> findAllOrderedById() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CompraCabecera> criteria = cb.createQuery(CompraCabecera.class);
        Root<CompraCabecera> compra = criteria.from(CompraCabecera.class);
        criteria.select(compra);
        //criteria.select(compra).orderBy(cb.asc(compra.get("id_compraCabecera")));
        
        /*String sql = "SELECT CLIENTE.* FROM CLIENTE";
        
        Query query = em.createNativeQuery(sql, Cliente.class);
       
		List<Cliente> clientes = (List<Cliente>) query.getResultList();
        System.out.println(clientes.size());
        return clientes;*/
       return em.createQuery(criteria).getResultList();
    }
}
