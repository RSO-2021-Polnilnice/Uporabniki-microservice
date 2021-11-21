package si.fri.rso.uporabniki.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import si.fri.rso.uporabniki.lib.Customer;
import si.fri.rso.uporabniki.models.converters.CustomerConverter;
import si.fri.rso.uporabniki.models.entities.CustomerEntity;
import si.fri.rso.uporabniki.services.config.RestProperties;


@RequestScoped
public class CustomerBean {

    private Logger log = Logger.getLogger(CustomerBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Customer> getCustomer() {

        TypedQuery<CustomerEntity> query = em.createNamedQuery(
                "CustomerEntity.getAll", CustomerEntity.class);

        List<CustomerEntity> resultList = query.getResultList();

        return resultList.stream().map(CustomerConverter::toDto).collect(Collectors.toList());

    }

    public List<Customer> getCustomerFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, CustomerEntity.class, queryParameters).stream()
                .map(CustomerConverter::toDto).collect(Collectors.toList());
    }

    public Customer getCustomer(String username) {

        CustomerEntity customerEntity = em.find(CustomerEntity.class, username);

        if (customerEntity == null) {
            throw new NotFoundException();
        }

        Customer customer = CustomerConverter.toDto(customerEntity);

        return customer;
    }

    public Customer createCustomer(Customer customer) {

        // Searching if user with such username already exists
        CustomerEntity findExistingCustomerEntity = em.find(CustomerEntity.class, customer.getUsername());

        // If it exists, throw error
        if (findExistingCustomerEntity != null) {
            throw new RuntimeException("User with this username already exists");
        }

        CustomerEntity customerEntity = CustomerConverter.toEntity(customer);

        try {
            beginTx();
            em.persist(customerEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (customerEntity.getUsername() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return CustomerConverter.toDto(customerEntity);
    }

    public Customer putCustomer(String username, Customer customer) {

        CustomerEntity c = em.find(CustomerEntity.class, username);

        if (c == null) {
            return null;
        }

        CustomerEntity updatedCustomerEntity = CustomerConverter.toEntity(customer);

        try {
            beginTx();
            updatedCustomerEntity.setUsername(c.getUsername());
            updatedCustomerEntity = em.merge(updatedCustomerEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return CustomerConverter.toDto(updatedCustomerEntity);
    }

    public boolean deleteCustomer(String username) {

        CustomerEntity customer = em.find(CustomerEntity.class, username);

        if (customer != null) {
            try {
                beginTx();
                em.remove(customer);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}