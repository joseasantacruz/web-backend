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
import py.pol.una.ii.pw.data.ClienteRepository;
import py.pol.una.ii.pw.model.VentaCabecera;
import py.pol.una.ii.pw.model.VentaDetalle;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.util.Venta;
import py.pol.una.ii.pw.util.VentaDet;
import py.pol.una.ii.pw.util.MensajeVenta;
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
public class VentaRegistration {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;
    
    @Inject
    private ClienteRepository repositoryCliente;
    
    @Inject
    private ClienteRegistration registrationCliente;
    
    @Inject
    private ProductoRepository repositoryProducto;
    
    @Inject
    private ProductoRegistration registrationProducto;
    
    @Resource
	SessionContext ctx;

    @Inject
    private Event<VentaCabecera> ventaCabeceraEventSrc;
    
    @Inject
    private Event<VentaDetalle> ventaDetalleEventSrc;
    
	@SuppressWarnings("null")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<MensajeVenta> registerVenta(Venta venta) throws ServiceException {
    	
        List<MensajeVenta> mensajes = null;
    	try {

			Cliente cliente = repositoryCliente.findById(venta.getCliente());
			VentaCabecera ventaCabecera = new VentaCabecera();
			ventaCabecera.setCliente(cliente);
			ventaCabecera.setFecha(venta.getFecha());
			ventaCabecera.setMonto(venta.getMonto());
			registerVentaCabecera(ventaCabecera);
			Float saldo = cliente.getSaldo() + venta.getMonto();
			cliente.setSaldo(saldo);
			registrationCliente.alter(cliente);
			
			for (VentaDet ventaDet: venta.getVentaDetalle()){
				Producto producto = repositoryProducto.findById(ventaDet.getProducto());
				VentaDetalle ventaDetalle = new VentaDetalle();
				if( producto.getCantidad() >= ventaDet.getCantidad()){
					ventaDetalle.setProducto(producto);
					ventaDetalle.setCantidad(ventaDet.getCantidad());
					ventaDetalle.setMonto_parcial(producto.getPrecio_venta() * ventaDet.getCantidad());
					ventaDetalle.setVentaCabecera(ventaCabecera);
					//ventaDetalleList.add(ventaDetalle);
					registerVentaDetalle(ventaDetalle);
					Float diferencia = producto.getCantidad() - ventaDet.getCantidad();
					producto.setCantidad(diferencia);
					registrationProducto.alter(producto);
				}else{
					MensajeVenta mensaje = new MensajeVenta();
					mensaje.setMensaje("Producto Codigo:" + producto.getId_producto() + " - cantidad insuficiente " + producto.getCantidad());
					mensajes.add(mensaje);
					ctx.setRollbackOnly();
				}
			}
			//ventaCabecera.setVentaDetalles(ventaDetalleList);
		}
		catch(Exception e){
			MensajeVenta mensaje = new MensajeVenta();
			mensaje.setMensaje(e.getMessage());
			/*mensaje.setVenta(Venta);
			mensajes.add(mensaje);*/
			ctx.setRollbackOnly();
		}
    		
    	return mensajes;
    }
    
    public void registerVentaCabecera(VentaCabecera ventaCabecera) throws ServiceException {
    	try{
    		log.info("Registering VENTA Cabecera --- " + ventaCabecera.getCliente().getNombre());
            em.persist(ventaCabecera);
            ventaCabeceraEventSrc.fire(ventaCabecera);
    	}catch (Exception e){
    		ctx.setRollbackOnly();
			new ServiceException(e);
    	}
    }
    
    public void registerVentaDetalle(VentaDetalle ventaDetalle) throws ServiceException {
    	try{
    		log.info("Registering VENTA Detalle --- " + ventaDetalle.getProducto().getNombre());
            em.persist(ventaDetalle);
            ventaDetalleEventSrc.fire(ventaDetalle);
    	}catch (Exception e){
    		ctx.setRollbackOnly();
			new ServiceException(e);
    	}
    }
}
