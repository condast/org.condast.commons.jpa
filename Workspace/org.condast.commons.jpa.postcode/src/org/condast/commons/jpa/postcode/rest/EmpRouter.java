package org.condast.commons.jpa.postcode.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.condast.commons.strings.StringUtils;

@Path("/emp")
public class EmpRouter {

	@GET
	@Path("/getEmp")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response getEmp( @PathParam("empRequest")String empRequest){
		StringBuilder empResponse = new StringBuilder();
		if (!StringUtils.isEmpty(empRequest)) {
			empResponse.append("FOUND: ");
			empResponse.append(empRequest);
		} else {
			empResponse.append("NOT FOUND: ");
			empResponse.append(empRequest);
		}
		return Response.ok(empResponse).build();
	}
}