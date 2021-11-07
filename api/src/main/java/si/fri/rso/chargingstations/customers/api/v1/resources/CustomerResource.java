package si.fri.rso.chargingstations.customers.api.v1.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

import si.fri.rso.chargingstations.customers.lib.Customer;
import si.fri.rso.chargingstations.customers.services.beans.CustomerBean;

@ApplicationScoped
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    private Logger log = Logger.getLogger(CustomerResource.class.getName());

    @Inject
    private CustomerBean customerBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getCustomer() {

        List<Customer> customer = customerBean.getCustomerFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(customer).build();
    }

    @GET
    @Path("/{customerId}")
    public Response getCustomer(@PathParam("customerId") Integer customerId) {

        Customer customer = customerBean.getCustomer(customerId);

        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(customer).build();
    }

    @POST
    public Response createCustomer(Customer customer) {

        if ((customer.getUsername() == null || customer.getFirstName() == null || customer.getLastName() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            customer = customerBean.createCustomer(customer);
        }

        return Response.status(Response.Status.CONFLICT).entity(customer).build();

    }

    @PUT
    @Path("{customerId}")
    public Response putCustomer(@PathParam("customerId") Integer customerId,
                                     Customer customer) {

        customer = customerBean.putCustomer(customerId, customer);

        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @DELETE
    @Path("{customerId}")
    public Response deleteCustomer(@PathParam("customerId") Integer customerId) {

        boolean deleted = customerBean.deleteCustomer(customerId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}