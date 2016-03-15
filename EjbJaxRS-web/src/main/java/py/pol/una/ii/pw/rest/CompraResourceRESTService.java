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

import py.pol.una.ii.pw.data.CompraRepository;
import py.pol.una.ii.pw.model.CompraCabecera;
import py.pol.una.ii.pw.service.CompraRegistration;
import py.pol.una.ii.pw.util.Compra;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the compraCabecera and compraDetalle table.
 */
@Path("/compras")
@RequestScoped
public class CompraResourceRESTService {
    @Inject
    private Logger log;

    @Inject
    private CompraRepository repository;

    @Inject
    CompraRegistration registration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CompraCabecera> listAllCompras() {
    	List<CompraCabecera> compras = repository.findAllOrderedById();
    	System.out.println("Objeto a retornar" + compras.size());
        return compras;
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public CompraCabecera lookupCompraById(@PathParam("id") Integer id) {
    	CompraCabecera compra = repository.findById(id);
        if (compra == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        System.out.println("Objeto a retornar" + compra);
        return compra;
    }

    /**
     * Creates a new compra from the values provided. Performs validation, and will return a JAX-RS response with either 200 ok,
     * or with a map of fields, and related errors.
     */
    @POST
    @Path("/nueva")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCompra(Compra compra) {
    	System.out.println("Objeto recibido---" + compra);
        Response.ResponseBuilder builder = null;
        try {
            registration.registerCompra(compra);

            // Create an "ok" response
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
