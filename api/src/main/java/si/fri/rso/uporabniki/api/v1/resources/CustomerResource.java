package si.fri.rso.uporabniki.api.v1.resources;

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

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import si.fri.rso.uporabniki.lib.Customer;
import si.fri.rso.uporabniki.services.beans.CustomerBean;
import si.fri.rso.uporabniki.config.RestProperties;

@Log
@ApplicationScoped
@Path("/uporabniki")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, DELETE, OPTIONS")
public class CustomerResource {

    private static final Logger LOG = LogManager.getLogger(CustomerResource.class.getName());

    @Inject
    private CustomerBean customerBean;

    @Operation(description = "Get all customers.", summary = "Get all customers")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of users",
                    content = @Content(schema = @Schema(implementation = Customer.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getCustomer() {

        List<Customer> customer = customerBean.getCustomer();

        return Response.status(Response.Status.OK).entity(customer).build();
    }

    @Operation(description = "Get emails of subscribed customers.", summary = "Get emails of subscribed customers")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of users",
                    content = @Content(schema = @Schema(implementation = Customer.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    @Log(LogParams.METRICS)
    @Path(("/emails"))
    public Response getCustomerEmails() {

        List<Customer> customer = customerBean.getCustomerEmails();

        return Response.status(Response.Status.OK).entity(customer).build();
    }


    @Operation(description = "Get customer.", summary = "Get customer")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Customer",
                    content = @Content(
                            schema = @Schema(implementation = Customer.class))
            )})
    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") Integer id) {

        Customer customer = customerBean.getCustomer(id);

        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(customer).build();
    }

    @Inject
    private RestProperties properties;

    // TODO only for testing purposes, remove later
    @GET
    @Path("/config")
    public Response test() {
        String response =
                "{" +
                        "\"maintenanceMode\": %b," +
                        "\"broken\": %b" +
                        "}";

        response = String.format(
                response,
                properties.getMaintenanceMode(),
                properties.getBroken());

        return Response.ok(response).build();
    }


    @Operation(description = "Add customer.", summary = "Add customer")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Customer successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @Log(value = LogParams.METRICS)
    @POST
    public Response createCustomer(Customer customer) {

        customer.setCharging(false);

        if ((customer.getUsername() == null || customer.getFirstName() == null || customer.getLastName() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            customer = customerBean.createCustomer(customer);
        }

        return Response.status(Response.Status.CONFLICT).entity(customer).build();

    }

    @Operation(description = "Update customer.", summary = "Update customer")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Customer successfully updated."
            )
    })
    @PUT
    @Path("{id}")
    public Response putCustomer(@PathParam("id") Integer id,
                                     Customer customer) {

        customer = customerBean.putCustomer(id, customer);

        if (customer == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @Operation(description = "Delete customer.", summary = "Delete customer")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Customer successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{id}")
    public Response deleteCustomer(@PathParam("id") Integer id) {

        boolean deleted = customerBean.deleteCustomer(id);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}