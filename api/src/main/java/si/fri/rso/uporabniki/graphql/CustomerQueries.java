package si.fri.rso.uporabniki.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import si.fri.rso.uporabniki.lib.Customer;
import si.fri.rso.uporabniki.services.beans.CustomerBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class CustomerQueries {

    @Inject
    private CustomerBean customerBean;

    @GraphQLQuery
    public PaginationWrapper<Customer> allCustomers(@GraphQLArgument(name = "pagination") Pagination pagination,
                                                        @GraphQLArgument(name = "sort") Sort sort,
                                                        @GraphQLArgument(name = "filter") Filter filter) {

        return GraphQLUtils.process(customerBean.getCustomer(), pagination, sort, filter);
    }

    @GraphQLQuery
    public Customer getCustomer(@GraphQLArgument(name = "username") String username) {
        return customerBean.getCustomer(username);
    }

}