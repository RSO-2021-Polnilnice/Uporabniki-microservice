package si.fri.rso.uporabniki.services.beans;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.Console;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.jboss.weld.bootstrap.api.helpers.ForwardingBootstrap;
import si.fri.rso.uporabniki.lib.Customer;
import si.fri.rso.uporabniki.models.converters.CustomerConverter;
import si.fri.rso.uporabniki.models.entities.CustomerEntity;


@RequestScoped
public class CustomerBean {

    private Logger log = Logger.getLogger(CustomerBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private HttpClient httpClient;

    private String baseUrl;

    @PostConstruct
    private void init() {
        baseUrl = "http://localhost:8081";
    }

    public List<Customer> getCustomer() {

        TypedQuery<CustomerEntity> query = em.createNamedQuery(
                "CustomerEntity.getAll", CustomerEntity.class);

        List<CustomerEntity> resultList = query.getResultList();

        return resultList.stream().map(CustomerConverter::toDto).collect(Collectors.toList());

    }

    @Timed
    public List<Customer> getCustomerFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, CustomerEntity.class, queryParameters).stream()
                .map(CustomerConverter::toDto).collect(Collectors.toList());
    }

    public List<Customer> getCustomerEmails() {

        TypedQuery<CustomerEntity> query = em.createNamedQuery(
                "CustomerEntity.getSubscribedEmails", CustomerEntity.class);

        List<CustomerEntity> resultList = query.getResultList();

        return resultList.stream().map(CustomerConverter::toDto).collect(Collectors.toList());
    }

    public Customer getCustomer(Integer id) {

        CustomerEntity customerEntity = em.find(CustomerEntity.class, id);

        if (customerEntity == null) {
            throw new NotFoundException();
        }

        Customer customer = CustomerConverter.toDto(customerEntity);
        customer.setCharging(this.isCharging(customer.getId()));

        return customer;
    }

    public Customer createCustomer(Customer customer) {

        TypedQuery<CustomerEntity> query = em.createQuery(
                "SELECT c FROM CustomerEntity c WHERE c.username = '" + customer.getUsername() +"'",
                CustomerEntity.class);

        List<CustomerEntity> resultList = query.getResultList();

        System.out.println(resultList.size());

        if (resultList.size() != 0) {
            return null;
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

    public Customer putCustomer(Integer id, Customer customer) {

        CustomerEntity c = em.find(CustomerEntity.class, id);

        if (c == null) {
            return null;
        }

        CustomerEntity updatedCustomerEntity = CustomerConverter.toEntity(customer);

        try {
            beginTx();
            updatedCustomerEntity.setId(c.getId());
            updatedCustomerEntity = em.merge(updatedCustomerEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return CustomerConverter.toDto(updatedCustomerEntity);
    }

    public boolean deleteCustomer(Integer id) {

        CustomerEntity customer = em.find(CustomerEntity.class, id);

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

    // @Retry
    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "isChargingFallback")
    public Boolean isCharging(Integer id) {

        log.info("Calling polnilnice service: getting number of polnilnice.");

        try {
            HttpGet request = new HttpGet(baseUrl + "/v1/polnilnice/" + id);
            HttpResponse response = httpClient.execute(request);

            int status = response.getStatusLine().getStatusCode();

            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();

                if (entity != null)
                    return true;
            } else {
                String msg = "Remote server '" + baseUrl + "' failed with status " + status + ".";
                throw new InternalServerErrorException(msg);
            }
        }
        catch (WebApplicationException | ProcessingException e) {
            log.severe(e.getMessage());
            throw new InternalServerErrorException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO fix this
        return false;
    }

    public Boolean isChargingFallback(Integer id) {
        return false;
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