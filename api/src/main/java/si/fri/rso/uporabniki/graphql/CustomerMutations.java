package si.fri.rso.uporabniki.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import si.fri.rso.uporabniki.lib.Customer;
import si.fri.rso.uporabniki.services.beans.CustomerBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@GraphQLClass
@ApplicationScoped
public class CustomerMutations {

    @Inject
    private CustomerBean customerBean;

    @GraphQLMutation
    public Customer addCustomer(@GraphQLArgument(name = "customer") Customer customer) {
        customerBean.createCustomer(customer);
        return customer;
    }

    @GraphQLMutation
    public DeleteResponse deleteCustomer(@GraphQLArgument(name = "id") Integer id) {
        return new DeleteResponse(customerBean.deleteCustomer(id));
    }

}