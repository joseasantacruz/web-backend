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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import py.pol.una.ii.pw.model.Producto;

@ApplicationScoped
public class ProductoRepository {

    @Inject
    private EntityManager em;

    public Producto findById(Integer id) {
        return em.find(Producto.class, id);
    }
    
    public List<Producto> findAllOrderedByName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Producto> criteria = cb.createQuery(Producto.class);
        Root<Producto> producto = criteria.from(Producto.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        criteria.select(producto).orderBy(cb.asc(producto.get("nombre")));
        return em.createQuery(criteria).getResultList();
    }
    
    public List<Producto> getAll(Integer desde, Integer cantidad) {
    	
    	return em.createQuery("SELECT p FROM Producto p", Producto.class).setFirstResult(desde).setMaxResults(cantidad).getResultList();
    }
}
