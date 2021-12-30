package si.fri.rso.uporabniki.api.v1;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.kumuluz.ee.discovery.annotations.RegisterService;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Customers API", version = "v1",
        contact = @Contact(email = "rso2021jklk@gmail.com"),
        license = @License(name = "dev"), description = "API for managing customers."),
        servers = @Server(url = "http://20.83.140.172:8080/"))
@ApplicationPath("/v1")
public class CustomerApplication extends Application {

}
