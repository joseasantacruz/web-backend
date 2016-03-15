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
package py.pol.una.ii.pw.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import py.pol.una.ii.pw.model.Pago;
import py.pol.una.ii.pw.model.Cliente;
import py.pol.una.ii.pw.data.ClienteRepository;
import py.pol.una.ii.pw.data.PagoRepository;
import py.pol.una.ii.pw.service.ClienteRegistration;
import py.pol.una.ii.pw.service.PagoRegistration;
import py.pol.una.ii.pw.util.PagoJson;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the pagos table.
 */
@Path("/pagos")
@RequestScoped
public class PagosRESTService {
    @Inject
    private Logger log;

    @Inject
    PagoRegistration registration;
    
    @Inject
    PagoRepository repository;
    
    @Inject
    ClienteRepository repositoryCliente;
    
    @Inject
    ClienteRegistration registrationCliente;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Pago> listAllPagos() {
    	List<Pago> pagos = repository.findAllOrderedById();
    	System.out.println("Objeto a retornar" + pagos);
        return pagos;
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Pago lookupPagoById(@PathParam("id") Integer id) {
    	Pago pago = repository.findById(id);
        if (pago == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        System.out.println("Objeto a retornar" + pago);
        return pago;
    }
    /**
     * Creates a new pagos from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPago(PagoJson pago) {
    	
        Response.ResponseBuilder builder = null;
        try {
        	System.out.println("Objeto recibido *id_cliente : ---" + pago.getCliente() );
        	System.out.println("Objeto recibido *monto : ---" + pago.getMonto() );
        	System.out.println("Objeto recibido *fecha : ---" + pago.getFecha());
        	Cliente cliente = repositoryCliente.findById(pago.getCliente());
        	
        	Pago pagos = new Pago();
        	pagos.setCliente(cliente);
        	pagos.setFecha(pago.getFecha());
        	pagos.setMonto(pago.getMonto());
        	Float diferencia = cliente.getSaldo() - pago.getMonto();
        	cliente.setSaldo(diferencia);
            registration.register(pagos);
            registrationCliente.alter(cliente);
            log.info(" Creando Pago numero" + pagos.getId_Pago().toString());
            builder = Response.ok();
        }catch (Exception e) {
        	log.fine(e.getMessage());
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
        return builder.build();
    }
}
