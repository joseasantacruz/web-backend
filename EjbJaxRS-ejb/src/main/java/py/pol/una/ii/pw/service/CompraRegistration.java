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

import py.pol.una.ii.pw.data.ProductoRepository;
import py.pol.una.ii.pw.data.ProveedorRepository;
import py.pol.una.ii.pw.model.CompraCabecera;
import py.pol.una.ii.pw.model.CompraDetalle;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.util.Compra;
import py.pol.una.ii.pw.util.CompraDet;
import py.pol.una.ii.pw.util.MensajeCompra;
import py.pol.una.ii.pw.util.ServiceException;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import java.util.List;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class CompraRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;
    
    @Inject
    private ProveedorRepository repositoryProveedor;
    
    @Inject
    private ProductoRepository repositoryProducto;
    
    @Inject
    private ProductoRegistration registrationProducto;
    
    @Resource
	SessionContext ctx;

    @Inject
    private Event<CompraCabecera> compraCabeceraEventSrc;
    
    @Inject
    private Event<CompraDetalle> compraDetalleEventSrc;
    
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<MensajeCompra> registerCompra(Compra compra) throws ServiceException {
    	
        List<MensajeCompra> mensajes = null;
    	try {

			Proveedor proveedor = repositoryProveedor.findById(compra.getProveedor());
			CompraCabecera compraCabecera = new CompraCabecera();
			compraCabecera.setFecha(compra.getFecha());
			compraCabecera.setMonto(compra.getMonto());
			compraCabecera.setProveedor(proveedor);
			
			registerCompraCabecera(compraCabecera);
			
			for (CompraDet compraDet: compra.getCompraDetalle()){
				Producto producto = repositoryProducto.findById(compraDet.getProducto());
				CompraDetalle compraDetalle = new CompraDetalle();
				compraDetalle.setProducto(producto);
				compraDetalle.setCantidad(compraDet.getCantidad());
				compraDetalle.setCompraCabecera(compraCabecera);
				registerCompraDetalle(compraDetalle);
				Float suma = producto.getCantidad() + compraDetalle.getCantidad();
				producto.setCantidad(suma);
				registrationProducto.alter(producto);
			}
		}
		catch(Exception e){
			MensajeCompra mensaje = new MensajeCompra();
			mensaje.setMensaje(e.getMessage());
			mensaje.setCompra(compra);
			mensajes.add(mensaje);

			ctx.setRollbackOnly();
		}
    		
    	return mensajes;
    }
    
    public void registerCompraCabecera(CompraCabecera compraCabecera) throws ServiceException {
    	try{
    		log.info("Registering COMPRA Cabecera --- " + compraCabecera.getProveedor().getNombre());
            em.persist(compraCabecera);
            compraCabeceraEventSrc.fire(compraCabecera);
    	}catch (Exception e){
    		ctx.setRollbackOnly();
			new ServiceException(e);
    	}
    }
    
    public void registerCompraDetalle(CompraDetalle compraDetalle) throws ServiceException {
    	try{
    		log.info("Registering COMPRA Detalle --- " + compraDetalle.getProducto().getNombre());
            em.persist(compraDetalle);
            compraDetalleEventSrc.fire(compraDetalle);
    	}catch (Exception e){
    		ctx.setRollbackOnly();
			new ServiceException(e);
    	}
    }
}
