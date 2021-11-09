package si.fri.rso.uporabniki.models.converters;

import si.fri.rso.uporabniki.lib.Customer;
import si.fri.rso.uporabniki.models.entities.CustomerEntity;

public class CustomerConverter {

    public static Customer toDto(CustomerEntity entity) {

        Customer dto = new Customer();
        dto.setCustomerId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setYearBorn(entity.getYearBorn());
        dto.setFunds(entity.getFunds());

        return dto;

    }

    public static CustomerEntity toEntity(Customer dto) {

        CustomerEntity entity = new CustomerEntity();
        entity.setUsername(dto.getUsername());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setYearBorn(dto.getYearBorn());
        entity.setFunds(dto.getFunds());

        return entity;

    }

}
